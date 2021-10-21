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

import com.refinitiv.platformservices.rt.objects.common.Completable;

/**
 * The root interface of chains. A <code>Chain</code> represents a specific type 
 * of <code>MarketPrice</code> instruments that contains other instruments names. 
 * From a pure semantic point of view a chain can be considered as a list of 
 * instruments names. A chain is actually made of a chained list of <code>Chain 
 * Records</code> that are <code>MarketPrice</code> instruments that contain 
 * names of other instruments. <code>Chain Records</code> also contain additional
 * fields that link them together (More details about chains data structure are
 * available in <a href="https://developers.refinitiv.com/en/article-catalog/article/simple-chain-objects-ema-part-1" target="_blank">this article</a> 
 * available on the  <a href="https://developers.refinitiv.com" 
 * target="_blank">Refinitiv Developer Portal</a> ).
 * A <code>Chain</code> is an object that subscribes to the underlying 
 * linked list of <code>Chain Records</code> of a given chain and that retrieves 
 * the contained instruments names for you. 
 */
public interface Chain extends Completable
{
    /**
     * Returns the name of this <code>Chain</code>. This name is used by the 
     * chain to subscribe to its first <code>Chain Record</code>.
     * @return the name of this <code>Chain</code>.
     */
    String getName();
    
    /**
     * Returns the name of the service this <code>Chain</code> is published on. 
     * This service name is used by the chain to subscribe to the underlying 
     * <code>Chain Record</code>.
     * @return the service name used by this <code>Chain</code>.
     */
    String getServiceName();
    
    /**
     * Opens this <code>Chain</code>. Opening a chain starts the subscription 
     * and the decoding of its underlying <code>Chain Record</code>. Once opened, 
     * the chain starts invoking the functional interfaces specified at built 
     * time. This method has no effect if the chain is already opened. 
     */
    void open();

    /**
     * Closes this <code>Chain</code> and unsubscribes to the underlying <code>
     * Chain Record</code>. Once closed the chain stops invoking the functional
     * interfaces that have been specified at built time. This method has
     * no effect if the chain is already closed. 
     */
    void close();

    /**
     * Indicates if the name you used to build this <code>Chain</code> corresponds
     * to an actual chain published on the Elektron or Refinitiv Enterprise 
     * Platform (RTDS) infrastructure. In order to determine if it's the case 
     * the chain must be opened so that it subscribed and decoded it's first 
     * <code>Chain Record</code>. If the chain is not opened, the method 
     * returns <code>false</code>.
     * @return true if the name used to build this chain corresponds to an 
     * instrument that contains all the fields a Chain Record should have. 
     * Returns false otherwise or if the chain is not opened.
     */
    boolean isAChain();    
}
