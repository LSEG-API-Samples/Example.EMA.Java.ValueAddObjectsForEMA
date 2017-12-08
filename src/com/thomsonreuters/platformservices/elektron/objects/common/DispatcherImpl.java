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
package com.thomsonreuters.platformservices.elektron.objects.common;

import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmException;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The implementation class of the <code>Dispatcher</code> interface.
 */
class DispatcherImpl implements Dispatcher
{
    // Value of the timeout used by the event dispatching loops
    private static final int DISPATCH_TIMEOUT_IN_MS = 200;
 
    // The OmmConsumer to dispatch
    private final OmmConsumer ommConsumer;
    
    /**
     * Constructor used by the <code>Dispatcher.Builder</code> to build a new 
     * <code>Dispatcher</code> object. 
     * @param builder the <code>Builder</code> that contains all the parameters
     * required to build the class. This is this builder that instantiated this 
     * object by calling this constructor.
     */    
    DispatcherImpl(Dispatcher.Builder builder) 
    {
        ommConsumer = builder.ommConsumer;
    }    
    
    @Override
    public void dispatchEventsUntilComplete(Completable completable) throws OmmException
    {
        do
        {
            ommConsumer.dispatch(DISPATCH_TIMEOUT_IN_MS);
        } 
        while (!completable.isComplete());   
    }

    @Override
    public void dispatchEventsUntilTimeElapsed(int durationInSeconds) throws OmmException
    {          
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(durationInSeconds);
        do
        {
            ommConsumer.dispatch(DISPATCH_TIMEOUT_IN_MS);
        } 
        while(LocalDateTime.now().isBefore(endTime));                  
    }

    @Override
    public void dispatchEventsUntilKeyPressed() throws OmmException
    {
        try
        {        
            // Dispatch events until characters are available
            do
            {
                ommConsumer.dispatch(DISPATCH_TIMEOUT_IN_MS);
            } 
            while (System.in.available() <= 0);

            // Read and discard all available characters
            do
            {
                System.in.read();
            }
            while (System.in.available() > 0);
        }  
        catch (IOException exception)  
        {} 
    }
    
    
    @Override
    public void dispatchEventsForever() throws OmmException
    {
        do
        {
            ommConsumer.dispatch(DISPATCH_TIMEOUT_IN_MS);
        } 
        while (true);    
    }
}
