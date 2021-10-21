/*
 * Copyright 2021 Refinitiv
 *
 * DISCLAIMER: This source code has been written by Refinitiv for the only 
 * purpose of illustrating articles published on the Refinitiv Developer 
 * Community. It has not been tested for usage in production environments. 
 * Refinitiv cannot be held responsible for any issues that may happen if 
 * these objects or the related source code is used in production or any other 
 * client environment.
 *
 * Refinitiv Developer Community: https://developers.refinitiv.com
 *
 */
package com.refinitiv.platformservices.rt.objects.chain;

import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.platformservices.rt.objects.common.Dispatcher;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class RecursiveChainImpl implements RecursiveChain
{
    private static final int WAITING_LOOP_TIMEOUT_IN_MS = 200;
            
    private final OmmConsumer ommConsumer;
    private final String name;
    private final String serviceName;
    private final SummaryLinksToSkipByDisplayTemplate summaryLinksToSkipByDisplayTemplate;
    private final int nameGuessesCount;
    private final int maxDepth;
    private final boolean synchronousModeActivated;
    private final boolean autoDispatch;        
    private final OnElementAddedFunction onElementAddedFunction;
    private final OnCompleteFunction onCompleteFunction;
    private final OnErrorFunction onErrorFunction;
    private enum State {OPENING, OPENED, CLOSING, CLOSED, IN_ERROR};
    private State state;
    private final FlatChain currentDepthChain;
    private final Map<RecursiveChain, Long> positionsBySubChain = new HashMap<>();
    private final boolean maxDepthReached;
    private boolean isComplete;
    private static Dispatcher dispatcher; 

    public RecursiveChainImpl(RecursiveChain.Builder builder)
    {
        ommConsumer = builder.ommConsumer;
        name = builder.chainName;
        serviceName = builder.serviceName;
        summaryLinksToSkipByDisplayTemplate = builder.summaryLinksToSkipByDisplayTemplate;
        nameGuessesCount = builder.nameGuessesCount;
        maxDepth = builder.maxDepth;
        synchronousModeActivated = builder.synchronousModeActivated;
        autoDispatch = builder.autoDispatch;            
        onElementAddedFunction = builder.onElementAddedFunction;
        onCompleteFunction = builder.onCompleteFunction;
        onErrorFunction = builder.onErrorFunction;
        
        state = State.CLOSED;
        currentDepthChain = buildFlatChain(name);
        if(maxDepth == 0)
            maxDepthReached = true;
        else
            maxDepthReached = false;
        
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void open()
    {
        synchronized(this)
        {
            if(state == State.OPENED || state == State.OPENING || state == State.IN_ERROR)
                return;

            state = State.OPENING;

            if(maxDepthReached)
            {
                state = State.IN_ERROR;
                onErrorFunction.onError("MaxDepth reached, sub-chain <" + name + "> will not be opened.", this);
                onCompleteFunction.onComplete(this);
                return;
            }

            currentDepthChain.open();
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
        
        closeSubChains();
        currentDepthChain.close();        
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
    public boolean isAChain()
    {
        if(maxDepthReached)
        {
            return false;
        }
        
        return currentDepthChain.isAChain();
    }
    
    @Override
    public synchronized Map<List<Long>, List<String>> getElements()
    {
        Comparator<List<Long>> positionComparator = buildElementPositionComparator();
        Map<List<Long>, List<String>> allElementsByPosition = new TreeMap<>(positionComparator);
        
        for (Map.Entry<RecursiveChain, Long> entry : positionsBySubChain.entrySet())    
        {
            Long subChainPosition = entry.getValue();
            RecursiveChain subChain = entry.getKey();
            
            if(subChain.isAChain())
            {
                for (Map.Entry<List<Long>, List<String>> subChainElementEntry : subChain.getElements().entrySet())
                {
                    List<Long> elementPositionInSubChain = subChainElementEntry.getKey();
                    List<Long> elementPositionInChain = buildElementPositionInChain(elementPositionInSubChain, subChainPosition);
                    List<String> elementNameInSubChain = subChainElementEntry.getValue();
                    List<String> elementNameInChain = buildElementNameInChain(elementNameInSubChain, subChain);
                    allElementsByPosition.put(elementPositionInChain, elementNameInChain);
                }
            }
            else
            {
                List<Long> elementPositionInChain = buildElementPositionInChain(subChainPosition);
                List<String> elementNameSubChain = buildElementNameInChain(subChain);
                allElementsByPosition.put(elementPositionInChain, elementNameSubChain);
            }
        }
               
        return allElementsByPosition;
    }
        
    private Comparator<List<Long>> buildElementPositionComparator()
    {
        return new Comparator<List<Long>>() 
        {
            @Override public int compare(List<Long> key1, List<Long> key2) 
            {
                Iterator<Long> key1Iterator = key1.iterator();
                Iterator<Long> key2Iterator = key2.iterator();
                while(key1Iterator.hasNext() && key2Iterator.hasNext())
                {
                    long key1Value = key1Iterator.next();
                    long key2Value = key2Iterator.next();
                    
                    if(key1Value != key2Value)
                    {
                        return (int) (key1Value - key2Value);
                    }
                }
                
                return key1.size() - key1.size();
            }           
        };
    }
    
    private int computeNextMaxDepth()
    {
        int nextMaxDepth = -1;
        
        if(maxDepth > 0)
        {
            nextMaxDepth = maxDepth - 1;
        }
        
        return nextMaxDepth;
    }
    
    private FlatChain buildFlatChain(String chainName)
    {
        FlatChain newChain = new FlatChain.Builder()
                    .withOmmConsumer(ommConsumer)
                    .withChainName(chainName)
                    .withServiceName(serviceName)
                    .withUpdates(false)
                    .withSummaryLinksToSkip(summaryLinksToSkipByDisplayTemplate)
                    .withNameGuessingOptimization(nameGuessesCount)
                    .onElementAdded (
                            (position, name, chain) -> onLinkAdded(position, name, chain)
                    )
                    .onComplete(
                            (chain)-> onComplete(chain)
                    )
                    .onError(
                            (errorMessage, chain) -> onError(errorMessage, chain)
                    )
                    .build();      
        
        return newChain;
    }
    
    private RecursiveChain buildRecursiveChain(String chainName)
    {
        RecursiveChain subChain = new RecursiveChain.Builder()
                    .withOmmConsumer(ommConsumer)
                    .withChainName(chainName)
                    .withServiceName(serviceName)
                    .withSummaryLinksToSkip(summaryLinksToSkipByDisplayTemplate)
                    .withMaxDepth(computeNextMaxDepth())
                    .onElementAdded(
                            (position, name, chain) -> onSubLinkAdded(position, name, chain)
                    )
                    .onComplete(
                            (chain) -> onSubChainComplete(chain)
                    )
                    .onError(
                            (errorMessage, chain) -> onSubChainError(errorMessage, chain)
                    )
                    .build();

        return subChain;
    }
    
    private void openSubChain(String chainName, long position)
    {
        RecursiveChain subChain = buildRecursiveChain(chainName);
        positionsBySubChain.put(subChain, position);
        subChain.open();
    }

    private void closeSubChains()
    {
        Collection<RecursiveChain> subChains = positionsBySubChain.keySet();
        subChains.forEach((subChain) -> subChain.close());
        positionsBySubChain.clear();
    }
        
    private void onLinkAdded(long linkPosition, String linkName, FlatChain chain)
    {
        if(linkName.isEmpty())
        {
            onElementAddedFunction.onElementAdded(
                    buildElementPositionInChain(linkPosition),
                    buildElementNameInChain(linkName), 
                    this);            
        }
        else
        {
            openSubChain(linkName, linkPosition);        
        }
    }

    private void onComplete(FlatChain chain)
    {
        checkIfCompleteAndNotify();
    }
    
    private void onError(String errorMessage, FlatChain chain)
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        state = State.IN_ERROR;
        onErrorFunction.onError(errorMessage, this);
    }
    
    private void onSubLinkAdded(List<Long> linkPositionInSubChain, List<String> linkNameInSubChain, RecursiveChain subChain)
    {
        onElementAddedFunction.onElementAdded(
                buildElementPositionInChain(linkPositionInSubChain, subChain),
                buildElementNameInChain(linkNameInSubChain, subChain), 
                this);
    }
    
    private void onSubChainComplete(RecursiveChain subChain)
    {
        checkIfCompleteAndNotify();
    }

    private void onSubChainError(String errorMessage, RecursiveChain subChain)
    {
        if(state == State.CLOSED || state == State.CLOSING)
            return;

        if(!subChain.isAChain())
        {
            
            onElementAddedFunction.onElementAdded(
                    buildElementPositionInChain(subChain), 
                    buildElementNameInChain(subChain),
                    this);
        }
        else
        {
            onErrorFunction.onError(errorMessage, this);
        }
    }

    private void checkIfCompleteAndNotify()
    {
        if (state != State.OPENING && state != State.IN_ERROR)
        {
            return;
        }

        if(!currentDepthChain.isComplete())
        {
            return;
        }

        isComplete = true;
        Collection<RecursiveChain> subChains = positionsBySubChain.keySet();
        subChains.forEach(
                (subChain) -> {
                    if(!subChain.isComplete())
                    {
                        isComplete = false;
                    }
                    else
                    {
                    }
                }
        );
        
        if(isComplete)
        {
            state = State.OPENED;
            onCompleteFunction.onComplete(this);
        }
        else
        {
        }
    }    
    
    private List<Long> buildElementPositionInChain(List<Long> elementPositionInSubChain, RecursiveChain subChain)
    {
        LinkedList<Long> elementPositionInChain = new LinkedList<>(elementPositionInSubChain);
        long subChainPosition = positionsBySubChain.get(subChain);
        elementPositionInChain.addFirst(subChainPosition);
        return elementPositionInChain;
    }    

    private List<Long> buildElementPositionInChain(List<Long> elementPositionInSubChain, long subChainPosition)
    {
        LinkedList<Long> elementPositionInChain = new LinkedList<>(elementPositionInSubChain);
        elementPositionInChain.addFirst(subChainPosition);        
        return elementPositionInChain;
    }    
    
    private List<Long> buildElementPositionInChain(RecursiveChain subChain)
    {
        LinkedList<Long> elementPositionInChain = new LinkedList<>();
        long elementPosition = positionsBySubChain.get(subChain);
        elementPositionInChain.add(elementPosition);
        return elementPositionInChain;
    }    
    
    private List<Long> buildElementPositionInChain(long position)
    {
        LinkedList<Long> elementPositionInChain = new LinkedList<>();
        elementPositionInChain.add(position);
        return elementPositionInChain;
    }    
    
    private List<String> buildElementNameInChain(String elementName)
    {
        LinkedList<String> elementNameInChain = new LinkedList<>();
        elementNameInChain.add(this.name);
        elementNameInChain.add(elementName);
        return elementNameInChain;
        
    }

    private List<String> buildElementNameInChain(List<String> elementNameInSubChain, RecursiveChain subChain)
    {
        LinkedList<String> elementNameInChain = new LinkedList<>(elementNameInSubChain);        
        elementNameInChain.addFirst(subChain.getName());
        return elementNameInChain;
        
    }

    private List<String> buildElementNameInChain(RecursiveChain subChain)
    {
        LinkedList<String> elementNameInChain = new LinkedList<>();
        elementNameInChain.add(subChain.getName());
        return elementNameInChain;        
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
