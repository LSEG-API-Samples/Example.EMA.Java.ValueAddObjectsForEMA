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
import com.thomsonreuters.ema.access.OmmAscii;

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