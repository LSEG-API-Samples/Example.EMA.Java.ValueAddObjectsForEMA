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
package com.refinitiv.platformservices.rt.objects.data;

import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.OmmTime;

/**
 * 
 * In-memory implementation of the <code>OmmTime</code> interface.
 */
class OmmTimeImpl extends DataImpl implements OmmTime
{
    private final int hour;
    private final int minute;
    private final int second;
    private final int millisecond;
    private final int microsecond;
    private final int nanosecond;
    private final String valueAsString;

    /**
     * Copy the given <code>OmmTime</code>
     * @param ommTime the <code>OmmTime</code> to copy.
     */
    public OmmTimeImpl(OmmTime ommTime)
    {
        super(ommTime);
        this.hour = ommTime.hour();
        this.minute = ommTime.minute();
        this.second = ommTime.second();
        this.millisecond = ommTime.millisecond();
        this.microsecond = ommTime.microsecond();
        this.nanosecond = ommTime.nanosecond();
        this.valueAsString = ommTime.toString();
    }

    @Override
    public int dataType()
    {
        return DataType.DataTypes.TIME;
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