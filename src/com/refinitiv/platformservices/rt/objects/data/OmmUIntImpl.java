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
import com.refinitiv.ema.access.OmmUInt;
import java.math.BigInteger;

/**
 * 
 * In-memory implementation of the <code>OmmUInt</code> interface.
 */
class OmmUIntImpl extends DataImpl implements OmmUInt
{
    private final BigInteger value;
    private final String valueAsString;
	
    /**
     * Copy the given <code>OmmUInt</code>
     * @param ommUInt the <code>OmmUInt</code> to copy.
     */
    public OmmUIntImpl(OmmUInt ommUInt)
    {
        super(ommUInt);
        this.value = ommUInt.bigIntegerValue();
        this.valueAsString = ommUInt.toString();
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.UINT;
    }

    @Override
    public long longValue()
    {
        return value.longValue();
    }

    @Override
    public BigInteger bigIntegerValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return valueAsString;
    }

}