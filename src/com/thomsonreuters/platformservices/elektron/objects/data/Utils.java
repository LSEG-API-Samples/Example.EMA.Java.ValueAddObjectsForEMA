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
import com.thomsonreuters.ema.access.DataType.DataTypes;
import com.thomsonreuters.ema.access.OmmAscii;
import com.thomsonreuters.ema.access.OmmDate;
import com.thomsonreuters.ema.access.OmmEnum;
import com.thomsonreuters.ema.access.OmmError;
import com.thomsonreuters.ema.access.OmmInt;
import com.thomsonreuters.ema.access.OmmReal;
import com.thomsonreuters.ema.access.OmmRmtes;
import com.thomsonreuters.ema.access.OmmTime;
import com.thomsonreuters.ema.access.OmmUInt;
import com.thomsonreuters.ema.rdm.DictionaryEntry;
import com.thomsonreuters.ema.rdm.EnumType;

/**
 * 
 * Helper class used to clone RWF based OmmData into in-memory OmmData.
 * 
 * Rationales: OmmData received from EMA is based on an RWF Buffer implementation 
 * that cannot be used out of the context of EMA OmmConsumerClient callback methods.
 * In other words, the EMA RWF implementation cannot be used to preserve in-memory
 * the data received from the Platform. In order to workaround this limitation,
 * Received OmmData is cloned to an in-memory implementation of the different
 * OMM data types.
 * 
 */
class Utils {
 
    /**
     * Clone an OmmData into an OmmData in-memory implementation
     * @param originalValue the <code>Data</code> to clone.
     * @return the cloned <code>Data</code>.
     */
    static Data clone(Data originalValue) 
    {
        return clone(originalValue, null);
    }
    
    /**
     * Clone an OmmData into an OmmData in-memory implementation
     * @param originalValue the <code>Data</code> to clone.
     * @param dictionaryEntry the <code>DictionaryEntry</code> of the data to 
     * clone. Used for the conversion of enumerated fields to string.
     * @return the cloned <code>Data</code>.
     */
    static Data clone(Data originalValue, DictionaryEntry dictionaryEntry) 
    {
        Data clonedValue;
        
        switch(originalValue.dataType())
        {
            case DataTypes.REAL:
                clonedValue = new OmmRealImpl((OmmReal)originalValue);
                break;
            case DataTypes.DATE:
                clonedValue = new OmmDateImpl((OmmDate)originalValue);
                break;
            case DataTypes.TIME:
                clonedValue = new OmmTimeImpl((OmmTime)originalValue);
                break;
            case DataTypes.INT:
                clonedValue = new OmmIntImpl((OmmInt)originalValue);
                break;
            case DataTypes.UINT:
                clonedValue = new OmmUIntImpl((OmmUInt)originalValue);
                break;
            case DataTypes.ASCII:
                clonedValue = new OmmAsciiImpl((OmmAscii)originalValue);
                break;
            case DataTypes.ENUM:
                {
                    OmmEnum originalOmmEnum = (OmmEnum)originalValue;
                    if(dictionaryEntry != null)
                    {
                        EnumType enumType = dictionaryEntry.enumType(originalOmmEnum.enumValue());
                        String enumValueAsString = enumType.display();
                        clonedValue = new OmmEnumImpl(originalOmmEnum, enumValueAsString);
                    }
                    else
                    {
                        clonedValue = new OmmEnumImpl(originalOmmEnum);
                    }
                }
                break;
            case DataTypes.RMTES:
                clonedValue = new OmmRmtesImpl((OmmRmtes)originalValue);
                break;
            case DataTypes.ERROR:
                clonedValue = new OmmErrorImpl((OmmError)originalValue);
                break;
            default:
                clonedValue = new OmmErrorImpl(OmmError.ErrorCode.UNSUPPORTED_DATA_TYPE, "UnsupportedDataType");
                break;
        }
        
        return clonedValue; 
    }
    
}
