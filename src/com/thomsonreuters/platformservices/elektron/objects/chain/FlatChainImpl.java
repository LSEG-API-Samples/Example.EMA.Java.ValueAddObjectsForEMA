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
package com.thomsonreuters.platformservices.elektron.objects.chain;

import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.platformservices.elektron.objects.common.Dispatcher;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

class FlatChainImpl implements FlatChain, ChainRecordContext, ChainRecordListener
{
    private static final int WAITING_LOOP_TIMEOUT_IN_MS = 200;
            
    private final OmmConsumer ommConsumer;
    private final String name;
    private final String serviceName;
    private final boolean withUpdates;
    private final SummaryLinksToSkipByDisplayTemplate summaryLinksToSkipByDisplayTemplate;
    private final int nameGuessesCount;
    private final boolean synchronousModeActivated;
    private final boolean autoDispatch;    
    private final OnElementAddedFunction onElementAddedFunction;
    private final OnElementChangedFunction onElementChangedFunction;
    private final OnElementRemovedFunction onElementRemovedFunction;
    private final OnCompleteFunction onCompleteFunction;
    private final OnErrorFunction onErrorFunction;    
    private final ChainRecordFactory chainRecordFactory;
    private ChainRecord firstChainRecord;
    private enum State {OPENING, OPENED, CLOSING, CLOSED, IN_ERROR};
    private State state;
    private final Map<Long, String> elementsByPosition = new TreeMap<>();
    private static Dispatcher dispatcher;    
    
    public FlatChainImpl(FlatChain.Builder builder)
    {
        ommConsumer = builder.ommConsumer;
        name = builder.chainName;
        serviceName = builder.serviceName;
        withUpdates = builder.withUpdates;
        summaryLinksToSkipByDisplayTemplate = builder.summaryLinksToSkipByDisplayTemplate;
        nameGuessesCount = builder.nameGuessesCount;
        synchronousModeActivated = builder.synchronousModeActivated;
        autoDispatch = builder.autoDispatch;    
        onElementAddedFunction = builder.onElementAddedFunction;
        onElementChangedFunction = builder.onElementChangedFunction;
        onElementRemovedFunction = builder.onElementRemovedFunction;
        onCompleteFunction = builder.onCompleteFunction;
        onErrorFunction = builder.onErrorFunction;

        ChainRecordContext chainRecordContext = this;
        ChainRecordListener chainRecordListener = this;
        chainRecordFactory = new ChainRecordFactoryImpl(chainRecordContext, chainRecordListener);         
        
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
            if(state == State.OPENED || state == State.OPENING || state == State.IN_ERROR)
                return;

            state = State.OPENING;

            firstChainRecord = chainRecordFactory.acquire(getName());
            firstChainRecord.open();
        }
        
        if(synchronousModeActivated)
        {
            waitForCompletion();
        }        
    }

    @Override
    public synchronized void close()
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        state = State.CLOSING;
        elementsByPosition.clear();
        
        firstChainRecord.close();
        firstChainRecord = null;
        
        chainRecordFactory.releaseAll();
    }
    
    @Override
    public synchronized boolean isComplete()
    {
        if(state == State.OPENED || state == State.IN_ERROR)
        {
            return true;
        }
        
        return false;
    }
    
    @Override
    public synchronized boolean isAChain()
    {
        if (firstChainRecord == null)
        {
            return false;
        }
        
        return firstChainRecord.isAChainRecord();
    }
    
    @Override
    public synchronized Map<Long, String> getElements()
    {
        Map<Long, String> deepCopiedElementsToReturn = Collections.unmodifiableMap(elementsByPosition);
        
        return deepCopiedElementsToReturn;
    }
        
    @Override
    public OmmConsumer getOmmConsumer()
    {
        return ommConsumer;
    }

    @Override
    public boolean getWithUpdates()
    {
        return withUpdates;
    }

    @Override
    public SummaryLinksToSkipByDisplayTemplate getSummaryLinksToSkipByDisplayTemplate()
    {
        return summaryLinksToSkipByDisplayTemplate;
    }

    @Override
    public int getNameGuessesCount()
    {
        return nameGuessesCount;
    }
    
    @Override
    public OnElementAddedFunction getOnElementAddedFunction()
    {
        return onElementAddedFunction;
    }

    @Override
    public OnElementChangedFunction getOnElementChangedFunction()
    {
        return onElementChangedFunction;
    }

    @Override
    public OnElementRemovedFunction getOnElementRemovedFunction()
    {
        return onElementRemovedFunction;
    }

    @Override
    public OnCompleteFunction getOnCompleteFunction()
    {
        return onCompleteFunction;
    }

    @Override
    public OnErrorFunction getOnErrorFunction()
    {
        return onErrorFunction;
    }    

    @Override
    public synchronized void onLinkAdded(long position, String name, ChainRecord chainRecord)
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        elementsByPosition.put(position, name);
        onElementAddedFunction.onElementAdded(position, name, this);
    }

    @Override
    public synchronized void onLinkRemoved(long position, ChainRecord chainRecord)
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        elementsByPosition.remove(position);
        onElementRemovedFunction.onElementRemoved(position, this);
    }

    @Override
    public synchronized void onLinkChanged(long position, String previousName, String newName, ChainRecord chainRecord)
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        elementsByPosition.replace(position, newName);
        onElementChangedFunction.onElementChanged(position, previousName, newName, this);
    }

    @Override
    public synchronized void onCompleted(ChainRecord chainRecord)
    {
        if(state == State.OPENED || state == State.CLOSED || state == State.CLOSING)
            return;

        state = State.OPENED;
        onCompleteFunction.onComplete(this);
    }

    @Override
    public synchronized void onError(String errorMessage, ChainRecord chainRecord)
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        State previousState = state;
        state = State.IN_ERROR;
        
        onErrorFunction.onError(errorMessage, this);

        if(previousState == State.OPENING)
        {
            onCompleteFunction.onComplete(this);
        }
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
}
