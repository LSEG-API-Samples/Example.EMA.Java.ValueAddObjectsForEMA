/*
 * Copyright 2017 Thomson Reuters
 *
 * DISCLAIMER: This source code has been written by Thomson Reuters for the only 
 * purpose of illustrating articles published on the Thomson Reuters Developer 
 * Community. It has not been tested for usage in production environments. 
 * Thomson Reuters cannot be held responsible for any issues that may happen if 
 * these objects or the related source code is used in production or any other 
 * client environment.
 *
 * Thomson Reuters Developer Community: https://developers.thomsonreuters.com
 *
 */
package com.refinitiv.platformservices.elektron.objects.chain;

import com.refinitiv.ema.access.AckMsg;
import com.refinitiv.ema.access.Data;
import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.DataType.DataTypes;
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.FieldEntry;
import com.refinitiv.ema.access.GenericMsg;
import com.refinitiv.ema.access.Msg;
import com.refinitiv.ema.access.OmmConsumerClient;
import com.refinitiv.ema.access.OmmConsumerEvent;
import com.refinitiv.ema.access.OmmState;
import com.refinitiv.ema.access.Payload;
import com.refinitiv.ema.access.RefreshMsg;
import com.refinitiv.ema.access.StatusMsg;
import com.refinitiv.ema.access.UpdateMsg;
import com.refinitiv.ema.rdm.EmaRdm;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

class ChainRecord implements OmmConsumerClient
{
    private final String name;
    private final ChainRecordFactory factory;
    private final ChainRecordContext context;
    private ChainRecordListener listener;
    private final Fields image;
    private boolean isAChainRecord;
    private Fields fieldsReceivedDuringPreOpening;
    private long subscriptionHandle;
    private int instrumentStreamState;
    private long firstLinkPosition;
    private long summaryLinksToSkip;
    private ChainRecord nextChainRecord;

    private enum State {PREOPENED, OPENED, CLOSED};
    private State state;    
    
    public ChainRecord(
            String name,
            ChainRecordFactory factory,
            ChainRecordContext context,
            ChainRecordListener listener)
    {
        this.name = name;
        this.factory = factory;
        this.context = context;
        this.listener = listener;
        this.state = State.CLOSED;
        
        image = new Fields();
        image.setToblank();
        
        isAChainRecord = false;
        
        fieldsReceivedDuringPreOpening = null;
    }

    public String getName()
    {
        return name;
    }

    public synchronized void open()
    {
        if(state == State.OPENED)
            return;
        
        if(state == State.CLOSED)
        {
            subscribe();
            isAChainRecord = false;
        }
        
        if(fieldsReceivedDuringPreOpening != null)
        {
            image.applyAndNotify(fieldsReceivedDuringPreOpening);
            fieldsReceivedDuringPreOpening = null;
        }
        
        state = State.OPENED;
    }
    
    public synchronized void preOpen()
    {
        if(state != State.CLOSED)
            return;
        
        subscribe();
        state = State.PREOPENED;
    }

    public synchronized void close()
    {   
        if(state == State.CLOSED)
            return;

        state = State.CLOSED;
        unsubscribe();
        
        clearImageAndNotifyOfRemovedLinks();
        closeNextChainRecordIfNeeded();
    }
    
    private void clearImageAndNotifyOfRemovedLinks()
    {
        image.setToblank();

        long positionOffset = ChainRecord.this.firstLinkPosition - ChainRecord.this.summaryLinksToSkip;
        
        for(int linkPositionInChainRecord = 0; 
                linkPositionInChainRecord < image.refCount; 
                ++linkPositionInChainRecord)
        {
            long linkPositionInChain = positionOffset + linkPositionInChainRecord; 
            listener.onLinkRemoved(linkPositionInChain, ChainRecord.this);
        }
    }
    
    private void closeNextChainRecordIfNeeded()
    {
        if(nextChainRecord != null)
        {
            nextChainRecord.close();
            nextChainRecord = null;
        }   
    }
    
    public synchronized void setListener(ChainRecordListener listener)
    {
        this.listener = listener;
    }

    private void subscribe()
    {
        subscriptionHandle = context.getOmmConsumer()
                .registerClient(
                        EmaFactory.createReqMsg()
                                .domainType(EmaRdm.MMT_MARKET_PRICE)
                                .name(name)
                                .serviceName(context.getServiceName())
                                .interestAfterRefresh(context.getWithUpdates()),
                        this);
        instrumentStreamState = OmmState.StreamState.OPEN;
    }
    
    private void unsubscribe()
    {
        if(instrumentStreamState == OmmState.StreamState.CLOSED ||
           instrumentStreamState == OmmState.StreamState.CLOSED_RECOVER ||
           instrumentStreamState == OmmState.StreamState.CLOSED_REDIRECTED)
        {
            return;
        }
        
        context.getOmmConsumer().unregister(subscriptionHandle);
    }    
    
    @Override
    public synchronized void onRefreshMsg(RefreshMsg message, OmmConsumerEvent event)
    {
        instrumentStreamState = message.state().streamState();
        
        Payload payload = message.payload();
        if (!isAFieldList(payload) || !isAChainRecord(payload))
        {
            String errorMessage = context.getServiceName() + "/" + name + " is not a ChainRecord.";
            listener.onError(errorMessage, this);
            return;
        }

        Fields receivedFields = decodePayload(payload);
        digest(receivedFields);
    }

    @Override
    public synchronized void onUpdateMsg(UpdateMsg message, OmmConsumerEvent event)
    {
        Payload payload = message.payload();        
        if (!isAFieldList(payload))
        {

            String errorMessage = "Invalid update received for " + context.getServiceName() + "/" + name + ". The data type should be a FIELD_LIST.";
            listener.onError(errorMessage, this);
            return;
        }
        
        Fields receivedFields = decodePayload(payload);
        digest(receivedFields);
    }

    @Override
    public synchronized void onStatusMsg(StatusMsg message, OmmConsumerEvent event)
    {
        if (message.hasState())
        {
            instrumentStreamState = message.state().streamState();
                    
            if(message.state().dataState() == OmmState.DataState.SUSPECT)
            {
                String errorMessage = "Invalid status received for <" + name + ">: " + message.state();
                listener.onError(errorMessage, this);
            }
        }
    }

    @Override
    public synchronized void onGenericMsg(GenericMsg message, OmmConsumerEvent event)
    {}

    @Override
    public synchronized void onAckMsg(AckMsg message, OmmConsumerEvent event)
    {}

    @Override
    public synchronized void onAllMsg(Msg message, OmmConsumerEvent event)
    {}
    
    private boolean isAFieldList(Payload payload)
    {
        return payload.dataType() == DataType.DataTypes.FIELD_LIST;
    }

    public boolean isAChainRecord()
    {
        return isAChainRecord;
    }
    
    private boolean isAChainRecord(Payload payload)
    {
        TreeSet<Integer> mandatoryFieldsSet = getMandatoryFieldsInPayload(payload);

        isAChainRecord = false;
        
        if (mandatoryFieldsSet.containsAll(FieldIds.LINK_MANDATORY_FIELDS) ||
            mandatoryFieldsSet.containsAll(FieldIds.LONGLINK_MANDATORY_FIELDS) ||
            mandatoryFieldsSet.containsAll(FieldIds.BR_LINK_MANDATORY_FIELDS))
        {
            isAChainRecord = true;
        }
        
        return isAChainRecord;
    }
    
    private TreeSet<Integer> getMandatoryFieldsInPayload(Payload payload)
    {
        TreeSet<Integer> mandatoryFieldsSetInPayload = new TreeSet<>();
        payload.fieldList().forEach((FieldEntry fieldEntry) ->
        {
            int fieldId = fieldEntry.fieldId();
            if (FieldIds.ALL_MANDATORY_FIELDS.contains(fieldId))
            {
                mandatoryFieldsSetInPayload.add(fieldId);
            }
        });

        return mandatoryFieldsSetInPayload;  
    }

    private Fields decodePayload(Payload payload)
    {               
        Fields fields = new Fields();       
        fields.decodePayload(payload);
        
        return fields;
    }

    private void digest(Fields fields)
    {
        if(state == State.OPENED)
        {
            image.applyAndNotify(fields);
        }
        else if(state == State.PREOPENED)
        {
            if(fieldsReceivedDuringPreOpening == null)
            {
                fieldsReceivedDuringPreOpening = fields;
            }
            else
            {
                fieldsReceivedDuringPreOpening.apply(fields);
            }
        }
    }
    
    private void computeSummaryLinkToSkip()
    {
        if (context.getSummaryLinksToSkipByDisplayTemplate() == null || !isFirstChainRecord())
        {
            summaryLinksToSkip = 0;
            return;
        }

        long displayTemplateId = 0;
        
        if (image.prefDisp > 0)
        {
            displayTemplateId = image.prefDisp;
        } 
        else if (image.prevDisp > 0)
        {
            displayTemplateId = image.prevDisp;
        } 
        else if (image.rdnDisplay > 0)
        {
            displayTemplateId = image.rdnDisplay;
        }

        summaryLinksToSkip = context.getSummaryLinksToSkipByDisplayTemplate()
                .getLinksToSkipForDisplayTemplate(displayTemplateId);        
    }

    private void openNextChainRecord()
    {
        closeNextChainRecordIfNeeded();
        
        String nextRecordName = image.nextLR;
        
        if(!nextRecordName.isEmpty())
        {
            nextChainRecord = factory.acquire(nextRecordName);
            nextChainRecord.firstLinkPosition = firstLinkPosition + image.refCount - summaryLinksToSkip;
            nextChainRecord.open();
        }
    }

    private boolean isFirstChainRecord()
    {
        return image.prevLR.isEmpty();
    }
    
    private static class FieldIds
    {
        private static final int RDNDISPLAY = 2;
        private static final int PREV_LR = 237;
        private static final int NEXT_LR = 238;
        private static final int REF_COUNT = 239;
        private static final int LINK_1 = 240;
        private static final int LINK_2 = 241;
        private static final int LINK_3 = 242;
        private static final int LINK_4 = 243;
        private static final int LINK_5 = 244;
        private static final int LINK_6 = 245;
        private static final int LINK_7 = 246;
        private static final int LINK_8 = 247;
        private static final int LINK_9 = 248;
        private static final int LINK_10 = 249;
        private static final int LINK_11 = 250;
        private static final int LINK_12 = 251;
        private static final int LINK_13 = 252;
        private static final int LINK_14 = 253;
        private static final int LONGLINK1 = 800;
        private static final int LONGLINK2 = 801;
        private static final int LONGLINK3 = 802;
        private static final int LONGLINK4 = 803;
        private static final int LONGLINK5 = 804;
        private static final int LONGLINK6 = 805;
        private static final int LONGLINK7 = 806;
        private static final int LONGLINK8 = 807;
        private static final int LONGLINK9 = 808;
        private static final int LONGLINK10 = 809;
        private static final int LONGLINK11 = 810;
        private static final int LONGLINK12 = 811;
        private static final int LONGLINK13 = 812;
        private static final int LONGLINK14 = 813;
        private static final int LONGPREVLR = 814;
        private static final int LONGNEXTLR = 815;
        private static final int PREF_DISP = 1080;
        private static final int PREV_DISP = 3263;
        private static final int BR_LINK1 = 6409;
        private static final int BR_LINK2 = 6410;
        private static final int BR_LINK3 = 6411;
        private static final int BR_LINK4 = 6412;
        private static final int BR_LINK5 = 6413;
        private static final int BR_LINK6 = 6414;
        private static final int BR_LINK7 = 6415;
        private static final int BR_LINK8 = 6416;
        private static final int BR_LINK9 = 6417;
        private static final int BR_LINK10 = 6418;
        private static final int BR_LINK11 = 6419;
        private static final int BR_LINK12 = 6420;
        private static final int BR_LINK13 = 6421;
        private static final int BR_LINK14 = 6422;
        private static final int BR_PREVLR = 6423;
        private static final int BR_NEXTLR = 6424;

        private static final List<Integer> ALL_MANDATORY_FIELDS = Arrays.asList(PREV_LR, NEXT_LR, REF_COUNT, LINK_1, LONGLINK1, LONGPREVLR, LONGNEXTLR, BR_LINK1, BR_PREVLR, BR_NEXTLR);
        private static final TreeSet<Integer> LINK_MANDATORY_FIELDS = new TreeSet<>(Arrays.asList(PREV_LR, NEXT_LR, REF_COUNT, LINK_1));
        private static final TreeSet<Integer> LONGLINK_MANDATORY_FIELDS = new TreeSet<>(Arrays.asList(REF_COUNT, LONGLINK1, LONGPREVLR, LONGNEXTLR));
        private static final TreeSet<Integer> BR_LINK_MANDATORY_FIELDS = new TreeSet<>(Arrays.asList(REF_COUNT, BR_LINK1, BR_PREVLR, BR_NEXTLR));
    }    
    
    private class Fields
    {
        private static final int LINKS_COUNT = 14;
        private final String links[];
        private int refCount = -1;
        private String prevLR = null;
        private String nextLR = null;
        private long prefDisp = -1;
        private long prevDisp = -1;
        private long rdnDisplay = -1;
        
        private Fields()
        {
            links = new String[LINKS_COUNT];
        }
        
        private void setToblank()
        {
            refCount = 0;
            prevLR = "";
            nextLR = "";
            prefDisp = 0;
            prevDisp = 0;
            rdnDisplay = 0;
            
            for(int linkId = 0; 
                    linkId < LINKS_COUNT; 
                    ++linkId)
            {
                links[linkId] = "";
            }
        }
        
        private void apply(Fields newFields)
        {
            if(newFields.prefDisp != -1)
                prefDisp = newFields.prefDisp;

            if(newFields.prevDisp != -1)
                prevDisp = newFields.prevDisp;

            if(newFields.rdnDisplay != -1)
                rdnDisplay= newFields.rdnDisplay;

            if(newFields.prevLR != null)
                prevLR = newFields.prevLR;                
                                    
            if(newFields.nextLR != null)
                nextLR = newFields.nextLR;                
            
            if(newFields.refCount != -1)
                refCount= newFields.refCount;
                
            for(int linkId = 0; 
                    linkId < LINKS_COUNT; 
                    ++linkId)
            {
                if(newFields.links[linkId] != null)
                {
                    links[linkId] = newFields.links[linkId];
                }
            }
        }
        
        private void applyAndNotify(Fields newFields)
        {
            applyAndNotifyPrevLinkFieldOf(newFields);
            applyAndNotifyDisplayTemplateFieldsOf(newFields);
            applyAndNotifyRefCountAndLinkFieldsOf(newFields);
            applyAndNotifyNextLinkFieldOf(newFields); 
        } 
        
        private void applyAndNotifyPrevLinkFieldOf(Fields newFields)
        {
            if(newFields.prevLR != null)
                prevLR = newFields.prevLR;             
        }
        
        private void applyAndNotifyDisplayTemplateFieldsOf(Fields newFields)
        {
            if(newFields.prefDisp != -1 || newFields.prevDisp != -1 || newFields.rdnDisplay != -1)
            {
                if(newFields.prefDisp != -1)
                    prefDisp = newFields.prefDisp;

                if(newFields.prevDisp != -1)
                    prevDisp = newFields.prevDisp;

                if(newFields.rdnDisplay != -1)
                    rdnDisplay= newFields.rdnDisplay;
                
                ChainRecord.this.computeSummaryLinkToSkip();
            }          
        }
        
        private void applyAndNotifyRefCountAndLinkFieldsOf(Fields newFields)
        {
            int previousRefCount = refCount;
            int newRefCount = newFields.refCount;            
            if(newRefCount >= 0)
            {
                refCount = newFields.refCount;
            }
            else
            {
                newRefCount = previousRefCount;                
            }
            
            int lowerRefCount = Math.min(previousRefCount, newRefCount);
            
            applyLinksNotImpactedByRefCountChange(newFields, lowerRefCount);
            applyLinksImpactedByRefCountChange(newFields, previousRefCount, newRefCount);
        }
        
        private void applyLinksNotImpactedByRefCountChange(Fields newFields, int lowerRefCount)
        {
            for(int linkPositionInChainRecord = 0; 
                    linkPositionInChainRecord < lowerRefCount; 
                    ++linkPositionInChainRecord)
            {
                String newLink = newFields.links[linkPositionInChainRecord];

                if (linkPositionInChainRecord < ChainRecord.this.summaryLinksToSkip
                        || newLink == null)
                {
                    continue;
                }
                
                String currentLink = links[linkPositionInChainRecord];
                if(!currentLink.equals(newLink))
                {
                    links[linkPositionInChainRecord] = newLink;                    
                    long linkPositionInChain = getPositionOffset() + linkPositionInChainRecord;                 
                    ChainRecord.this.listener.onLinkChanged(linkPositionInChain , currentLink, newLink, ChainRecord.this);
                }
            }
        }
        
        private void applyLinksImpactedByRefCountChange(Fields newFields, int previousRefCount, int newRefCount)
        {
            if(newRefCount > previousRefCount)
            {
                applyLinksImpactedByRefCountIncrease(newFields, previousRefCount, newRefCount);
            }
            else if(newRefCount < previousRefCount)
            {
                applyLinksImpactedByRefCountDecrease(newFields, previousRefCount, newRefCount);
            }                                       
        }
        
        private void applyLinksImpactedByRefCountIncrease(Fields newFields, int previousRefCount, int newRefCount)
        {
            for(int linkPositionInChainRecord = previousRefCount; 
                    linkPositionInChainRecord < newRefCount; 
                    ++linkPositionInChainRecord)
            {
                if (linkPositionInChainRecord < ChainRecord.this.summaryLinksToSkip)
                {
                    continue;
                }

                long linkPositionInChain = getPositionOffset() + linkPositionInChainRecord; 

                String newLink = newFields.links[linkPositionInChainRecord];
                if(newLink!=null)
                {
                    links[linkPositionInChainRecord] = newLink;
                }
                ChainRecord.this.listener.onLinkAdded(linkPositionInChain , links[linkPositionInChainRecord], ChainRecord.this);                    
            }                            
        }
        
        private void applyLinksImpactedByRefCountDecrease(Fields newFields, int previousRefCount, int newRefCount)
        {
            for(int linkPositionInChainRecord = newRefCount; 
                    linkPositionInChainRecord < previousRefCount; 
                    ++linkPositionInChainRecord)
            {
                if (linkPositionInChainRecord < ChainRecord.this.summaryLinksToSkip)
                {
                    continue;
                }

                long linkPositionInChain = getPositionOffset() + linkPositionInChainRecord; 
                ChainRecord.this.listener.onLinkRemoved(linkPositionInChain, ChainRecord.this);
            }                            
        }        
        
        private long getPositionOffset()
        {
            return ChainRecord.this.firstLinkPosition - ChainRecord.this.summaryLinksToSkip;
        }
        
        private void applyAndNotifyNextLinkFieldOf(Fields newFields)
        {
            if(newFields.nextLR != null)
            {
                if(!nextLR.equals(newFields.nextLR))
                {
                    nextLR = newFields.nextLR;
                    ChainRecord.this.openNextChainRecord();
                }
                else if(newFields.nextLR.isEmpty())
                {
                    ChainRecord.this.listener.onCompleted(ChainRecord.this);
                }
            }
        }
        
        private void decodePayload(Payload payload)
        {               
            for (FieldEntry fieldEntry : payload.fieldList())
            {
                int fieldId = fieldEntry.fieldId();

                switch (fieldId)
                {
                    case FieldIds.REF_COUNT:
                        decodeRefCountField(fieldEntry);
                        break;
                    case FieldIds.PREV_LR:
                    case FieldIds.LONGPREVLR:
                    case FieldIds.BR_PREVLR:
                        decodePrevField(fieldEntry);
                        break;
                    case FieldIds.NEXT_LR:
                    case FieldIds.LONGNEXTLR:
                    case FieldIds.BR_NEXTLR:
                        decodeNextField(fieldEntry);
                        break;
                    case FieldIds.LINK_1:
                    case FieldIds.LINK_2:
                    case FieldIds.LINK_3:
                    case FieldIds.LINK_4:
                    case FieldIds.LINK_5:
                    case FieldIds.LINK_6:
                    case FieldIds.LINK_7:
                    case FieldIds.LINK_8:
                    case FieldIds.LINK_9:
                    case FieldIds.LINK_10:
                    case FieldIds.LINK_11:
                    case FieldIds.LINK_12:
                    case FieldIds.LINK_13:
                    case FieldIds.LINK_14:
                        decodeLinkField(fieldEntry, fieldId, FieldIds.LINK_1);
                        break;
                    case FieldIds.LONGLINK1:
                    case FieldIds.LONGLINK2:
                    case FieldIds.LONGLINK3:
                    case FieldIds.LONGLINK4:
                    case FieldIds.LONGLINK5:
                    case FieldIds.LONGLINK6:
                    case FieldIds.LONGLINK7:
                    case FieldIds.LONGLINK8:
                    case FieldIds.LONGLINK9:
                    case FieldIds.LONGLINK10:
                    case FieldIds.LONGLINK11:
                    case FieldIds.LONGLINK12:
                    case FieldIds.LONGLINK13:
                    case FieldIds.LONGLINK14:
                        decodeLinkField(fieldEntry, fieldId, FieldIds.LONGLINK1);
                        break;
                    case FieldIds.BR_LINK1:
                    case FieldIds.BR_LINK2:
                    case FieldIds.BR_LINK3:
                    case FieldIds.BR_LINK4:
                    case FieldIds.BR_LINK5:
                    case FieldIds.BR_LINK6:
                    case FieldIds.BR_LINK7:
                    case FieldIds.BR_LINK8:
                    case FieldIds.BR_LINK9:
                    case FieldIds.BR_LINK10:
                    case FieldIds.BR_LINK11:
                    case FieldIds.BR_LINK12:
                    case FieldIds.BR_LINK13:
                    case FieldIds.BR_LINK14:
                        decodeLinkField(fieldEntry, fieldId, FieldIds.BR_LINK1);
                        break;
                    case FieldIds.PREF_DISP:
                    case FieldIds.PREV_DISP:
                    case FieldIds.RDNDISPLAY:
                        decodeDisplayTemplateField(fieldEntry, fieldId);
                        break;
                }
            }
        }        
        
        private void decodeRefCountField(FieldEntry fieldEntry)
        {
            if(fieldEntry.code() == Data.DataCode.BLANK || fieldEntry.loadType() != DataTypes.UINT)
                refCount = 0;
            else
                refCount = (int)fieldEntry.uintValue();

            if(refCount < 0)
                refCount = 0;
            
            if(refCount > 14)
                refCount = 14;
        }

        private void decodePrevField(FieldEntry fieldEntry)
        {
            prevLR = extractStringFrom(fieldEntry);
        }

        private void decodeNextField(FieldEntry fieldEntry)
        {
            nextLR = extractStringFrom(fieldEntry);
        }

        private void decodeLinkField(FieldEntry fieldEntry, int fieldId, int fieldBase)
        {
            int linkPositionInChainRecord = fieldId - fieldBase;

            String instrumentName = extractStringFrom(fieldEntry);
            links[linkPositionInChainRecord] = instrumentName;
        }

        private String extractStringFrom(FieldEntry fieldEntry)
        {
            String extractedString = "";

            if(fieldEntry.code() != Data.DataCode.BLANK 
                    && fieldEntry.loadType() == DataTypes.ASCII)
            {
                extractedString = fieldEntry.ascii().ascii();
            }
            
            return extractedString;
        }

        private void decodeDisplayTemplateField(FieldEntry fieldEntry, int fieldId)
        {
            if (ChainRecord.this.context.getSummaryLinksToSkipByDisplayTemplate() == null)
            {
                return;
            }
            
            long templateId = 0;
            
            if(fieldEntry.code() != Data.DataCode.BLANK
                    && fieldEntry.loadType() == DataTypes.UINT)
            {
                templateId = fieldEntry.uintValue();
            }

            switch (fieldId)
            {
                case FieldIds.PREF_DISP:
                    prefDisp = templateId;
                    break;
                case FieldIds.PREV_DISP:
                    prevDisp = templateId;
                    break;
                case FieldIds.RDNDISPLAY:
                    rdnDisplay = templateId;
                    break;
            }
        }    
    }   
}
