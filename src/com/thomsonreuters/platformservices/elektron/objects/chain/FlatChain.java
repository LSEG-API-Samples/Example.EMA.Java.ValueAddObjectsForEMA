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
package com.thomsonreuters.platformservices.elektron.objects.chain;

import com.thomsonreuters.ema.access.OmmConsumer;
import java.util.Map;

/**
 * The interface of a <code>Chain</code> implementation you can use to open flat
 * chains (meaning non recursive chains). <code>FlatChain</code>s must be built 
 * using a <code>FlatChain.Builder</code>. As an example, the code snippet 
 * below creates and opens a <code>FlatChain</code> for the Dow Jones chain 
 * published on the ELEKTRON_DD service:
 * <br>
 * <br>
 * <pre>
 *    OmmConsumer ommConsumer = ...;
 *      .
 *      .
 *      .
 *    FlatChain theChain = new FlatChain.Builder()
 *                  .withOmmConsumer(ommConsumer)
 *                  .withChainName("0#.DJI")
 *                  .withServiceName("ELEKTRON_DD")
 *                  .build();
 * 
 *    theChain.open();
 * </pre>
 * <br>
 * There are more parameters you can use with <code>FlatChain</code> objects. 
 * They must be set at built time via a {@link FlatChain.Builder}. Please refer 
 * to this class for the exhaustive list.
 * <br>
 * <strong>Note that this implementation of FlatChains is synchronized</strong>. It 
 * can be used indifferently with the <code>API_DISPATCH</code> and 
 * <code>USER_DISPATCH</code> <code>OmmConsumer</code> operation models.
 */
public interface FlatChain extends Chain
{

    /**
     * Returns the elements of this chain. FlatChain elements are returned in 
     * a <code>Map</code> with the elements positions as keys and the elements 
     * names as values. The first position starts at 0. The map is sorted 
     * according to the natural ordering of the positions. If the chain is not 
     * complete this method may return a partial list of elements. 
     * @see Chain#isComplete()
     * @see Builder#onComplete(FlatChain.OnCompleteFunction)
     * 
     * @return a <code>Map</code> that contains the elements names (values) and 
     * their respective positions (keys).
     */
    Map<Long, String> getElements();
        
    /**
     * Represents an optional operation that is called when an element is added 
     * to a {@link FlatChain}. This operation is optionally set when the 
     * {@link FlatChain} is created. See the {@link Builder#onElementAdded(com.thomsonreuters.platformservices.elektron.objects.chain.FlatChain.OnElementAddedFunction)} 
     * for more details.
     */
    @FunctionalInterface
    public interface OnElementAddedFunction
    {

        /**
         * Called by a <code>FlatChain</code> when a new element is added.
         * @param position position of the element in the chain (starts at 0).
         * @param name name of the new element.
         * @param chain the FlatChain the new element has been added to. 
         */
        void onElementAdded(long position, String name, FlatChain chain);
    }
    
    /**
     * Represents an optional operation that is called when a chain element  
     * changes. This operation is optionally set when the {@link FlatChain} is 
     * created. See the {@link Builder#onElementChanged(com.thomsonreuters.platformservices.elektron.objects.chain.FlatChain.OnElementChangedFunction) }
     * for more details.
     */
    @FunctionalInterface
    public interface OnElementChangedFunction
    {

        /**
         * Called by a <code>FlatChain</code> when an existing element is changed.
         * @param position position of the element in the chain (starts at 0).
         * @param previousName previous element name.
         * @param newName new element name
         * @param chain the chain that contains the changed element.
         */
        void onElementChanged(long position, String previousName, String newName, FlatChain chain);
    }

    /**
     * Represents an optional operation that is called when a chain element  
     * is removed from a chain. This operation is optionally set when the 
     * {@link FlatChain} is created. See the {@link Builder#onElementRemoved(com.thomsonreuters.platformservices.elektron.objects.chain.FlatChain.OnElementRemovedFunction) } 
     * for more details.
     */
    @FunctionalInterface
    public interface OnElementRemovedFunction
    {

        /**
         * Called by a <code>FlatChain</code> when an existing element is removed.
         * @param position position of the element in the chain (starts at 0).
         * @param chain the chain that contained the removed element.
         */
        void onElementRemoved(long position, FlatChain chain);
    }

    /**
     * Represents an optional operation that is called when a chain is complete  
     * This operation is optionally set when the {@link FlatChain} is created. 
     * See the {@link Builder#onComplete(com.thomsonreuters.platformservices.elektron.objects.chain.FlatChain.OnCompleteFunction) }
     * for more details.
     */
    @FunctionalInterface
    public interface OnCompleteFunction
    {

        /**
         * Called when a <code>FlatChain</code> is complete.
         * @param chain the completed chain.
         */
        void onComplete(FlatChain chain);
    }

    /**
     * Represents an optional operation that is called when a chain is in error.
     * This operation is optionally set when the {@link FlatChain} is created. 
     * See the {@link Builder#onError(com.thomsonreuters.platformservices.elektron.objects.chain.FlatChain.OnErrorFunction) }
     * for more details.
     */
    @FunctionalInterface
    public interface OnErrorFunction
    {
        /**
         * Called when a <code>FlatChain</code> is in error. <br>
         * <strong>Note:</strong> A chain in error is considered completed.
         * @param errorMessage the error message.
         * @param chain the chain in error.
         */
        void onError(String errorMessage, FlatChain chain);
    }
    
    /**
     * Used to build <code>FlatChain</code> objects. <code>FlatChain</code>s are
     * built as if they were immutable objects. This means that you can't directly 
     * change their fields once they are built (there is no setter). However, 
     * <code>FlatChain</code>s cannot be considered as pure immutable objects as
     * their state changes when they receive data from EMA.<br>
     * The following code snippet builds a <code>FlatChain</code> for the British
     * FTSE 100 and set the two functions (lambda expressions in this case) to 
     * be called when the chain is complete and when an error occurs:
     * <br>
     * <br>
     * <pre>
     *    OmmConsumer ommConsumer = ...;
     *      .
     *      .
     *      .
     *    FlatChain theChain = new FlatChain.Builder()
     *                  .withOmmConsumer(ommConsumer)
     *                  .withChainName("0#.FTSE")
     *                  .onChainComplete(
     *                          (chain) -&gt; 
     *                                  chain.getElements().forEach(
     *                                          (position, name) -&gt; 
     *                                                  System.out.println("\t" + chain.getName() + "[" + position + "] = " + name)
     *                                  )
     *                  )
     *                  .onChainError(
     *                          (errorMessage, chain) -&gt; 
     *                                  System.out.println("\tError received for &lt;" + chain.getName() + "&gt;: " + errorMessage)
     *                  )
     *                  .build();
     * </pre>
     * <br>
     * <strong>Note:</strong> The name of the chain and the OmmConsumer are the 
     * two mandatory parameters that must be set for every FlatChain 
     * (see {@link #withChainName(String)} and {@link #withOmmConsumer(OmmConsumer)}). 
     */
    public static class Builder
    {
        OmmConsumer ommConsumer;
        String chainName = "";
        String serviceName = "ELEKTRON_DD";
        boolean withUpdates = false;
        SummaryLinksToSkipByDisplayTemplate summaryLinksToSkipByDisplayTemplate;
        int nameGuessesCount = 0;
        OnElementAddedFunction onElementAddedFunction = (position, name, chain) ->{};
        OnElementChangedFunction onElementChangedFunction = (position, previousName, newName, chain) ->{};
        OnElementRemovedFunction onElementRemovedFunction = (position, chain) -> {};
        OnCompleteFunction onCompleteFunction = (chain) -> {};
        OnErrorFunction onErrorFunction = (errorMessage, chain) -> {};

        /**
         * Default constructor
         */
        public Builder()
        {
        }

        /**
         * Builds the <code>FlatChain</code>
         * @return the built FlatChain
         */
        public FlatChain build()
        {
            if (chainName.isEmpty())
            {
                throw new IllegalStateException("The Chain.Builder cannot build a chain without a name");
            }
            if (ommConsumer == null)
            {
                throw new IllegalStateException("The Chain.Builder cannot build a chain without an OmmConsumer.");
            }

            FlatChainImpl builtChain = new FlatChainImpl(this);

            return builtChain;
        }

        /**
         * Sets the EMA OmmConsumer used by the chain to subscribe to the 
         * underlying <code>Chain Records</code>. This OmmConsumer must have 
         * been properly initialized and connected to a Thomson Reuters real-time
         * infrastructure.
         * 
         * @param ommConsumer the OmmConsumer that will be used by the chain to
         * subscribe to the underlying <code>Chain Records</code>.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withOmmConsumer(OmmConsumer ommConsumer)
        {
            this.ommConsumer = ommConsumer;
            return this;
        }

        /**
         * Sets the name of the chain.
         * @param chainName the name of the chain.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withChainName(String chainName)
        {
            this.chainName = chainName;
            return this;
        }

        /**
         * Sets the name of the service the chain is published on. This service 
         * name is used by the chain to subscribe to the underlying <code>Chain 
         * Record</code>. If the service name is not set the default "ELEKTRON_DD"
         * service name will be used.
         * @param serviceName the name of the service the chain is published on.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withServiceName(String serviceName)
        {
            this.serviceName = serviceName;
            return this;
        }

        /**
         * Indicates you are interested in chain updates or not. If you set 
         * <code>withUpdates</code> to <code>true</code> the chain will be kept
         * updated with the messages received from the EMA OmmConsumer. You will
         * also be notified of changes via the {@link OnElementAddedFunction}, 
         * {@link OnElementChangedFunction} or {@link OnElementRemovedFunction} 
         * functions if you set them at build time. These calls may happen 
         * before and after the chain is complete. <br>
         * If <code>withUpdates</code> is not called, the default value 
         * (<code>false</code>) will be used.
         * @param withUpdates whether or not you're interested in chain changes.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withUpdates(boolean withUpdates)
        {
            this.withUpdates = withUpdates;
            return this;
        }

        /**
         * Sets the number of summary links to skip when opening the chain. Some
         * chains start with summary links (elements) that you may want to skip 
         * because they are not part of the chain's constituents. The number of 
         * summary links to skip depends on the Display Template used by the
         * chain. This method allows you to set the number of summary link to
         * skip for several display templates. Checkout this <a href="https://developers.thomsonreuters.com/platform-services-work-progress/ema/docs?content=12021&type=documentation_item" target="_blank">Thomson Reuters article</a> 
         * to learn more about chains and summary links.  If this method is not 
         * called, no summary links will be skipped.
         * @param summaryLinksToSkipByDisplayTemplate the number of summary links to skip per 
         * display template. 
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withSummaryLinksToSkip(SummaryLinksToSkipByDisplayTemplate summaryLinksToSkipByDisplayTemplate)
        {
            this.summaryLinksToSkipByDisplayTemplate = summaryLinksToSkipByDisplayTemplate;
            return this;
        }

        /**
         * Activates the "Name Guessing Optimization" and sets the number of 
         * names to guess when the chain subscribes to a new <code>Chain 
         * Record</code>. Checkout this <a href="https://developers.thomsonreuters.com/platform-services-work-progress/ema/docs?content=12021&type=documentation_item" target="_blank">Thomson Reuters article</a> 
         * to learn more about the name guessing optimization.  If this method 
         * is not called, the "Name Guessing Optimization" is not activated.
         * @param guessesCount the number of names to guess. A reasonable value 
         * would be between 5 and 50.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withNameGuessingOptimization(int guessesCount)
        {
            if(guessesCount < 0)
            {
                guessesCount = 0;
            }
            
            this.nameGuessesCount = guessesCount;
            return this;
        }
        
        /**
         * Sets the function to be called when the chain decodes a new element. 
         * @param function function to be called on new elements.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onElementAdded(OnElementAddedFunction function)
        {
            this.onElementAddedFunction = function;
            return this;
        }

        /**
         * Sets the function to be called when a chain element changes.
         * @param function function to be called on element change.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onElementChanged(OnElementChangedFunction function)
        {
            this.onElementChangedFunction = function;
            return this;
        }

        /**
         * Sets the function to be called when a chain element is removed.
         * @param function function to be called on element removal.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onElementRemoved(OnElementRemovedFunction function)
        {
            this.onElementRemovedFunction = function;
            return this;
        }

        /**
         * Sets the function to be called when the chain is complete.
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    FlatChain theChain = new FlatChain.Builder()
         *                  .onComplete(
         *                          (chain) -&gt; 
         *                                  chain.getElements().forEach(
         *                                          (position, name) -&gt; 
         *                                                  System.out.println("\t" + chain.getName() + "[" + position + "] = " + name)
         *                                  )
         *                  )
         *                        .
         *                        .
         *                        .
         * </pre>
         * <br>
         * @param function function to be called when the chain is complete.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onComplete(OnCompleteFunction function)
        {
            this.onCompleteFunction = function;
            return this;
        }

        /**
         * Sets the function to be called when an error occurs. 
         * <br>
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    FlatChain theChain = new FlatChain.Builder()
         *                  .onError(
         *                          (errorMessage, chain) -&gt; 
         *                                  System.out.println("\tError received for &lt;" + chain.getName() + "&gt;: " + errorMessage)
         *                  )
         *                        .
         *                        .
         *                        .
         * </pre>
         * <br>
         * @param function function to be called on errors.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder onError(OnErrorFunction function)
        {
            this.onErrorFunction = function;
            return this;
        }
    }
}
