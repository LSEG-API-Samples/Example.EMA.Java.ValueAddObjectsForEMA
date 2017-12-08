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
import com.thomsonreuters.ema.access.OmmUInt;
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