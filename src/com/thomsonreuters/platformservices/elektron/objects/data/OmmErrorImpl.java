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
package com.thomsonreuters.platformservices.elektron.objects.data;

import com.thomsonreuters.ema.access.DataType;
import com.thomsonreuters.ema.access.OmmError;

/**
 * 
 * In-memory implementation of the <code>OmmError</code> interface.
 */
class OmmErrorImpl extends DataImpl implements OmmError
{
    private final int errorCode;
    private final String errorCodeAsString;

   /**
     * Creates a new <code>OmmError</code>
     * @param errorCode the error code.
     * @param errorCodeAsString the String representation of the error.
     */
    public OmmErrorImpl(int errorCode, String errorCodeAsString)
    {
        this.errorCode = errorCode;
        this.errorCodeAsString = errorCodeAsString;
    }
            
   /**
     * Copy the given <code>OmmError</code>
     * @param ommError the <code>OmmError</code> to copy.
     */
    public OmmErrorImpl(OmmError ommError)
    {
        this.errorCode = ommError.errorCode();
        this.errorCodeAsString = ommError.toString();
    }
            
    @Override
    public int dataType()
    {
        return DataType.DataTypes.ERROR;
    }

    @Override
    public String errorCodeAsString() 
    {
        return errorCodeAsString;
    }

    @Override
    public int errorCode() 
    {
        return errorCode;
    }
    
    @Override
    public String toString()
    {
        return errorCodeAsString();
    }
    
}