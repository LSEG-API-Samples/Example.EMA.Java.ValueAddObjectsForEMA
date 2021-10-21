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
package com.refinitiv.platformservices.rt.objects.data;

import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.OmmDouble;

/**
 * 
 * In-memory implementation of the <code>OmmDouble</code> interface.
 */
class OmmDoubleImpl extends DataImpl implements OmmDouble
{
    private final double value;

    /**
     * Copy the given <code>OmmDouble</code>
     * @param ommDouble the <code>OmmDouble</code> to copy.
     */    
    public OmmDoubleImpl(OmmDouble ommDouble)
    {
        super(ommDouble);
        this.value = ommDouble.doubleValue();
    }
            
    @Override
    public int dataType()
    {
        return DataType.DataTypes.DOUBLE;
    }

    @Override
    public double doubleValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        if (DataCode.BLANK == code())
            return BLANK_STRING;
        else
            return Double.toHexString(value);
    }
}