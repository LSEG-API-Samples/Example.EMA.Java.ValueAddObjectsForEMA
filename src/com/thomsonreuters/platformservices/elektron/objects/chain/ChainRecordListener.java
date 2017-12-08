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

interface ChainRecordListener
{

    void onLinkAdded(long position, String name, ChainRecord chainRecord);

    void onLinkRemoved(long position, ChainRecord chainRecord);

    void onLinkChanged(long position, String previousName, String newName, ChainRecord chainRecord);

    void onCompleted(ChainRecord chainRecord);

    void onError(String errorMessage, ChainRecord chainRecord);
}
