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
package com.refinitiv.platformservices.rt.objects.common;

import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.ema.access.OmmException;

/**
 * The interface of a <code>Dispatcher</code> implementation that you can use
 * to dispatch EMA dispatchable objects like <code>OmmConsumer</code>s.  
 * <code>Dispatcher</code>s provide a number of helper methods that facilitates 
 * EMA objects dispatching for common use cases.
 * <br>
 * <br>
 * <strong>Note:</strong> <code>Dispatcher</code>s must only be used with 
 * <code>OmmConsumer</code>s that have been properly initialized and configured
 * with the USER_DISPATCH operational model.  
 */
public interface Dispatcher 
{
    /**
     * Duration of 2 seconds that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int TWO_SECONDS = 2;

    /**
     * Duration of 5 seconds that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int FIVE_SECONDS = 5;

    /**
     * Duration of 10 seconds that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int TEN_SECONDS = 10;

    /**
     * Duration of 15 seconds that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int FIFTEEN_SECONDS = 15;

    /**
     * Duration of 20 seconds that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int TWENTY_SECONDS = 20;

    /**
     * Duration of 30 seconds that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int THIRTY_SECONDS = 30;

    /**
     * Duration of 1 minute that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int ONE_MINUTE = 60;

    /**
     * Duration of 2 minutes that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int TWO_MINUTES = 120;

    /**
     * Duration of 5 minutes that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int FIVE_MINUTE = 300;

    /**
     * Duration of 10 minutes that can be used as a parameter of {@link #dispatchEventsUntilTimeElapsed(int)}. 
     */
    public static final int TEN_MINUTE = 600;
    
    /**
     * Dispatch events until the object given in parameter is complete. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This call is blocking until the object is complete.
     * @param completable the object that must be completed.
     */
    public void dispatchEventsUntilComplete(Completable completable) throws OmmException;
    
    /**
     * Dispatch events until the duration given in parameter is expired. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This call is blocking until the indicated duration expires.
     * @param durationInSeconds the duration in seconds.
     */
    public void dispatchEventsUntilTimeElapsed(int durationInSeconds) throws OmmException;
    
    /**
     * Dispatch events until the user presses &lt;Enter&gt; in the console. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This call is blocking until the user presses &lt;Enter&gt;.
     */
    public void dispatchEventsUntilKeyPressed() throws OmmException;
    
    /**
     * Dispatch events forever. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This method never returns.
     */
    public void dispatchEventsForever() throws OmmException;
    
    
    /**
     * Used to build <code>Dispatcher</code> objects. <code>Dispatcher</code>s are
     * immutable objects. This means that you can't directly change their fields 
     * once they are built (there is no setter).
     * <br>
     * The following code snippet builds a <code>Dispatcher</code> and dispatches
     * events of an <code>OmmConsumer</code> for 30 seconds.
     * <br>
     * <br>
     * <pre>
     *    OmmConsumer ommConsumer = ...;
     *      .
     *      .
     *      .
     *    Dispatcher theDispatcher = new Dispatcher.Builder()
     *                  .withOmmConsumer(ommConsumer)
     *                  .build();
     * 
     *    theDispatcher.dispatchEventsUntilTimeElapsed(Dispatcher.THIRTY_SECONDS);
     *      .
     *      .
     *      .
     * </pre>
     * <br>
     * <br>
     * <strong>Note:</strong> The OmmConsumer is a mandatory parameter that must 
     * be set before {@link #build()} is called.
     */    
    public static class Builder {

        OmmConsumer ommConsumer;
        
        /**
         * Default constructor
         */
        public Builder() 
        {            
        }

        /**
         * Sets the EMA OmmConsumer used by this dispatcher. 
         * This OmmConsumer must have been properly initialized and configured
         * with the USER_DISPATCH operational model.
         * @param ommConsumer the OmmConsumer that will be dispatch.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withOmmConsumer(OmmConsumer ommConsumer) 
        {
            this.ommConsumer = ommConsumer;
            return this;
        }
        
        
        /**
         * Builds the <code>Dispatcher</code> object
         * @return the built Dispatcher
         */
        public Dispatcher build() 
        {
            if (ommConsumer == null)
            {
                throw new IllegalStateException("The MarketPrice.Builder cannot build a MarketPrice without an OmmConsumer.");
            }

            DispatcherImpl builtDispatcher = new DispatcherImpl(this);

            return builtDispatcher;
        }        
    }    
}
