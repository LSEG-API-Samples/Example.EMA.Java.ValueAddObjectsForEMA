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
import com.thomsonreuters.ema.access.OmmEnum;

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