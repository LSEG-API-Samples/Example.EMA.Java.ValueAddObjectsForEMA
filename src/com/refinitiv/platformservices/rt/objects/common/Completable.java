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
package com.refinitiv.platformservices.rt.objects.common;

/**
 * Interface that should be implemented by any Elektron real-time object that 
 * can be completed. An object is said completed if it received its complete
 * data set from the platform or if it's in an error state.
 */
public interface Completable 
{
    /**
     * Returns the completion state of the object. 
     * <br>
     * An object is said completed if it received its complete data set from 
     * the platform or if it's in an error state.
     * <br>
     * For example, a <code>MarketPrice</code> is complete once it received its first 
     * image (Refresh message) from the platform or an error status. A <code>Chain</code> is 
     * complete once it received all the elements of that chain. An <code>OrderBook</code> 
     * is complete once it received the last part of it's image (Refresh message with 
     * the <code>Complete</code> indication set to <code>true</code>).
     * @return <code>true</code> if the object is complete. Returns <code>false</code> otherwise.
     */    
    boolean isComplete();      
}
