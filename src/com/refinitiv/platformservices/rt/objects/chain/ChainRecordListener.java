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

interface ChainRecordListener
{

    void onLinkAdded(long position, String name, ChainRecord chainRecord);

    void onLinkRemoved(long position, ChainRecord chainRecord);

    void onLinkChanged(long position, String previousName, String newName, ChainRecord chainRecord);

    void onCompleted(ChainRecord chainRecord);

    void onError(String errorMessage, ChainRecord chainRecord);
}
