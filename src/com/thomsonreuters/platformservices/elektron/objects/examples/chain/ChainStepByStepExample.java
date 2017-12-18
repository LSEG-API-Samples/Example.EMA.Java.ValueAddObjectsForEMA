/*
 * Copyright 2017 Thomson Reuters
 *
 * DISCLAIMER: This source code has been written by Thomson Reuters for the only 
 * purpose of illustrating the "Simple Chain objects for EMA" article published  
 * on the Thomson Reuters Developer Community. It has not been tested for usage 
 * in production environments. Thomson Reuters cannot be held responsible for any 
 * issues that may happen if these objects or the related source code is used in 
 * production or any other client environment.
 *
 * Thomson Reuters Developer Community: https://developers.thomsonreuters.com
 * Simple Chain objects for EMA - Part 1: https://developers.thomsonreuters.com/article/simple-chain-objects-ema-part-1
 * Simple Chain objects for EMA - Part 2: https://developers.thomsonreuters.com/article/simple-chain-objects-ema-part-2
 *
 */
package com.thomsonreuters.platformservices.elektron.objects.examples.chain;

import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerConfig;
import com.thomsonreuters.ema.access.OmmConsumerConfig.OperationModel;
import com.thomsonreuters.ema.access.OmmException;
import com.thomsonreuters.platformservices.elektron.objects.chain.FlatChain;
import com.thomsonreuters.platformservices.elektron.objects.chain.RecursiveChain;
import com.thomsonreuters.platformservices.elektron.objects.chain.SummaryLinksToSkipByDisplayTemplate;
import com.thomsonreuters.platformservices.elektron.objects.common.Dispatcher;
import com.thomsonreuters.platformservices.elektron.objects.chain.Chain;
import java.io.IOException;
import static java.lang.System.exit;
import static java.lang.System.out;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class ChainStepByStepExample
{
    // TREP or Elektron Service name used request chains and tiles
    // IMPORTANT NOTE:  You may need to change this value to match the
    // appropriate service name to be used in your environment
    private static final String SERVICE_NAME = "ELEKTRON_DD";
    
    // If the Data Access Control System (DACS) is activated on your TREP 
    // and if your DACS username is different than your operating system user 
    // name, you may need to hardcode your DACS user name in this application.
    // To do so, you just have to set it in the following field. 
    // Note: DACS user names are usualy provided by the TREP administration 
    // team of your company. 
    private static final String DACS_USER_NAME = "";

    // Indicate is MarketPrice objects must dispatch EMA events themselves or
    // not when they are built using the synchronous mode.
    private static final boolean AUTO_DISPATCH = true;

    // The OmmConsumer used to request the chains
    private static OmmConsumer ommConsumer;
    private static int operationModel = OmmConsumerConfig.OperationModel.USER_DISPATCH;

    // The OmmConsumer dispatcher
    private static Dispatcher dispatcher;    
    

    public static void main(String[] args)
    {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("|                                                                             |");
        System.out.println("|            Chain value add object for EMA - Example application             |");
        System.out.println("|                                                                             |");
        System.out.println("| This example application illustrates the concepts explained in the \"Simple |");
        System.out.println("| Chain objects for EMA\" article published on the Thomson Reuters Developer  |");
        System.out.println("| Portal portal. More specifically, this application demonstrates how to use  |");        
        System.out.println("| the value add chain objects that implements the different concepts,         |");
        System.out.println("| algorithms and optimizations described in the article.                      |");
        System.out.println("| The value add chain objects for EMA are exposed by the com.thomsonreuters   |");
        System.out.println("| .platformservices.elektron.objects.chain package of the                     |");
        System.out.println("| ValueAddObjectsForEMA example library.                                      |");
        System.out.println("| The application starts by creating an EMA OmmConsumer and uses it with the  |");
        System.out.println("| chain value add objects to expand a number of different chains,             |");
        System.out.println("| demonstrating the implemented capabilities. Chain examples are executed one |");
        System.out.println("| by one in 12 individual steps.  Before each step, explanatory text is       |");
        System.out.println("| displayed and you are prompted to press <Enter> to start the step.          |");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println();

        // Steps using an OmmConsumer configured with the USER_DISPATCH OperationModel
        {
            createOmmConsumer(operationModel);

            // Dispatcher used by the Steps to dispatch events from the main thread
            dispatcher = new Dispatcher.Builder()
                    .withOmmConsumer(ommConsumer)
                    .build();

            openAChainAndDisplayElementNamesWhenFinished_1();  
            openAChainAndDisplayElementNamesWhenFinished_2();
            openAChainAndDisplayElementsNamesAsSoonAsTheyAreDetected();
            openAChainAndSkipSummaryLinks();
            openAVeryLongChain();
            openAVeryLongChainWithTheOptimizedAlgorithm();
            openChainWithUpdates();
            openARecursiveChain();
            openARecursiveChainWithMaxDepth();
            openAChainThatDoesntExist();
            openAChain_SynchronousMode_UserDispatch();

            uninitializeOmmConsumer();
            dispatcher = null;
        }
        
        // Steps using an OmmConsumer configured with the API_DISPATCH OperationModel
        // No Dispatcher is required as it's an EMA thread that dispatches events
        {
            createOmmConsumer(OperationModel.API_DISPATCH);
            
            openAChain_SynchronousMode_ApiDispatch();
            
            uninitializeOmmConsumer();
        }
        
 
        System.out.println("  >>> Exiting the application");
    }

    private static void openAChainAndDisplayElementNamesWhenFinished_1()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 1/12 - openAChainAndDisplayElementNamesWhenFinished_1()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the Dow Jones chain. When the chain decoding is");
        System.out.println("  . completed we display the names of all elements that constitute this chain.");
        System.out.println("  . We also display errors if any. In order to determine if the chain is");
        System.out.println("  . complete we poll the isComplete() method at regular intervals (after each");
        System.out.println("  . OMM consumer dispatch). In the next step we will see another technique to");
        System.out.println("  . detect chains completion.");
        System.out.println();              
        waitForKeyPress();
               
        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#.DJI")
                .withServiceName(SERVICE_NAME)
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        // Display the chain elements after the dispatch loop exited because the 
        // chain is complete.   
        theChain.getElements().forEach(
                (position, name) ->
                        System.out.println("\t" + theChain.getName() + "[" + position + "] = " + name)
        );
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }
        
    private static void openAChainAndDisplayElementNamesWhenFinished_2()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 2/12 - openAChainAndDisplayElementNamesWhenFinished_2()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the Dow Jones chain and display its elements names");
        System.out.println("  . when the chain is complete. This step displays the exact same information");
        System.out.println("  . than the previous one, but this time we use another technique to detect the");
        System.out.println("  . chain’s completion: we leverage the ChainCompleteFunction that is called as");
        System.out.println("  . soon as the chain is complete (no is polling required).");
        System.out.println();              
        waitForKeyPress();

        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#.DJI")
                .withServiceName(SERVICE_NAME)
                .onComplete(
                        (chain) -> 
                                chain.getElements().forEach(
                                        (position, name) -> 
                                                System.out.println("\t" + chain.getName() + "[" + position + "] = " + name)
                                )
                )
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }
    
    private static void openAChainAndDisplayElementsNamesAsSoonAsTheyAreDetected()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 3/12 - openAChainAndDisplayElementsNamesAsSoonAsTheyAreDetected()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the Dow Jones chain and display the name of new");
        System.out.println("  . elements as soon as they are detected by the decoding algorithm. To this");
        System.out.println("  . aim we leverage the ElementAddedFunction functional interface.");
        System.out.println();              
        waitForKeyPress();

        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#.DJI")
                .withServiceName(SERVICE_NAME)
                .onElementAdded(
                        (position, name, chain) -> 
                                System.out.println("\tElement added to <" + chain.getName() + "> at position " + position + ": " + name)
                )
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }
    
    private static void openAChainAndSkipSummaryLinks()
    {
                
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 4/12 - openAChainAndSkipSummaryLinks()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the Dow Jones chain once again, but this time we skip");
        System.out.println("  . the summary links. As the Dow Jones chain has one summary link, the chain ");
        System.out.println("  . will be made of 30 elements instead of 31. The number of summary links may");
        System.out.println("  . be different for other chains and depends on the display template used by");
        System.out.println("  . the chain. For example, it's 2 for the British FTSE 100 (0#.FTSE), 6 for");
        System.out.println("  . the Italian FTSE 100 (0#.FTMIB) and 6 for the French CAC40 (0#.FCHI).");
        System.out.println("  . The SummaryLinksToSkip object used in this method is setup for these 4");
        System.out.println("  . cases.");
        System.out.println();              
        waitForKeyPress();

        SummaryLinksToSkipByDisplayTemplate summaryLinksToSkip = new SummaryLinksToSkipByDisplayTemplate.Builder()
                .forDisplayTemplate(187).skip(1)  // e.g. 0#.DJI
                .forDisplayTemplate(205).skip(2)  // e.g. 0#.FTSE
                .forDisplayTemplate(1792).skip(6) // e.g. 0#.FTMIB
                .forDisplayTemplate(1098).skip(6) // e.g. 0#.FCHI
                .build();
        
        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#.DJI")
                .withServiceName(SERVICE_NAME)
                .withSummaryLinksToSkip(summaryLinksToSkip)
                .onComplete(
                        (chain) -> chain.getElements().forEach(
                                (position, name) -> 
                                        System.out.println("\t" + chain.getName() + "[" + position + "] = " + name)
                        )
                )
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }    
    
    private static void openAVeryLongChain()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 5/12 - openAVeryLongChain()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the NASDAQ BASIC chain that contains more than 8000 ");
        System.out.println("  . elements. This kind of chain may take more than 20 seconds to open with the");
        System.out.println("  . normal decoding algorithm. For easier comparison with the optimized");
        System.out.println("  . algorithm, the time spent to decode the chain is displayed.");
        System.out.println();              
        waitForKeyPress();

        LocalDateTime startTime = LocalDateTime.now();
        
        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#UNIVERSE.NB")
                .withServiceName(SERVICE_NAME)
                .onElementAdded(
                        (position, name, chain) -> 
                        {
                            if(position % 100 == 0)
                                System.out.println("\t" + (position+1) + " element(s) decoded for <" + chain.getName() + ">. Latest: " + name);
                        }
                )
                .onComplete(
                        (chain) ->
                        {
                            long expansionTimeInSeconds = startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
                            System.out.println("\tChain <" + chain.getName() + "> contains " + chain.getElements().size() + " elements and opened in "+ expansionTimeInSeconds + " seconds.");
                        }
                )
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }    
    
    private static void openAVeryLongChainWithTheOptimizedAlgorithm()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 6/12 - openAVeryLongChainWithTheOptimizedAlgorithm()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the NASDAQ BASIC chain with the optimized decoding");
        System.out.println("  . algorithm. You should observe much better performance than with the normal");
        System.out.println("  . algorithm.");
        System.out.println();              
        waitForKeyPress();

        LocalDateTime startTime = LocalDateTime.now();
        
        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#UNIVERSE.NB")
                .withServiceName(SERVICE_NAME)
                .withNameGuessingOptimization(50)
                .onElementAdded(
                        (position, name, chain) -> 
                        {
                            if(position % 100 == 0)
                                System.out.println("\t" + (position+1) + " element(s) decoded for <" + chain.getName() + ">. Latest: " + name);
                        }
                )
                .onComplete(
                        (chain) ->
                        {
                            long expansionTimeInSeconds = startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
                            System.out.println("\tChain <" + chain.getName() + "> contains " + chain.getElements().size() + " elements and opened in "+ expansionTimeInSeconds + " seconds.");
                        }
                )
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }        
    
    private static void openChainWithUpdates()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 7/12 - openChainWithUpdates()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the \"NYSE Active Volume leaders\" tile (.AV.O), this");
        System.out.println("  . type of chain that updates very frequently. Tiles follow the same naming");
        System.out.println("  . convention than classical chains, except for the name of their first chain");
        System.out.println("  . record that doesn't start by \"0#\". This example leverages the");
        System.out.println("  . ElementAddedFunction, ElementChangedFunction and ElementRemovedFunction");
        System.out.println("  . functional interfaces to display chain changes. For this step, EMA events");
        System.out.println("  . are displayed for 2 minutes. In order to help you visualizing the changes");
        System.out.println("  . that happened to the chain, the complete list of chain elements is");
        System.out.println("  . displayed when the chain is complete and just before it is closed, after");
        System.out.println("  . the 2 minutes wait. If this step is executed when the NYSE is opened, you");
        System.out.println("  . should observe changes in the chain.");
        System.out.println();              
        waitForKeyPress();

        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName(".AV.O")
                .withServiceName(SERVICE_NAME)
                .withUpdates(true)
                .onElementAdded(
                        (position, name, chain) ->
                                System.out.println("\tElement added to <" + chain.getName() + "> at position " + position + ": " + name)
                )
                
                .onElementRemoved(
                        (position, chain) -> 
                                System.out.println("\tElement removed from <" + chain.getName() + "> at position " + position)
                )
                .onElementChanged(
                        (position, previousName, newName, chain) -> 
                        {
                            System.out.println("\tElement changed in <" + chain.getName() + "> at position " + position);
                            System.out.println("\t\tPrevious name: " + previousName + " New name: " + newName);
                        }
                )               
                .onComplete(
                        (chain) -> 
                        {
                            System.out.println("\n\tThe chain is complete and contains the following elements:");
                            chain.getElements().forEach(
                                    (position, name) ->
                                            System.out.println("\t\t" + chain.getName() + "[" + position + "] = " + name)
                            );
                            System.out.println("\tWaiting for updates...\n");
                        }
                )                
                .onError(
                        (errorMessage, chain) ->
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsFor(Dispatcher.TWO_MINUTES);
        
        {// Prints the chain after 2 minutes
            System.out.println("\n\tThe chain is about to be closed. It now contains the following elements:");
            theChain.getElements().forEach(
                    (position, name) -> 
                            System.out.println("\t\t" + theChain.getName() + "[" + position + "] = " + name)
            );
            System.out.println();
        }        
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }    
    
    private static void openARecursiveChain()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 8/12 - openARecursiveChain()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we open the chain for the Equity Japanese Contracts (0#JP-EQ).");
        System.out.println("  . This chain contains elements that are also chains. In this step we use a ");
        System.out.println("  . RecursivesChain object to open all elements of this chain of chains ");
        System.out.println("  . recursively. With recursive chains, the position is represented by a list");
        System.out.println("  . of numbers (Each number representing a position at a given depth).");
        System.out.println("  . The element name is made of a list of strings (Each string representing");
        System.out.println("  . the name of the element at a given level).");
        System.out.println();              
        waitForKeyPress();
        
        RecursiveChain theChain = new RecursiveChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#JP-EQ")
                .withServiceName(SERVICE_NAME)
                .onComplete(
                        chain ->
                                chain.getElements().forEach(
                                    (position, name) ->
                                        System.out.println("\t" + chain.getName() + position + " = " + name)
                                )
                )
                .onError(
                        (errorMessage, chain) ->  
                        {
                            if(!chain.isAChain())
                            {
                                System.out.println("<" + chain.getName() + "> is not a chain");
                            }
                            System.out.println("Error received for <" + chain.getName() + ">: " + errorMessage);
                        }
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }

    private static void openARecursiveChainWithMaxDepth()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 9/12 - openARecursiveChainWithMaxDepth()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we recursively open the chain for the Equity Japanese");
        System.out.println("  . Contracts (0#JP-EQ) and we limit the recursion depth to 2 levels.");
        System.out.println();              
        waitForKeyPress();
        
        RecursiveChain theChain = new RecursiveChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#JP-EQ")
                .withServiceName(SERVICE_NAME)
                .withMaxDepth(2)
                .onComplete(
                        chain ->
                                chain.getElements().forEach(
                                    (position, name) ->
                                        System.out.println("\t" + chain.getName() + position + " = " + name)
                                )
                )
                .onError(
                        (errorMessage, chain) ->  
                        {
                            if(!chain.isAChain())
                            {
                                System.out.println("<" + chain.getName() + "> is not a chain");
                            }
                            System.out.println("Error received for <" + chain.getName() + ">: " + errorMessage);
                        }
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }
    
    
    private static void openAChainThatDoesntExist()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 10/12 - openAChainThatDoesntExist()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step we try to open a chain that doesn't exist and display the ");
        System.out.println("  . error detected by the decoding algorithm.");
        System.out.println();              
        waitForKeyPress();

        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("THIS_CHAIN_DOESNT_EXIST")
                .withServiceName(SERVICE_NAME)
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        dispatchEventsUntilIsComplete(theChain);
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }
    
    private static void openAChain_SynchronousMode_UserDispatch()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 11/12 - openAChain_SynchronousMode_UserDispatch()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step, we show how to open a Chain in synchronous mode. In");
        System.out.println("  . this mode, the open() method is blocking and only returns once the");
        System.out.println("  . Chain is complete.");
        System.out.println("  . For the purpose of this demonstration, we build a FlatChain with the");
        System.out.println("  . SynchronousMode activated and, as soon as the open() method returns, we");
        System.out.println("  . display the names of all elements that constitute this chain..");
        System.out.println("  . It is important to note that the OmmConsumer object used for this step");
        System.out.println("  . has been built using the USER_DISPATCH EMA operation model. For this reason");
        System.out.println("  . the FlatChain object is built with the synchronous mode activated but ");
        System.out.println("  . also with the autodispatch parameter set to true. Thanks to this parameter");
        System.out.println("  . the open method of the FlatChain will dispatch EMA events until it is complete.");
        System.out.println();              
        waitForKeyPress();
               
        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#.DJI")
                .withServiceName(SERVICE_NAME)
                .withSynchronousMode(AUTO_DISPATCH)
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        // Display the chain elements after the dispatch loop exited because the 
        // chain is complete.   
        theChain.getElements().forEach(
                (position, name) ->
                        System.out.println("\t" + theChain.getName() + "[" + position + "] = " + name)
        );
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }    
    
    
    private static void openAChain_SynchronousMode_ApiDispatch()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  . 12/12 - openAChain_SynchronousMode_UserDispatch()");
        System.out.println("  ..............................................................................");
        System.out.println("  . In this step, we show how to open a Chain in synchronous mode. In");
        System.out.println("  . this mode, the open() method is blocking and only returns once the");
        System.out.println("  . Chain is complete.");
        System.out.println("  . For the purpose of this demonstration, we build a FlatChain with the");
        System.out.println("  . SynchronousMode activated and, as soon as the open() method returns, we");
        System.out.println("  . display the names of all elements that constitute this chain..");
        System.out.println("  . It is important to note that the OmmConsumer object used for this step");
        System.out.println("  . has been built using the API_DISPATCH EMA operation model. For this reason");
        System.out.println("  . the FlatChain object is built with the synchronous mode activated but ");
        System.out.println("  . also with the autodispatch parameter set to false. Thanks to this parameter");
        System.out.println("  . the open method of the FlatChain will not dispatch EMA events but sleep ");
        System.out.println("  . instead until it is complete.");
        System.out.println();              
        waitForKeyPress();
               
        FlatChain theChain = new FlatChain.Builder()
                .withOmmConsumer(ommConsumer)
                .withChainName("0#.DJI")
                .withServiceName(SERVICE_NAME)
                .withSynchronousMode(!AUTO_DISPATCH)
                .onError(
                        (errorMessage, chain) -> 
                                System.out.println("\tError received for <" + chain.getName() + ">: " + errorMessage)
                )
                .build();
        
        System.out.println("    >>> Opening <" + theChain.getName() + ">");
        theChain.open();                                    
        
        // Display the chain elements after the dispatch loop exited because the 
        // chain is complete.   
        theChain.getElements().forEach(
                (position, name) ->
                        System.out.println("\t" + theChain.getName() + "[" + position + "] = " + name)
        );
        
        System.out.println("    >>> Closing <" + theChain.getName() + ">");
        theChain.close();        
    }    
    
    /**
     * Creates the <code>OmmConsumer</code> used by the different steps of this 
     * example application. This method only sets the operation model and user 
     * name used by the OmmConsumer. Other parameters must be set via the 
     * EmaConfig.xml configuration file that comes with this application.
     * @param operationModel the EMA operation model the OmmConsumer should use.
     * It can be either <code>OperationModel.API_DISPATCH</code> or
     * <code>OperationModel.USER_DISPATCH</code>
     * @return the created <code>OmmConsumer</code>.
     */     
    private static void createOmmConsumer(int operationModel)
    {
        String operationModelName;
        if(operationModel == OperationModel.API_DISPATCH)
        {
            operationModelName = "API_DISPATCH";
        }
        else
        {
            operationModelName = "USER_DISPATCH";
        }
        
        out.println();
        out.println("  .............................................................................");
        out.println("  >>> Creating the OmmConsumer ("+ operationModelName +")");
        
        if(ommConsumer != null)
            return;
        
        ChainStepByStepExample.operationModel = operationModel;
        
        try
        {
            OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig()
                    .operationModel(operationModel);
            
            if(!DACS_USER_NAME.isEmpty())
            {
                config.username(DACS_USER_NAME);
            }
            
            ommConsumer = EmaFactory.createOmmConsumer(config);
        } 
        catch (OmmException exception)
        {
            out.println("      ERROR - Can't create the OmmConsumer because of the following error: " + exception.getMessage());
            out.println("  >>> Exiting the application");
            exit(-1);
        }                       
    }    
    
    private static void uninitializeOmmConsumer()
    {
        System.out.println();
        System.out.println("  ..............................................................................");
        System.out.println("  >>> Uninitializing the OmmConsumer");

        if(ommConsumer != null)
        {
            ommConsumer.uninitialize();        
            ommConsumer = null;
        }
    }

    /**
     * Dispatch events until the theMarketPrice given in parameter is complete. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This call is blocking until the object is complete.
     * @param completable the object that must be completed.
     */    
    private static void dispatchEventsUntilIsComplete(Chain theChain)
    {
        if(operationModel == OmmConsumerConfig.OperationModel.API_DISPATCH)
        {
            System.err.println("    >>> Cannot dispatch an OmmConsumer configured with the API_DISPATCH OperationModel");
            return;
        }
     
        try
        {
            out.println("    >>> Dispatching events until <" + theChain.getName() + "> is complete");

            // This method uses the dispatchEventsUntilComplete() method of a
            // Dispatcher provided with the MarketPrice library. You can either
            // use this Dispatcher method or implement the dispatching loop 
            // yourself. The implementation is quite simple and looks like this:
            // 
            //     do
            //     {
            //         ommConsumer.dispatch(DISPATCH_TIMEOUT_IN_MS); // DISPATCH_TIMEOUT_IN_MS = 200ms
            //     } 
            //     while (!theMarketPrice.isComplete());   
            //
            dispatcher.dispatchEventsUntilComplete(theChain);
            
            out.println("    >>> Finished dispatching events. <" + theChain.getName() + "> is complete.");
        } 
        catch (OmmException exception)
        {
            out.println("      ERROR - OmmConsumer event dispatching failed: " + exception.getMessage());
            out.println("  >>> Exiting the application");
            exit(-1);
        }                
    }
    
    /**
     * Dispatch events until the duration given in parameter is expired. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This call is blocking until the indicated duration expires.
     * @param durationInSeconds the duration in seconds.
     */    
    private static void dispatchEventsFor(int durationInSeconds)
    {                
        if(ommConsumer == null)
        {
            System.err.println("    >>> Cannot dispatch. The OmmConsumer has not been created.");
            return;
        }

        if(operationModel == OmmConsumerConfig.OperationModel.API_DISPATCH)
        {
            System.err.println("    >>> Cannot dispatch an OmmConsumer configured with the API_DISPATCH OperationModel");
            return;
        }
        
        try
        {
            out.println("    >>> Dispatching events for " + durationInSeconds + " seconds");
            
            // This method uses the dispatchEventsUntilTimeElapsed() method of a
            // Dispatcher provided with the MarketPrice library. 
            dispatcher.dispatchEventsUntilTimeElapsed(durationInSeconds);          
            
            out.println("    >>> Finished dispatching events after " + durationInSeconds + " seconds");        
        } 
        catch (OmmException exception)
        {
            out.println("      ERROR - OmmConsumer event dispatching failed: " + exception.getMessage());
            out.println("  >>> Exiting the application");
            exit(-1);
        }
        
    }
     
    /**
     * Dispatch events until the user presses &lt;Enter&gt; in the console. 
     * <br>
     * <br>
     * <strong>Note:</strong> Events are dispatched from the thread that calls 
     * this method. This call is blocking until the user presses &lt;Enter&gt;.
     */   
    private static void waitForKeyPressWhileDispatchingEvents()
    { 
        out.println("    <<< Press <Enter> to continue...");
        try
        {
            // This method uses the dispatchEventsUntilKeyPressed() method of a
            // Dispatcher provided with the MarketPrice library.             
            dispatcher.dispatchEventsUntilKeyPressed();
        }  
        catch(OmmException exception)
        {
            out.println("      ERROR - OmmConsumer event dispatching failed: " + exception.getMessage());
            out.println("  >>> Exiting the application");
            exit(-1);
        } 
    }    
    
    /**
     * Wait until the user to presses &lt;Enter&gt; in the console. 
     */  
    private static void waitForKeyPress()
    { 
        out.println("    <<< Press <Enter> to continue...");
    
        try
        {
            // Wait until characters are available
            do
            {
                Thread.sleep(200);
            } 
            while (System.in.available() <= 0);
            
            // Read and discard all available characters
            do
            {
                System.in.read();
            }
            while (System.in.available() > 0);
        }  
        catch (IOException | InterruptedException exception) 
        {}  
    }    

    /**
     * Sleep for the duration given in parameter. 
     * @param durationInSeconds the duration in seconds.
     */ 
    private static void sleep(int durationInSeconds)
    { 
        out.println("    <<< Sleeping for " + durationInSeconds + " seconds...");
        try
        {
            Thread.sleep(durationInSeconds * 1000);
        }  
        catch (InterruptedException exception) 
        {}  
    }    
    
    /**
     * Return the name of the current thread. 
     * @return the name of the current thread.
     */
    private static String currentThread()
    {
        return " (Thread: " + Thread.currentThread().getName() + ")";
    }    
}
