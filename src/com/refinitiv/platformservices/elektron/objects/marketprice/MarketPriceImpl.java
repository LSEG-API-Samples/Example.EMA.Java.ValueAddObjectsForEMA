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
package com.refinitiv.platformservices.elektron.objects.marketprice;

import com.refinitiv.platformservices.elektron.objects.data.Field;
import com.refinitiv.ema.access.AckMsg;
import com.refinitiv.ema.access.Data;
import com.refinitiv.ema.access.ElementList;
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.FieldEntry;
import com.refinitiv.ema.access.FieldList;
import com.refinitiv.ema.access.GenericMsg;
import com.refinitiv.ema.access.Msg;
import com.refinitiv.ema.access.OmmArray;
import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.ema.access.OmmConsumerClient;
import com.refinitiv.ema.access.OmmConsumerEvent;
import com.refinitiv.ema.access.OmmState;
import static com.refinitiv.ema.access.OmmState.StreamState;
import com.refinitiv.ema.access.RefreshMsg;
import com.refinitiv.ema.access.ReqMsg;
import com.refinitiv.ema.access.StatusMsg;
import com.refinitiv.ema.access.UpdateMsg;
import com.refinitiv.ema.rdm.DataDictionary;
import com.refinitiv.ema.rdm.DictionaryUtility;
import com.refinitiv.ema.rdm.EmaRdm;
import com.refinitiv.platformservices.elektron.objects.common.Dispatcher;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The implementation class of the <code>MarketPrice</code> interface.
 * It implements the level 1 instruments subscription logic, the image cache 
 * management, the partial updates management...
 */
class MarketPriceImpl implements MarketPrice, OmmConsumerClient
{
    private static final int WAITING_LOOP_TIMEOUT_IN_MS = 200;
    private static final int UNSET_STREAMID = -1;
    
    private final OmmConsumer ommConsumer;
    private final String name;
    private final String serviceName;
    private final boolean withUpdates;
    private final boolean partialUpdatesManagementActivated;
    private final boolean synchronousModeActivated;
    private final boolean autoDispatch;
    private final LinkedList<Integer> fieldIds;
    private final LinkedList<String> fieldNames;
    private final OnCompleteFunction onCompleteFunction;
    private final OnImageFunction onImageFunction;
    private final OnUpdateFunction onUpdateFunction;
    private final OnStateFunction onStateFunction;
    
    private enum State {OPENING, OPENED, CLOSED};
    private State state;
    private int streamId = -1;
    private long subscriptionHandle;
    private OmmState ommState;
    private final Map<Integer, Field> fieldsById = new TreeMap<>();    
    private final Map<String, Field> fieldsByName = new TreeMap<>();  
    private static Dispatcher dispatcher; 

    /**
     * Constructor used by the <code>MarketPrice.Builder</code> to build a new 
     * <code>MarketPrice</code> object. 
     * @param builder the <code>Builder</code> that contains all the parameters
     * required to build the class. This is this builder that instantiated this 
     * object by calling this constructor.
     */
    MarketPriceImpl(MarketPrice.Builder builder) 
    {
        ommConsumer = builder.ommConsumer;
        name = builder.name;
        serviceName = builder.serviceName;
        withUpdates = builder.withUpdates;
        partialUpdatesManagementActivated = builder.partialUpdatesManagementActivated;
        synchronousModeActivated = builder.synchronousModeActivated;
        autoDispatch = builder.autoDispatch;
        fieldIds = builder.fieldIds;
        fieldNames = builder.fieldNames;
        onCompleteFunction = builder.onCompleteFunction;
        onImageFunction = builder.onImageFunction;
        onUpdateFunction = builder.onUpdateFunction;
        onStateFunction = builder.onStateFunction;
        
        state = State.CLOSED;
        
        dispatcher = new Dispatcher.Builder()
                .withOmmConsumer(ommConsumer)
                .build();        
    }

    @Override
    public String getName() 
    {
        return name;
    }

    @Override
    public String getServiceName() 
    {
        return serviceName;
    }

    @Override
    public void open() 
    {
        synchronized(this)
        {
            if(state == State.OPENED || state == State.OPENING)
                return;

            state = State.OPENING;

            ReqMsg requestMessage = EmaFactory.createReqMsg()
                                                    .name(name)
                                                    .serviceName(serviceName)
                                                    .interestAfterRefresh(withUpdates);
                    
            ElementList view = buildEmaView();
            if(view != null)
            {
                requestMessage.payload(view);
            }

            subscriptionHandle = ommConsumer.registerClient(requestMessage, this);
        }
  
        if(synchronousModeActivated)
        {
            waitForCompletion();
        }
    }

    @Override
    public synchronized void close() 
    {
        if(state == State.CLOSED)
            return;
        
        ommConsumer.unregister(subscriptionHandle);
        state = State.CLOSED;
        streamId = -1;
        clearCachedImage();
        ommState = null;
    }

    @Override
    public synchronized boolean isComplete() 
    {
        return state == State.OPENED;
    }
    
    @Override
    public synchronized Collection<Field> getFields() 
    {
        Collection<Field> deepCopiedFieldsToReturn = Collections.unmodifiableCollection(fieldsById.values());
        
        return deepCopiedFieldsToReturn;        
    }

    @Override
    public synchronized Field getField(String fieldName)
    {
        return fieldsByName.get(fieldName);
    }
    
    @Override
    public synchronized Field getField(int fieldId)
    {
        return fieldsById.get(fieldId);
    }
    
    @Override
    public synchronized OmmState getState() 
    {
        return ommState;
    }
    
    @Override
    public int getStreamId()
    {
        return streamId;
    }
        
    @Override
    public synchronized void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent consumerEvent) 
    {   
        if(streamId == UNSET_STREAMID)
        {
            streamId = refreshMsg.streamId();
        }
        
        ommState = refreshMsg.state();
        
        if(refreshMsg.domainType() == EmaRdm.MMT_MARKET_PRICE)
        {
            Collection<Field> image = extractFieldsFrom(refreshMsg.payload().fieldList());            
            applyImage(image);
            onImageFunction.onImage(this, image, ommState);
        }
        
        if(state == State.OPENING)
        {
            state = State.OPENED;
            onCompleteFunction.onComplete(this);
        }
    }

    @Override
    public synchronized void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent consumerEvent) 
    {
        if(streamId == UNSET_STREAMID)
        {
            streamId = updateMsg.streamId();
        }
        
        if(updateMsg.domainType() == EmaRdm.MMT_MARKET_PRICE)
        {
            Collection<Field> update = extractFieldsFrom(updateMsg.payload().fieldList());            
            applyUpdate(update);
            onUpdateFunction.onUpdate(this, Collections.unmodifiableCollection(update));
        }
    }

    @Override
    public synchronized void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent consumerEvent) 
    {
        if(streamId == UNSET_STREAMID)
        {
            streamId = statusMsg.streamId();
        }
        
        ommState = statusMsg.state();
        onStateFunction.onState(this, ommState);
        
        if((ommState.streamState() == StreamState.CLOSED ||
            ommState.streamState() == StreamState.CLOSED_RECOVER ||
            ommState.streamState() == StreamState.CLOSED_REDIRECTED)
                &&
           state == State.OPENING)
        {
            state = State.OPENED;
            onCompleteFunction.onComplete(this);
        }
    }

    @Override
    public synchronized void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent) {}

    @Override
    public synchronized void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent) {}

    @Override
    public synchronized void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent) {
    }      
    
    
    /**
     * Build the EMA ElementList that describes the View used by this MarketPrice.<br>
     * This ElementList will be used to specify the view in the EMA RequestMsg.
     * @return the ElementList that describes the View
     */
    private ElementList buildEmaView() 
    {
        if(fieldIds.isEmpty() && fieldNames.isEmpty())
        {
            return null;
        }
        
        if(!fieldIds.isEmpty())
        {
            return buildEmaViewBasedOnFieldIds() ;
        }
        else
        {
            return buildEmaViewBasedOnFieldNames();
        }
    }    
    
    /**
     * Build an EMA View based on field Ids.
     * @return the ElementList that describes the View
     */
    private ElementList buildEmaViewBasedOnFieldIds() 
    {
        ElementList view = EmaFactory.createElementList();
        OmmArray array = EmaFactory.createOmmArray();
        
        for(Integer fieldId : fieldIds)
        {
            array.add(EmaFactory.createOmmArrayEntry().intValue(fieldId));
        }

        view.add(EmaFactory.createElementEntry().uintValue(EmaRdm.ENAME_VIEW_TYPE, 1));        
        view.add(EmaFactory.createElementEntry().array(EmaRdm.ENAME_VIEW_DATA, array));
        
        return view;
    }

    /**
     * Build an EMA View is based on field names.
     * @return the ElementList that describes the View
     */    
    private ElementList buildEmaViewBasedOnFieldNames() 
    {
        ElementList view = EmaFactory.createElementList();
        OmmArray array = EmaFactory.createOmmArray();
        
        for(String fieldName : fieldNames)
        {
            array.add(EmaFactory.createOmmArrayEntry().ascii(fieldName));
        }

        view.add(EmaFactory.createElementEntry().uintValue(EmaRdm.ENAME_VIEW_TYPE, 2));
        view.add(EmaFactory.createElementEntry().array(EmaRdm.ENAME_VIEW_DATA, array));
        
        return view;        
    }
    
    /**
     * Wait for the completion of this MarketPrice either by sleeping 
     * (autoDispatch==false) or by dispatching events (autoDispatch==false)
     */
    private void waitForCompletion() 
    {
        do
        {
            if(autoDispatch)
            {
                dispatcher.dispatchEventsUntilComplete(this);
            }
            else
            {
                try 
                {
                    Thread.sleep(WAITING_LOOP_TIMEOUT_IN_MS);
                } 
                catch (InterruptedException exception) {}
            }
        } 
        while (!isComplete());
    }

    
    /**
     * Returns the EMA DataDictionary used by the FieldList
     * @param fieldList the field list that refers to the dictionary
     * @return the EMA <code>DataDictionary</code>
     */
    private static DataDictionary getDictionaryFrom(FieldList fieldList) 
    {
        DictionaryUtility dictionaryUtility = EmaFactory.createDictionaryUtility();
        DataDictionary dataDictionary = dictionaryUtility.dataDictionary(fieldList);
        
        return dataDictionary;
    }       
    
    /**
     * Extract a collection of <code>Fields</code> from and EMA <code>FieldList</code>.
     * @param emaFieldList the EMA <code>FieldList</code>.
     * @return the collection of <code>Fields</code>.
     */
    private Collection<Field> extractFieldsFrom(FieldList emaFieldList) 
    {
        LinkedList<Field> fields = new LinkedList<>();
        DataDictionary dataDictionary = getDictionaryFrom(emaFieldList);
        
        for (FieldEntry emaFieldEntry : emaFieldList) 
        {
            int fieldId = emaFieldEntry.fieldId();
            Data fieldValue = emaFieldEntry.load();
            Field newField = null;
                               
            if(partialUpdatesManagementActivated)
            {
                // Retrieve the cached Field
                Field cachedField = fieldsById.get(fieldId);
                
                // If found, apply the partial update
                if(cachedField != null)
                {
                    newField = cachedField.applyPartialUpdate(fieldValue);
                }
            }
            
            // If partial updates management is not activated 
            //      or
            // If the field was not cached yet
            if(newField == null)
            {
                newField = 
                        new Field.Builder()
                                .withId(fieldId)
                                .withValue(fieldValue)
                                .withDataDictionary(dataDictionary)
                                .build();
            }
            
            fields.add(newField);
        }
        
        return fields;
    }
    

    /**
     * Apply a given image to the cached image of this <code>MarketPrice</code>.
     * @param image the image to apply
     */
    private void applyImage(Collection<Field> image) 
    {
        clearCachedImage();
        applyUpdate(image);
    }

    /**
     * Clear the cached image
     */
    private void clearCachedImage() 
    {
        fieldsById.clear();
        fieldsByName.clear();        
    }
    
    /**
     * Apply a given update to the cached image of this <code>MarketPrice</code>.
     * @param update the update to apply
     */   
    private void applyUpdate(Collection<Field> update) 
    {
        for(Field updatedField: update)
        {                        
            fieldsById.put(updatedField.description().fid(), updatedField);
            fieldsByName.put(updatedField.description().acronym(), updatedField);        
        }        
    }
    
}
