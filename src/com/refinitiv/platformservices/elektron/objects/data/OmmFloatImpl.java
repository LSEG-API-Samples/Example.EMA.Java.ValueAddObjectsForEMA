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
package com.refinitiv.platformservices.elektron.objects.data;

import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.OmmFloat;

/**
 * 
 * In-memory implementation of the <code>OmmFloat</code> interface.
 */
class OmmFloatImpl extends DataImpl implements OmmFloat
{
    private final float value;
	
   /**
     * Copy the given <code>OmmFloat</code>
     * @param ommFloat the <code>OmmFloat</code> to copy.
     */
    public OmmFloatImpl(OmmFloat ommFloat)
    {
        super(ommFloat);
        this.value = ommFloat.floatValue();
    }
    
    @Override
    public int dataType()
    {
        return DataType.DataTypes.FLOAT;
    }

    @Override
    public float floatValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        if (DataCode.BLANK == code())
            return BLANK_STRING;
        else
            return Float.toString(value);
    }	
}