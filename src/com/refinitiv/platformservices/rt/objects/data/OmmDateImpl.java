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
import com.refinitiv.ema.access.OmmDate;

/**
 * 
 * In-memory implementation of the <code>OmmDate</code> interface.
 */
class OmmDateImpl extends DataImpl implements OmmDate
{
    private final int year;
    private final int month;
    private final int day;
    private final String valueAsString;
		
    /**
     * Copy the given <code>OmmDate</code>
     * @param ommDate the <code>OmmDate</code> to copy.
     */
    public OmmDateImpl(OmmDate ommDate)
    {
        super(ommDate);
        this.year = ommDate.year();
        this.month = ommDate.month();
        this.day = ommDate.day();
        this.valueAsString = ommDate.toString();
    }
    
    @Override
    public int dataType()
    {
        return DataType.DataTypes.DATETIME;
    }

    @Override
    public int year()
    {
        return year;
    }

    @Override
    public int month()
    {
        return month;
    }

    @Override
    public int day()
    {
        return day;
    }

    @Override
    public String toString()
    {
        return valueAsString;
    }
}