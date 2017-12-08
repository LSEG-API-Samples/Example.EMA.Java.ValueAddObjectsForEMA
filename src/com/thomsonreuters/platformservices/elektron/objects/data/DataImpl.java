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

import com.thomsonreuters.ema.access.Data;
import java.nio.ByteBuffer;

/**
 * 
 * In-memory implementation of the OmmData interface.
 * 
 * Rationales: OmmData received from EMA is based on an RWF Buffer implementation 
 * that cannot be used out of the context of EMA OmmConsumerClient callback methods.
 * In other words, the EMA RWF implementation cannot be used to preserve in-memory
 * the data received from the Platform. In order to workaround this limitation,
 * Received OmmData is cloned to an in-memory implementation of the different
 * OMM data types.
 * 
 */
abstract class DataImpl implements Data
{
    protected final static String EMPTY_STRING          = "";
    protected final static String NOCODE_STRING         = "NoCode";
    protected final static String BLANK_STRING          = "(blank data)";
    protected final static String DEFAULTCODE_STRING    = "Unknown DataCode value ";

    protected final int code;

    public DataImpl()
    {
        this.code = DataCode.NO_CODE;
    }
    
    public DataImpl(Data data)
    {
        this.code = data.code();
    }
    
    @Override
    public int code()
    {
        return code;
    }

    @Override
    public String codeAsString()
    {
        switch (code())
        {
            case DataCode.NO_CODE:
                    return NOCODE_STRING;
            case DataCode.BLANK:
                    return BLANK_STRING;
            default:
                    return DEFAULTCODE_STRING + code();
        }
    }

    @Override
    public ByteBuffer asHex() 
    {
        throw new UnsupportedOperationException("Not supported by this implementation.");
    }
}