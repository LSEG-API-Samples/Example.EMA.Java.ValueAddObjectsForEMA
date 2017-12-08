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

import com.thomsonreuters.platformservices.elektron.objects.common.Completable;

/**
 * The root interface of chains. A <code>Chain</code> represents a specific type 
 * of <code>MarketPrice</code> instruments that contains other instruments names. 
 * From a pure semantic point of view a chain can be considered as a list of 
 * instruments names. A chain is actually made of a chained list of <code>Chain 
 * Records</code> that are <code>MarketPrice</code> instruments that contain 
 * names of other instruments. <code>Chain Records</code> also contain additional
 * fields that link them together (More details about chains data structure are
 * available in <a href="https://developers.thomsonreuters.com/article/elektron-article-1" target="_blank">this article</a> 
 * available on the  <a href="https://developers.thomsonreuters.com" 
 * target="_blank">Thomson Reuters Developer Portal</a> ).
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
     * to an actual chain published on the Elektron or Thomson Reuters Enterprise 
     * Platform (TREP) infrastructure. In order to determine if it's the case 
     * the chain must be opened so that it subscribed and decoded it's first 
     * <code>Chain Record</code>. If the chain is not opened, the method 
     * returns <code>false</code>.
     * @return true if the name used to build this chain corresponds to an 
     * instrument that contains all the fields a Chain Record should have. 
     * Returns false otherwise or if the chain is not opened.
     */
    boolean isAChain();    
}
