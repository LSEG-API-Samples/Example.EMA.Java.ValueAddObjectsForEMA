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
import java.util.List;
import java.util.Map;

/**
 * The interface of a <code>Chain</code> implementation you can use to open 
 * recursive chains. On the contrary of <code>FlatChain</code>s <code>RecursiveChain</code>s
 * are meant to recursively open chains that contain other chains. This is very 
 * handy for some use cases but this also comes with a limitation: you cannot 
 * open a <code>RecursiveChain</code> with updates. <code>RecursiveChain</code>s 
 * must be built using a <code>RecursiveChain.Builder</code>. As an example, the 
 * code snippet below creates and opens the Equity Markets chain that has a max 
 * depth of 4 levels:
 * <br>
 * <br>
 * <pre>
 *    OmmConsumer ommConsumer = ...;
 *      .
 *      .
 *      .
 *    RecursiveChain theChain = new RecursiveChain.Builder()
 *           .withOmmConsumer(ommConsumer)
 *           .withChainName("0#EQUITYMARKETS")
 *           .withServiceName("ELEKTRON_DD")
 *           .build();
 * 
 *    theChain.open();
 * </pre>
 * There are more parameters you can use with <code>RecursiveChain</code> objects.
 * They must be set at built time via a {@link RecursiveChain.Builder}. Please 
 * refer to this class for the exhaustive list.
 * <br>
 * <br>
 * <strong>Note that this implementation of <code>RecursiveChain</code> is 
 * synchronized</strong>. It can be used indifferently with the <code>API_DISPATCH</code> 
 * and <code>USER_DISPATCH</code> <code>OmmConsumer</code> operation models.
 * <br>
 * <br>
 * Below is an excerpt of the Equity Markets chain elements:
 * <br>
 * <br>
 * <pre>
 *    0#EQUITYMARKETS[0, 0]       = [0#CA-EQ, 0#CA-SECTORS]
 *    0#EQUITYMARKETS[0, 1, 0]    = [0#CA-EQ, 0#CA-MKTSTAT, .PG.TO]
 *    0#EQUITYMARKETS[0, 1, 1]    = [0#CA-EQ, 0#CA-MKTSTAT, .PL.TO]
 *    0#EQUITYMARKETS[0, 1, 2]    = [0#CA-EQ, 0#CA-MKTSTAT, .NG.TO]
 *     ...
 *    0#EQUITYMARKETS[1, 5, 437]  = [0#DE-EQ, 0#DE-INDICES, .CXPYX]
 *    0#EQUITYMARKETS[1, 5, 438]  = [0#DE-EQ, 0#DE-INDICES, .CXKYX]
 *    0#EQUITYMARKETS[2, 0]       = [0#FR-EQ, .FCHI]
 *    0#EQUITYMARKETS[2, 1]       = [0#FR-EQ, .SBF250]
 *    0#EQUITYMARKETS[2, 2, 0]    = [0#FR-EQ, 0#FR-SECTORS, .FRBM]
 *    0#EQUITYMARKETS[2, 2, 1]    = [0#FR-EQ, 0#FR-SECTORS, .FRIN]
 *     ... 
 *    0#EQUITYMARKETS[4, 2, 20]   = [0#IT-EQ, 0#IT-SECTORS, .MHME]
 *    0#EQUITYMARKETS[4, 2, 21]   = [0#IT-EQ, 0#IT-SECTORS, .MHTT]
 *    0#EQUITYMARKETS[4, 3, 0, 0] = [0#IT-EQ, 0#IT-MKTSTAT, .PG.MI, DA.MI]
 *    0#EQUITYMARKETS[4, 3, 0, 1] = [0#IT-EQ, 0#IT-MKTSTAT, .PG.MI, LVEN.MI]
 *     ... 
 *    0#EQUITYMARKETS[25, 5, 25]  = [0#RU-EQ, 0#RU-INDICES, .MCXLC]
 *    0#EQUITYMARKETS[25, 5, 26]  = [0#RU-EQ, 0#RU-INDICES, .MCXMC]
 *    0#EQUITYMARKETS[25, 5, 27]  = [0#RU-EQ, 0#RU-INDICES, .MCXSC]
 * </pre>
 * As you can see, the position of each element is represented by a list of 
 * numbers (Each number representing a position at a given depth in the chain). 
 * The element name is made of a list of strings that represents the path to 
 * reach this element in the recursive chain .
 */
public interface RecursiveChain extends Chain
{        
    /**
     * Returns the elements of this <code>RecursiveChain</code>. 
     * <code>RecursiveChain</code> elements are returned in a <code>Map</code> 
     * with the elements positions as keys and the elements names as values. The
     * first position starts at 0. The map is sorted according to the natural 
     * ordering of the positions. If the chain is not complete this method may 
     * return a partial list of elements. 
     * <br>
     * <br>
     * An element position is represented by a list of numbers (Each number 
     * representing a position at a given depth in the chain). 
     * <br>
     * An element name is made of a list of strings that represents the path to 
     * reach this element in the recursive chain .
     * <br>
     * As an example: In the 0#EQUITYMARKETS chain the position of LVEN.MI is
     * [4, 3, 0, 1] and it's name is [0#IT-EQ, 0#IT-MKTSTAT, .PG.MI, LVEN.MI].
     * 
     * @return a <code>Map</code> that contains the elements names (values) and 
     * their respective positions (keys).
     */
    public abstract Map<List<Long>, List<String>> getElements();
        
    /**
     * Represents an optional operation that is called when an element is added 
     * to a {@link RecursiveChain}. This operation is optionally set when the 
     * {@link RecursiveChain} is created. See the {@link Builder#onElementAdded(com.thomsonreuters.platformservices.elektron.objects.chain.RecursiveChain.OnElementAddedFunction) }
     * for more details.
     */
    @FunctionalInterface
    public interface OnElementAddedFunction
    {

        /**
         * Called by a <code>RecursiveChain</code> when a new element is added.
         * @param position position of the element in the chain (starts at 0).
         * An element position is represented by a list of numbers (Each number 
         * representing a position at a given depth in the chain). 
         * @param name name of the new element.
         * An element name is made of a list of strings that represents the path to 
         * reach this element in the recursive chain .
         * @param chain the RecursiveChain the new element has been added to. 
         */
        void onElementAdded(List<Long> position, List<String> name, RecursiveChain chain);
    }
    
    /**
     * Represents an optional operation that is called when a chain is complete.
     * This operation is optionally set when the {@link RecursiveChain} is 
     * created. See the {@link Builder#onComplete(com.thomsonreuters.platformservices.elektron.objects.chain.RecursiveChain.OnCompleteFunction) }
     * for more details.
     */
    @FunctionalInterface
    public interface OnCompleteFunction
    {

        /**
         * Called when a <code>RecursiveChain</code> is complete.
         * @param chain the completed chain.
         */
        void onComplete(RecursiveChain chain);
    }

    /**
     * Represents an optional operation that is called when a chain is in error.
     * This operation is optionally set when the {@link RecursiveChain} is 
     * created. See the {@link Builder#onError(com.thomsonreuters.platformservices.elektron.objects.chain.RecursiveChain.OnErrorFunction) }
     * for more details.
     */    
    @FunctionalInterface
    public interface OnErrorFunction
    {

        /**
         * Called when a <code>RecursiveChain</code> is in error. <br>
         * <strong>Note:</strong> A chain in error is considered completed.
         * @param errorMessage the error message.
         * @param chain the chain in error.
         */
        void onError(String errorMessage, RecursiveChain chain);
    }

    /**
     * Used to build <code>RecursiveChain</code> objects. <code>RecursiveChain</code>s 
     * are built as if they were immutable objects. This means that you can't 
     * directly change their fields once they are built (there is no setter). 
     * However, <code>RecursiveChain</code>s cannot be considered as pure 
     * immutable objects as their state changes when they receive data from EMA.
     * <br>
     * The following code snippet builds a <code>FlatChain</code> for the 
     * Equity Markets chain and set the two functions (lambda expressions in this 
     * case) to be called when the chain is complete and when an error occurs:
     * <br>
     * <br>
     * <pre>
     *    OmmConsumer ommConsumer = ...;
     *      .
     *      .
     *      .
     *    RecursiveChain theChain = new RecursiveChain.Builder()
     *                  .withOmmConsumer(ommConsumer)
     *                  .withChainName("0#EQUITYMARKETS")
     *                  .onComplete(
     *                          (chain) -&gt; 
     *                                  chain.getElements().forEach(
     *                                          (position, name) -&gt; 
     *                                                  System.out.println("\t" + chain.getName() + position + " = " + name)
     *                                  )
     *                  )
     *                  .onError(
     *                          (errorMessage, chain) -&gt; 
     *                                  System.out.println("\tError received for &lt;" + chain.getName() + "&gt;: " + errorMessage)
     *                  )
     *                  .build();
     * 
     *    theChain.open();
     * </pre>
     * <br>
     * <strong>Note:</strong> The name of the chain and the OmmConsumer are the 
     * two mandatory parameters that must be set for every RecursiveChain 
     * (see {@link #withChainName(String)} and {@link #withOmmConsumer(OmmConsumer)}). 
     */    
    public static class Builder
    {
        OmmConsumer ommConsumer;
        String chainName = "";
        String serviceName = "ELEKTRON";
        SummaryLinksToSkipByDisplayTemplate summaryLinksToSkipByDisplayTemplate;
        int nameGuessesCount;
        int maxDepth = -1;
        boolean synchronousModeActivated;
        boolean autoDispatch;        
        OnElementAddedFunction onElementAddedFunction = (position, path, chain) ->{};
        OnCompleteFunction onCompleteFunction = (chain) -> {};
        OnErrorFunction onErrorFunction = (errorMessage, chain) -> {};

        /**
         * Default constructor
         */
        public Builder()
        {
        }

        /**
         * Builds the <code>RecursiveChain</code>
         * @return the built RecursiveChain
         */
        public RecursiveChain build()
        {
            if (chainName.isEmpty())
            {
                throw new IllegalStateException("The Chain.Builder cannot build a chain without a name");
            }
            if (ommConsumer == null)
            {
                throw new IllegalStateException("The Chain.Builder cannot build a chain without an OmmConsumer.");
            }

            RecursiveChainImpl builtChain = new RecursiveChainImpl(this);
            
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
         * Sets the number of summary links to skip when opening the chain. Some
         * chains start with summary links (elements) that you may want to skip 
         * because they are not part of the chain's constituents. The number of 
         * summary links to skip is dependant of the Display Template field of
         * the chain. This method allows you to set the number of summary link 
         * to skip for the specified display templates. Checkout this <a href="https://developers.thomsonreuters.com/platform-services-work-progress/ema/docs?content=12021&type=documentation_item" target="_blank">Thomson Reuters article</a> 
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
         * Sets the maximum depth to go when recursively opening this chain.
         * @param maxDepth the maximum depth. Sub chains at this depth are 
         * not recursively opened and considered as leaves.
         * @return this Builder so that you can chain other Builder methods calls.
         */
        public Builder withMaxDepth(int maxDepth)
        {
            this.maxDepth = maxDepth;
            return this;
        }
        
        /**
         * Indicates if the <code>RecursiveChain</code> must be opened synchronously.
         * If <code>withSynchronousMode</code> is not called, the <code>RecursiveChain</code>
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
         * Indicates if the <code>RecursiveChain</code> must be opened synchronously.
         * If <code>withSynchronousMode</code> is not called, the <code>RecursiveChain</code>
         * will be opened asynchronously.
         * @param autoDispatch indicates if the <code>RecursiveChain</code> will have to dispatch
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
         * Sets the function to be called when the chain is complete.
         * Usage example with a lambda expression:
         * <br>
         * <br>
         * <pre>
         *    RecursiveChain theChain = new RecursiveChain.Builder()
         *                  .onComplete(
         *                          (chain) -&gt; 
         *                                  chain.getElements().forEach(
         *                                          (position, name) -&gt; 
         *                                                  System.out.println("\t" + chain.getName() + position + " = " + name)
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
         *    RecursiveChain theChain = new RecursiveChain.Builder()
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
