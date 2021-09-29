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
package com.refinitiv.platformservices.elektron.objects.data;

import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.OmmDateTime;

/**
 * 
 * In-memory implementation of the <code>OmmDateTime</code> interface.
 */
class OmmDateTimeImpl extends DataImpl implements OmmDateTime
{
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;
    private final int millisecond;
    private final int microsecond;
    private final int nanosecond;
    private final String valueAsString;
		
    /**
     * Copy the given <code>OmmDateTime</code>
     * @param ommDateTime the <code>OmmDateTime</code> to copy.
     */    
    public OmmDateTimeImpl(OmmDateTime ommDateTime)
    {
        super(ommDateTime);
        this.year = ommDateTime.year();
        this.month = ommDateTime.month();
        this.day = ommDateTime.day();
        this.hour = ommDateTime.hour();
        this.minute = ommDateTime.minute();
        this.second = ommDateTime.second();
        this.millisecond = ommDateTime.millisecond();
        this.microsecond = ommDateTime.microsecond();
        this.nanosecond = ommDateTime.nanosecond();
        this.valueAsString = ommDateTime.toString();
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
    public int hour()
    {
        return hour;
    }

    @Override
    public int minute()
    {
        return minute;
    }

    @Override
    public int second()
    {
        return second;
    }

    @Override
    public int millisecond()
    {
        return millisecond;
    }

    @Override
    public int microsecond()
    {
        return microsecond;
    }

    @Override
    public int nanosecond()
    {
        return nanosecond;
    }

    @Override
    public String toString()
    {
        return valueAsString;
    }
}