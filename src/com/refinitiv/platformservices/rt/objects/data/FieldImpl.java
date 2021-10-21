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
import com.refinitiv.ema.access.OmmRmtes;
import com.refinitiv.ema.access.RmtesBuffer;
import com.refinitiv.ema.rdm.DataDictionary;
import com.refinitiv.ema.rdm.DictionaryEntry;

/**
 * 
 * Implementation of the <code>Field</code> interface. 
 */
class FieldImpl implements Field {

    private final int  id;
    private final Data value;
    private final DataDictionary dataDictionary;
   
    /**
     * Constructor used by the <code>Field.Builder</code> to build a new 
     * <code>Field</code> object. 
     * @param builder the <code>Builder</code> that contains all the parameters
     * required to build the class. This is this builder that instantiated this 
     * object by calling this constructor.
     */    
    public FieldImpl(Field.Builder builder) 
    {
        id = builder.id;
        dataDictionary = builder.dataDictionary;
        
        value = Utils.clone(builder.value, dataDictionary.entry(id));        
    }

    @Override
    public DictionaryEntry description() 
    {
        return dataDictionary.entry(id);
    }

    @Override
    public Data value() 
    {
        return value;
    }

    @Override
    public Field applyPartialUpdate(Data fieldValue) 
    {
        if(this.value.dataType() == DataType.DataTypes.RMTES
                &&
           fieldValue.dataType() == DataType.DataTypes.RMTES)
        {
            RmtesBuffer rmtesImage = ((OmmRmtes)this.value()).rmtes();
            RmtesBuffer rmtesUpdate = ((OmmRmtes)fieldValue).rmtes();        
            rmtesImage.apply(rmtesUpdate);

            return this;
        }
        else
        {
            return new Field.Builder()
                    .withId(id)
                    .withValue(fieldValue)
                    .withDataDictionary(dataDictionary)
                    .build();
        }
    }
}
