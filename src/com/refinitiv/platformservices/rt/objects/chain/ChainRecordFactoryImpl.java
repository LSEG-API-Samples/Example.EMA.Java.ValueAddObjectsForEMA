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
package com.refinitiv.platformservices.rt.objects.chain;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ChainRecordFactoryImpl implements ChainRecordFactory, ChainRecordListener
{

    private final ChainRecordContext context;
    private final ChainRecordListener listener;
    private final Map<String, ChainRecord> preOpenedChainRecordByName = new HashMap<>();
    private boolean useGuessedNamesOptimization = false;
    private static final String chainNamePattern = "^[0-9A-F]+#";

    ChainRecordFactoryImpl(
            ChainRecordContext context,
            ChainRecordListener listener)
    {

        this.context = context;
        this.listener = listener;
        
        if(context.getNameGuessesCount() > 0)
        {
            useGuessedNamesOptimization = true;
        }
    }

    @Override
    public synchronized ChainRecord acquire(String name)
    {
        ChainRecord chainRecord=null;
        
        if(useGuessedNamesOptimization)
        {
            if(isPreOpened(name))
            {
                chainRecord = preOpenedChainRecordByName.remove(name);
                chainRecord.setListener(listener);
                return chainRecord;
            }
            guessAndPreOpenNextChainRecordsFor(name);
        }
        
        ChainRecordFactory chainRecordFactory = this;
        chainRecord = new ChainRecord(
                name,
                chainRecordFactory,
                context,
                listener);

        return chainRecord;
    }
    
    @Override
    public synchronized void releaseAll()
    {
        preOpenedChainRecordByName.values().forEach(
            (chainRecord) -> chainRecord.close()
        );
        
        preOpenedChainRecordByName.clear();
    }
    
    private boolean isPreOpened(String name)
    {
        return preOpenedChainRecordByName.containsKey(name);
    }
    
    private void guessAndPreOpenNextChainRecordsFor(String name)
    {   
        String strId = "0";
        String rootName = name;
        
        Pattern regExPattern = Pattern.compile(chainNamePattern);
        Matcher RegExMatcher = regExPattern.matcher(name);    
        
        if(RegExMatcher.find())
        {
            strId = name.substring(RegExMatcher.start(), RegExMatcher.end()-1);
            rootName = name.substring(RegExMatcher.end());
        }
                
        int id = Integer.parseUnsignedInt(strId, 16);
        int maxId = id + context.getNameGuessesCount();
        for(int nextId = id+1; nextId <= maxId; ++nextId)
        {
            String nextStrId = Integer.toString(nextId, 16);
            nextStrId = nextStrId.toUpperCase();
            String nextName = nextStrId + "#" + rootName;
            
            if(!isPreOpened(nextName))
            {
                ChainRecordFactory chainRecordFactory = this;
                ChainRecordListener listener = this;
                ChainRecord chainRecord = new ChainRecord(
                        nextName,
                        chainRecordFactory,
                        context,
                        listener);
                chainRecord.preOpen();
                preOpenedChainRecordByName.put(nextName, chainRecord);
            }
        }
    }

    @Override
    public synchronized void onLinkAdded(long position, String name, ChainRecord chainRecord)
    {}

    @Override
    public synchronized void onLinkRemoved(long position, ChainRecord chainRecord)
    {}

    @Override
    public synchronized void onLinkChanged(long position, String previousName, String newName, ChainRecord chainRecord)
    {}

    @Override
    public synchronized void onCompleted(ChainRecord chainRecord)
    {
        closeAndRemoveRemainingPreOpenedChainRecords();
    }

    private void closeAndRemoveRemainingPreOpenedChainRecords()
    {
        preOpenedChainRecordByName.forEach((String name, ChainRecord chainRecord) -> chainRecord.close());
        preOpenedChainRecordByName.clear();
    }

    @Override
    public synchronized void onError(String errorMessage, ChainRecord chainRecord)
    {
        closeAndRemovedInvalidPreOpenedChainRecord(chainRecord);
    }
    
    private void closeAndRemovedInvalidPreOpenedChainRecord(ChainRecord chainRecord)
    {
        chainRecord.close();
        preOpenedChainRecordByName.remove(chainRecord.getName());
    }
}
