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
package com.thomsonreuters.platformservices.elektron.objects.marketprice;

import com.thomsonreuters.platformservices.elektron.objects.data.Field;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmState;
import com.thomsonreuters.platformservices.elektron.objects.common.Completable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The interface of a <code>MarketPrice</code> implementation that you can use
 * to open level 1 instruments published on the real-time platform (Elektron 
 * or TREP). <code>MarketPrice</code>s must be built using a <code>MarketPrice.Builder</code>.
 * As an example, the code snippet below creates and opens the EUR/USD currency 
 * quote (EUR=) <code>MarketPrice</code> published on the ELEKTRON_DD service:
 * <br>
 * <br>
 * <pre>
 *    OmmConsumer ommConsumer = ...;
 *      .
 *      .
 *      .
 *      MarketPrice theMarketPrice = new MarketPrice.Builder()
 *           .withOmmConsumer(ommConsumer)
 *           .withName("EUR=")
 *           .withServiceName("ELEKTRON_DD")
 *           .build();
 * 
 *      theMarketPrice.open();
 * </pre>
 * <br>
 * There are more parameters you can use with <code>MarketPrice</code> objects. 
 * They must be set at built time via a {@link MarketPrice.Builder}. Please refer 
 * to this class for the exhaustive parameters list.
 * <br>
 * <br>
 * <strong>Note that this implementation of <code>MarketPrice</code> is synchronized</strong>. 
 * It can be used indifferently with the <code>API_DISPATCH</code> and 
 * <code>USER_DISPATCH</code> <code>OmmConsumer</code> operation models.
 */
public interface MarketPrice extends Completable
{
    /**
     * Returns the name of this <code>MarketPrice</code>. This is the name used 
     * to identify the instrument on the real-time platform.
     * @return the name of this <code>MarketPrice</code>.
     */    
    String getName();

    /**
     * Returns the name of the service this <code>MarketPrice</code> is published on. 
     * @return the service name used to subscribe to this <code>MarketPrice</code>.
     */    
    String getServiceName();

    /**
     * Opens this <code>MarketPrice</code>. Opening a <code>MarketPrice</code> 
     * initiates the subscription to the <code>MarketPrice</code> level 1 instrument
     * published on the platform. Once opened, the <code>MarketPrice</code> starts
     * invoking the functional interfaces specified at built time. This method has 
     * no effect if the <code>MarketPrice</code> is already opened. 
     */
    void open();
    
    /**
     * Closes this <code>MarketPrice</code> and unsubscribes to the instrument 
     * published by the platform. Once closed the <code>MarketPrice</code> stops
     * invoking the functional interfaces that have been specified when at built 
     * time. This  method has no effect if the <code>MarketPrice</code> is already closed. 
     */
    void close();

    /**
     * Returns the collection of all <code>Fields</code> that compose the image of 
     * this <code>MarketPrice</code>. This image (all fields) is automatically kept 
     * updated if the <code>MarketPrice</code> was open in streaming mode and if 
     * the events of the underlying <code>OmmConsumer</code> are properly dispatched.
     * @see MarketPrice.Builder#withUpdates(boolean)
     * @return the collection of <code>Fields</code> that compose the image of this 
     * <code>MarketPrice</code>. It may return an empty collection is the <code>MarketPrice</code>
     * is not opened yet or if the first Refresh message has not been received 
     * yet from the platform.
     */    
    Collection<Field> getFields();

    /**
     * Returns the <code>Field</code> identified by <code>fieldName</code> from 
     * the cached image of this <code>MarketPrice</code>. 
     * @param fieldName name of the <code>Field</code> to return.
     * @return the <code>Field</code> identified by <code>fieldName</code> or null
     * if the <code>Field</code> doesn't exist.
     */    
    Field getField(String fieldName);
    
    /**
     * Returns the <code>Field</code> identified by <code>fieldId</code> from 
     * the cached image of this <code>MarketPrice</code>. 
     * @param fieldId Id of the <code>Field</code> to return.
     * @return the <code>Field</code> identified by <code>fieldId</code> or null
     * if the <code>Field</code> doesn't exist.
     */    
    Field getField(int fieldId);
    
    /**
     * Returns the <code>OmmState</code> received from the platform for this
     * <code>MarketPrice</code> 
     * @return the <code>OmmState</code> of this <code>MarketPrice</code> or null
     * if the <code>MarketPrice</code> is not opened or if the <code>OmmState</code>
     * has not been received yet from the platform.
     */    
    OmmState getState();
    
    /**
     * Returns the Id of the underlying item stream opened with the platform for the 
     * <code>MarketPrice</code> 
     * @return the Id of the item stream or -1 if the stream is not opened yet.
     */    
    int getStreamId();
 
    /**
     * Represents an optional operation that is called when the <code>MarketPrice</code> 
     * is complete. This operation is optionally set when the <code>MarketPrice</code> 
     * is created. 
     * See the {@link Builder#onComplete(com.thomsonreuters.platformservices.elektron.objects.marketprice.MarketPrice.OnCompleteFunction) }
     * for more details.
     */
    @FunctionalInterface
    interface OnCompleteFunction
    {

        /**
         * Called when a <code>MarketPrice</code> is complete. A <code>MarketPrice</code> 
         * is said "complete" when it received the first Refresh message or Status message
         * from the platform after it has been opened.
         * @param marketPrice the completed MarketPrice.
         */
        void onComplete(MarketPrice marketPrice);
    }

    /**
     * Represents an optional operation that is called when a <code>MarketPrice</code>  
     * receives an new Refresh message (a.k.a. Image - All fields). This operation
     * is optionally set when the <code>MarketPrice</code> is created. 
     * See the {@link Builder#onImage(com.thomsonreuters.platformservices.elektron.objects.marketprice.MarketPrice.OnImageFunction) }
     * for more details.
     */
    @FunctionalInterface
    interface OnImageFunction
    {

        /**
         * Called when a <code>MarketPrice</code> receives a new Refresh message
         * (a.k.a. Image - All fields).
         * @param marketPrice the MarketPrice.
         * @param image the received image.
         * @param state the state received with the image.
         */
        void onImage(MarketPrice marketPrice, Collection<Field> image, OmmState state);
    }

    /**
     * Represents an optional operation that is called when a <code>MarketPrice</code>  
     * receives an update message (updated fields only). This operation is optionally 
     * set when the <code>MarketPrice</code> is created. 
     * See the {@link Builder#onUpdate(com.thomsonreuters.platformservices.elektron.objects.marketprice.MarketPrice.OnUpdateFunction) }
     * for more details.
     */
    @FunctionalInterface
    interface OnUpdateFunction
    {

        /**
         * Called when a <code>MarketPrice</code> receives an update (updated 
         * fields only).
         * @param marketPrice the MarketPrice.
         * @param update the received update.
         */
        void onUpdate(MarketPrice marketPrice, Collection<Field> update);
    }

    /**
     * Represents an optional operation that is called when a <code>MarketPrice</code>  
     * receives a Status message that transports a new State of the <code>MarketPrice</code>. 
     * This operation is optionally set when the <code>MarketPrice</code> is created. 
     * See the {@link Builder#onState(com.thomsonreuters.platformservices.elektron.objects.marketprice.MarketPrice.OnStateFunction) }
     * for more details.
     */
    @FunctionalInterface
    interface OnStateFunction
    {

        /**
         * Called when a <code>MarketPrice</code> receives a new state.
         * @param marketPrice the MarketPrice.
         * @param state the received state.
         */
        void onState(MarketPrice marketPrice, OmmState state);
    }

    public static class Builder {

        OmmConsumer ommConsumer;
        String name = "";
        String serviceName = "ELEKTRON_DD";
        boolean withUpdates = true;
        boolean partialUpdatesManagementActivated = false;
        boolean synchronousModeActivated;
        boolean autoDispatch;
        LinkedList<Integer> fieldIds = new LinkedList<>();
        LinkedList<String> fieldNames = new LinkedList<>();
        OnCompleteFunction onCompleteFunction = (marketPrice) -> {};
        OnImageFunction onImageFunction = (marketPrice, image, state) -> {};
        OnUpdateFunction onUpdateFunction = (marketPrice, update) -> {};
        OnStateFunction onStateFunction = (marketPrice, state) -> {};
        
        /**
         * Default constructor
         */
        public Builder() 
        {            
        }

        /**
         * Sets the EMA OmmConsumer used by the <code>MarketPrice</code> to subscribe. 
         * This <code>OmmConsumer</code> must have been properly initialized and connected 
         * to a Thomson Reuters real-time platform.
         * 
         * @param ommConsumer the OmmConsumer that will be used by the 
         * <code>OmmConsumer</code> to subscribe.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code> 
         * methods calls.
         */
        public Builder withOmmConsumer(OmmConsumer ommConsumer) 
        {
            this.ommConsumer = ommConsumer;
            return this;
        }

        /**
         * Sets the name of the market price level 1 instrument to 
         * subscribe to.
         * @param name the name of the <code>MarketPrice</code>.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code> 
         * methods calls.
         */
        public Builder withName(String name) 
        {
            this.name = name;
            return this;
        }

        /**
         * Sets the name of the service the  market price instrument is published on. 
         * This service name is used by the<code>MarketPrice</code> to subscribe. 
         * If the service name is not set the default "ELEKTRON_DD" service name
         * is used.
         * @param serviceName the name of the service the market price is published on.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withServiceName(String serviceName) 
        {
            this.serviceName = serviceName;
            return this;
        }

        /**
         * Indicates you are interested in market price updates or not. If you 
         * set <code>withUpdates</code> to <code>true</code> the <code>MarketPrice</code> 
         * will be kept updated with the messages received from the EMA 
         * <code>OmmConsumer</code>. You will also be notified of changes via the 
         * {@link OnImageFunction} or {@link OnUpdateFunction} functions if you 
         * set them at build time. These calls may happen before and after the
         * <code>MarketPrice</code> is complete. <br>
         * If <code>withUpdates</code> is not called, the default value 
         * (<code>true</code>) is used.
         * @param withUpdates whether or not you are interested in market price 
         * changes.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withUpdates(boolean withUpdates)
        {
            this.withUpdates = withUpdates;
            return this;
        }
        
        /**
         * Indicates if the <code>MarketPrice</code> has to manage partial updates.
         * Partial updates are updates that can possibly change only a part of a 
         * specific field. Partial updates only apply to RMTES field types and only 
         * for some level 1 instruments (generally pages composed of ROW64_n or 
         * ROW80_n fields). A good example is the "FXFX" page. 
         * Partial updates management consumes additional resources. For this 
         * reason, it must not be activated for instruments that do not receive 
         * partial updates. 
         * If <code>withPartialUpdatesManagement</code> is not called, the default
         * value (<code>false</code>) is used.
         * @param partialUpdatesManagementActivated whether or not you want to 
         * activate partial updates management.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withPartialUpdatesManagement(boolean partialUpdatesManagementActivated)
        {
            this.partialUpdatesManagementActivated = partialUpdatesManagementActivated;
            return this;        
        }
        
        /**
         * Indicates if the <code>MarketPrice</code> must be opened synchronously.
         * If <code>withSynchronousMode</code> is not called, the <code>MarketPrice</code>
         * will be opened asynchronously.
         * By default the autoDispatch mode is false meaning that the {@link #open()}
         * method will not dispatch events but just wait until the item is 
         * complete before returning control to the calling thread. 
         * This means that EMA events must be dispatch either from another 
         * application thread or from the EMA thread (see the API_DISPATCH EMA
         * operation model). 
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withSynchronousMode()
        {
            boolean autoDispatch = false;
            return withSynchronousMode(autoDispatch);
        }
        
        /**
         * Indicates if the <code>MarketPrice</code> must be opened synchronously.
         * If <code>withSynchronousMode</code> is not called, the <code>MarketPrice</code>
         * will be opened asynchronously.
         * @param autoDispatch indicates if the MarketPrice will have to dispatch
         * EMA events when the {@link #open()} method is called (see the 
         * USER_DISPATCH EMA operation model) or if these events will be dispatched 
         * by another thread. In the later case, the {@link #open()} method 
         * will not dispatch events but just wait until the item is complete. 
         * This means that EMA events must be dispatch either from another 
         * application thread or from the EMA thread (see the API_DISPATCH EMA
         * operation model). 
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withSynchronousMode(boolean autoDispatch)
        {
            this.synchronousModeActivated = true;
            this.autoDispatch = autoDispatch;
            return this;
        }

        /**
         * Indicates that the <code>MarketPrice</code> must subscribe to the 
         * instrument using a view that contains this field Id. 
         * You can chain method calls to specify several fields like this:
         * <br>
         * <br>
         * <pre>
         *    MarketPrice theMarketPrice = new MarketPrice.Builder()
         *                                                .
         *                                                .
         *                                                .
         *                                          .withField(3)
         *                                          .withField(22)
         *                                          .withField(25)
         *                                                .
         *                                                .
         *                                                .
         *                                          .build();
         * </pre>
         * <br>
         * If the platform supports views, the <code>MarketPrice</code> will only 
         * receive images and updates that contain the indicated fields. If the 
         * platform doesn't support views, the <code>MarketPrice</code> will 
         * receive images and updates for all fields.
         * <br>
         * <br>
         * <strong>Note</strong>: <code>.withField()</code> calls with ids and 
         * names cannot be mixed with the same builder.
         * @param fieldId the Id of a field to include to the view.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withField(int fieldId)
        {
            fieldIds.add(fieldId);
            return this;
        }
        
        /**
         * Indicates that the <code>MarketPrice</code> must subscribe to the 
         * instrument using a view that contains this field name. 
         * You can chain method calls to specify several fields like this:
         * <br>
         * <br>
         * <pre>
         *    MarketPrice theMarketPrice = new MarketPrice.Builder()
         *                                                .
         *                                                .
         *                                                .
         *                                          .withField("DSPLY_NAME")
         *                                          .withField("BID")
         *                                          .withField("ASK")
         *                                                .
         *                                                .
         *                                                .
         *                                          .build();
         * </pre>
         * <br>
         * If the platform supports views, the <code>MarketPrice</code> will only 
         * receive images and updates that contain the indicated fields. If the 
         * platform doesn't support views, the <code>MarketPrice</code> will 
         * receive images and updates for all fields.
         * <br>
         * <br>
         * <strong>Note</strong>: <code>.withField()</code> calls with ids and 
         * names cannot be mixed with the same builder.
         * @param fieldName the name of a field to include to the view.
         * @return this <code>Builder</code> so that you can chain other <code>Builder</code>
         * methods calls.
         */
        public Builder withField(String fieldName)
        {
            fieldNames.add(fieldName);
            return this;
        }
        
        /**
         * Sets the function to be called when the market price is complete.
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    MarketPrice theMarketPrice = new MarketPrice.Builder()
         *        .onComplete(
         *            (marketPrice) -&gt; 
         *                marketPrice.getFields().forEach(
         *                    (field) -&gt; 
         *                        println(&quot;\t&quot; + field.description().acronym() + &quot; (&quot; + field.description().fid() + &quot;) = &quot; + field.value())
         *                )
         *         )
         *                        .
         *                        .
         *                        .
         * </pre>
         * <br>
         * @param function function to be called when the market price is complete.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onComplete(OnCompleteFunction function)
        {
            this.onCompleteFunction = function;
            return this;
        }        
        
        /**
         * Sets the function to be called when the market price receives a 
         * new image (all fields).
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    MarketPrice theMarketPrice = new MarketPrice.Builder()
         *        .onImage(
         *            (marketPrice, image) -&gt; 
         *                image.forEach(
         *                    (field) -&gt; 
         *                        println(&quot;\t&quot; + field.description().acronym() + &quot; (&quot; + field.description().fid() + &quot;) = &quot; + field.value())
         *                )
         *        )
         *                        .
         *                        .
         *                        .
         * </pre>
         * <br>
         * @param function function to be called when the market price receives
         * a new image.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onImage(OnImageFunction function)
        {
            this.onImageFunction = function;
            return this;
        }        
        
        /**
         * Sets the function to be called when the market price receives an 
         * update (updated fields only).
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    MarketPrice theMarketPrice = new MarketPrice.Builder()
         *        .onUpdate(
         *            (marketPrice, update) -&gt; 
         *                update.forEach(
         *                    (field) -&gt; 
         *                        println(&quot;\t&quot; + field.description().acronym() + &quot; (&quot; + field.description().fid() + &quot;) = &quot; + field.value())
         *                )
         *        )
         *                        .
         *                        .
         *                        .
         * </pre>
         * <br>
         * @param function function to be called when the market price receives
         * an update.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onUpdate(OnUpdateFunction function)
        {
            this.onUpdateFunction = function;
            return this;
        }        
        
        /**
         * Sets the function to be called when the market price receives a new 
         * state (Status).
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    MarketPrice theMarketPrice = new MarketPrice.Builder()
         *        .onState(
         *            (marketPrice, State) -&gt; 
         *                println(&quot;\tNew state received for &lt;&quot; + marketPrice.getName() + &quot;&gt;: &quot; + state)
         *        )
         *                        .
         *                        .
         *                        .
         * </pre>
         * <br>
         * @param function function to be called when the market price receives
         * a new state.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onState(OnStateFunction function)
        {
            this.onStateFunction = function;
            return this;
        }        
        
        /**
         * Builds the <code>MarketPrice</code> object
         * @return the built MarketPrice
         */
        public MarketPrice build() 
        {
            if (name.isEmpty())
            {
                throw new IllegalStateException("The MarketPrice.Builder cannot build a MarketPrice with no name");
            }
            if (ommConsumer == null)
            {
                throw new IllegalStateException("The MarketPrice.Builder cannot build a MarketPrice without an OmmConsumer.");
            }
            if(!fieldIds.isEmpty() && !fieldNames.isEmpty())
            {
                throw new IllegalStateException("The MarketPrice.Builder cannot build this MarketPrice. Views cannot be defined with a mix of field ids and field names.");
            }

            MarketPriceImpl builtMarketPrice = new MarketPriceImpl(this);

            return builtMarketPrice;
        }
    }
    
}
