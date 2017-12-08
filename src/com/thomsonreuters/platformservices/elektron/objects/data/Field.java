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
import com.thomsonreuters.ema.rdm.DataDictionary;
import com.thomsonreuters.ema.rdm.DictionaryEntry;

/**
 * The interface of a <code>Field</code> implementation that you can use
 * to retrieve field values of instruments published on the real-time platform. 
 * (Elektron or TREP). <code>Field</code> objects can be built using a 
 * {@link Builder} but generally they are retrieved from Elektron objects like 
 * <code>MarketPrice</code>.
 * As an example, the code snippet below prints the BID and ASK fields from
 * and opened <code>MarketPrice</code>:
 * <br>
 * <br>
 * <pre>
 *    MarketPrice theMarketPrice =  ...;
 *    theMarketPrice.open();
 *      .
 *      .
 *      .
 *    Field bid = theMarketPrice.getField("BID");
 *    Field ask = theMarketPrice.getField("ASK");
 *    println(bid.description().acronym() + " = " + bid.value());
 *    println(ask.description().acronym() + " = " + ask.value());
 * </pre>
 */
public interface Field 
{
    /**
     * Returns the description of the <code>Field</code>.
     * @return the field description. See the <code>DictionaryEntry</code> class
     * in the EMA reference guide for more details about the returned type.
     */    
    public DictionaryEntry description();

    /**
     * Returns the value of the <code>Field</code>.
     * @return the field value. See the <code>DictionaryEntry</code> class
     * in the EMA reference guide for more details about the returned type.
     */    
    public Data value();
    
    /**
     * Applies a partial update on the <code>Field</code> and return an updated 
     * version of this field.
     * <br>
     * <br>
     * <strong>Note:</strong> This method only impacts on <code>Field</code> 
     * objects that contain and OmmRmtes value. For other value types, the 
     * method simply returns a new <code>Field</code> that contains the 
     * <code>fieldValue</code> given as a parameter
     * @param fieldValue value of the <code>Field</code> to build. See the 
     * <code>Data</code> class in the EMA reference guide for more details about 
     * this type.
     * @return the field value.
     */    
    public Field applyPartialUpdate(Data fieldValue);
    
    
    /**
     * Used to build <code>Field</code> objects. <code>Fields</code>s are
     * immutable objects. This means that you can't directly change their values 
     * once they are built (there is no setter).
     */
    public static class Builder {

        int id;
        Data value;
        DataDictionary dataDictionary;
                
        /**
         * Default constructor
         */
        public Builder() 
        {  
        }

        /**
         * Sets the Id of the <code>Field</code> to build.
         * @param id the field Id.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withId(int id) 
        {
            this.id = id;
            return this;
        }
 
        /**
         * Sets the value of the <code>Field</code> to build.
         * @param value the field value. See the <code>Data</code> class in the 
         * EMA reference guide for more details about this type.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withValue(Data value) 
        {
            this.value = value;
            return this;
        }

        /**
         * Sets the dictionary used to describe the <code>Field</code> to build.
         * @param dataDictionary the field dictionary. See the <code>DataDictionary</code> 
         * class in the EMA reference guide for more details about this type.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withDataDictionary(DataDictionary dataDictionary) 
        {
            this.dataDictionary = dataDictionary;
            return this;
        }

        
        /**
         * Builds the <code>Field</code> object
         * @return the built Field
         */
        public Field build() 
        {
            if (value == null)
            {
                throw new IllegalStateException("The Field.Builder cannot build a Field with no value");
            }
            if (dataDictionary == null)
            {
                throw new IllegalStateException("The Field.Builder cannot build a Field with no DataDictionary.");
            }

            FieldImpl builtField = new FieldImpl(this);

            return builtField;
        }
    }    
}
