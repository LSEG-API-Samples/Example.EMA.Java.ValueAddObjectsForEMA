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
import com.refinitiv.platformservices.rt.objects.chain.FlatChain.OnCompleteFunction;
import com.refinitiv.platformservices.rt.objects.chain.FlatChain.OnErrorFunction;
import com.refinitiv.platformservices.rt.objects.chain.FlatChain.OnElementAddedFunction;
import com.refinitiv.platformservices.rt.objects.chain.FlatChain.OnElementChangedFunction;
import com.refinitiv.platformservices.rt.objects.chain.FlatChain.OnElementRemovedFunction;

interface ChainRecordContext
{
    OmmConsumer getOmmConsumer();
            
    String getName();
    
    String getServiceName();
    
    boolean getWithUpdates();
    
    SummaryLinksToSkipByDisplayTemplate getSummaryLinksToSkipByDisplayTemplate();
    
    int getNameGuessesCount();

    OnElementAddedFunction getOnElementAddedFunction();
    
    OnElementChangedFunction getOnElementChangedFunction();
    
    OnElementRemovedFunction getOnElementRemovedFunction();
    
    OnCompleteFunction getOnCompleteFunction();
    
    OnErrorFunction getOnErrorFunction();
    
}
