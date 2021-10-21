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

import com.refinitiv.ema.access.Data;
import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.OmmRmtes;
import com.refinitiv.ema.access.RmtesBuffer;

/**
 * 
 * In-memory implementation of the <code>OmmRmtes</code> interface.
 */
class OmmRmtesImpl extends DataImpl implements OmmRmtes
{
    private final RmtesBuffer rmtesBuffer;

   /**
     * Copy the given <code>OmmRmtes</code>
     * @param ommRmtes the <code>OmmRmtes</code> to copy.
     */
    public OmmRmtesImpl(OmmRmtes ommRmtes)
    {
        super(ommRmtes);
        
        if(code() == Data.DataCode.BLANK)
        {
            this.rmtesBuffer = null;
        }
        else
        {
            this.rmtesBuffer = EmaFactory.createRmtesBuffer().apply(ommRmtes);
        }
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.RMTES;
    }

    @Override
    public String toString()
    {
        if (code() == DataCode.BLANK)
                return BLANK_STRING;
        else
                return rmtesBuffer.toString();
    }

    @Override
    public RmtesBuffer rmtes()
    {
        return rmtesBuffer;
    }
}