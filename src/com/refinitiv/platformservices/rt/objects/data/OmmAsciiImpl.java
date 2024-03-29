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
import com.refinitiv.ema.access.OmmAscii;

/**
 * 
 * In-memory implementation of the <code>OmmAscii</code> interface.
 */
class OmmAsciiImpl extends DataImpl implements OmmAscii
{
    private final String value;
    
    /**
     * Copy the given <code>OmmAscii</code>
     * @param ommAscii the <code>OmmAscii</code> to copy.
     */
    OmmAsciiImpl(OmmAscii ommAscii)
    {
        super(ommAscii);
        this.value = ommAscii.ascii();
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.ASCII;
    }

    @Override
    public String ascii()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}