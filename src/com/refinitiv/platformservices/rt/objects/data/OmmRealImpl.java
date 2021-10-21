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
import com.refinitiv.ema.access.OmmReal;

/**
 * 
 * In-memory implementation of the <code>OmmReal</code> interface.
 */
class OmmRealImpl extends DataImpl implements OmmReal
{
    private final int magnitudeType;
    private final String magnitudeTypeAsString;
    private final long mantissa;
    private final double valueAsDouble;
    private final String valueAsString;
    
   /**
     * Copy the given <code>OmmReal</code>
     * @param ommReal the <code>OmmReal</code> to copy.
     */
    public OmmRealImpl(OmmReal ommReal)
    {
        super(ommReal);
        this.magnitudeType = ommReal.magnitudeType();
        this.magnitudeTypeAsString = ommReal.magnitudeTypeAsString();
        this.mantissa = ommReal.mantissa();
        this.valueAsDouble = ommReal.asDouble();
        this.valueAsString = ommReal.toString();
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.REAL;
    }

    @Override
    public String magnitudeTypeAsString()
    {
        return magnitudeTypeAsString;
    }

    @Override
    public long mantissa()
    {
        return mantissa;
    }

    @Override
    public int magnitudeType()
    {
        return magnitudeType;
    }

    @Override
    public double asDouble()
    {
        return valueAsDouble;
    }

    @Override
    public String toString()
    {
        return valueAsString;
    }
}