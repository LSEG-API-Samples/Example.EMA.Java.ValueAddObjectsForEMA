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
import com.refinitiv.ema.access.OmmEnum;

/**
 * 
 * In-memory implementation of the <code>OmmEnum</code> interface.
 */
class OmmEnumImpl extends DataImpl implements OmmEnum
{
    private final int value;
    private final String valueAsString;
    
    /**
     * Copy the given <code>OmmEnum</code>
     * @param ommEnum the <code>OmmEnum</code> to copy.
     */
    public OmmEnumImpl(OmmEnum ommEnum)
    {
        super(ommEnum);
        this.value = ommEnum.enumValue();
        this.valueAsString = ommEnum.toString();        
    }
    
    public OmmEnumImpl(OmmEnum ommEnum, String valueAsString)
    {
        super(ommEnum);
        this.value = ommEnum.enumValue();
        this.valueAsString = valueAsString;
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.ENUM;
    }

    @Override
    public int enumValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return valueAsString;
    }
}