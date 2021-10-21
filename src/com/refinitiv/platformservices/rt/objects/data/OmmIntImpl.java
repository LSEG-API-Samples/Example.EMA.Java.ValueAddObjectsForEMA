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
import com.refinitiv.ema.access.OmmInt;

/**
 * 
 * In-memory implementation of the <code>OmmInt</code> interface.
 */
class OmmIntImpl extends DataImpl implements OmmInt
{
    private final long value;

   /**
     * Copy the given <code>OmmInt</code>
     * @param ommInt the <code>OmmInt</code> to copy.
     */
    public OmmIntImpl(OmmInt ommInt)
    {
        super(ommInt);
        value = ommInt.intValue();
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.INT;
    }

    @Override
    public long intValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        if (DataCode.BLANK == code())
                return BLANK_STRING;
        else
                return Long.toString(value);
    }
}