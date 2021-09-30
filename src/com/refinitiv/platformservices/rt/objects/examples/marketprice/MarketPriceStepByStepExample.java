/*
 * Copyright 2017 Thomson Reuters
 *
 * DISCLAIMER: This source code has been written by Thomson Reuters for the only 
 * purpose of illustrating the "A simple MarketPrice object" article published on
 * the Thomson Reuters Developer Community. It has not been tested for a usage in 
 * production environments.
 *
 * Thomson Reuters Developer Community: https://developers.thomsonreuters.com
 * A simple MarketPrice object - Part 1: https://developers.thomsonreuters.com/article/simple-marketprice-object-part-1
 * A simple MarketPrice object - Part 2: https://developers.thomsonreuters.com/article/simple-marketprice-object-part-2
 *
 */
package com.refinitiv.platformservices.rt.objects.examples.marketprice;

import com.refinitiv.platformservices.rt.objects.marketprice.MarketPrice;
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.ema.access.OmmConsumerConfig;
import com.refinitiv.ema.access.OmmConsumerConfig.OperationModel;
import com.refinitiv.ema.access.OmmException;
import com.refinitiv.platformservices.rt.objects.common.Dispatcher;
import com.refinitiv.platformservices.rt.objects.data.Field;
import java.io.IOException;
import static java.lang.System.exit;
import static java.lang.System.out;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

class MarketPriceStepByStepExample
{
    // TREP or Elektron Service name used request MarketPrice instruments
    // IMPORTANT NOTE:  You may need to change this value to match the
    // appropriate service name to be used in your environment
    private static final String SERVICE_NAME = "ELEKTRON_EDGE";
    
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

    // The OmmConsumer used to request the MarketPrice
    private static OmmConsumer ommConsumer;
    private static int operationModel = OperationModel.API_DISPATCH;

    // The OmmConsumer dispatcher
    private static Dispatcher dispatcher;    

    /**
     * Main method. 
     */      
    public static void main(String[] args)
    {
        out.println("-------------------------------------------------------------------------------");
        out.println("|                                                                             |");
        out.println("|         MarketPrice value add object for EMA - Example application          |");
        out.println("|                                                                             |");
        out.println("| This example application illustrates the concepts explained in the          |");
        out.println("| \"A simple MarketPrice object or how to consume real-time Level1 data?\"      |");
        out.println("| article published on the Thomson Reuters Developer Portal.                  |");
        out.println("| More specifically, this application demonstrates how to use the value add   |");        
        out.println("| MarketPrice class that implements these concepts.                           |");
        out.println("|                                                                             |");
        out.println("| The application starts by creating an EMA OmmConsumer and then uses it with |");
        out.println("| the different MarketPrice objects to subscribe to Level 1 real-time         |");
        out.println("| instruments, demonstrating the implemented capabilities. These MarketPrice  |");
        out.println("| examples are run in 17 individual steps. Before each step, explanatory text |");
        out.println("| is displayed and you are prompted to press <Enter> to start the step.       |");
        out.println("-------------------------------------------------------------------------------");
        out.println();
        
        // Steps using an OmmConsumer configured with the USER_DISPATCH OperationModel
        {
            createOmmConsumer(OperationModel.USER_DISPATCH);
            
            // Dispatcher used by the Steps to dispatch events from the main thread
            dispatcher = new Dispatcher.Builder()
                    .withOmmConsumer(ommConsumer)
                    .build();
            
            openAMarketPrice_And_DisplayFieldsWhenComplete();
            openAMarketPrice_And_DisplayFieldsOnComplete();
            openAMarketPrice_And_DisplayFieldsOnImage();
            openAMarketPrice_And_DisplayFieldsOnUpdate();
            openAMarketPrice_And_DisplayStatusWhenIsComplete();
            openAMarketPrice_And_DisplayStatusOnStatus();
            openAMarketPrice_And_DisplayFieldsByName();
            openAMarketPrice_And_DisplayFieldsById();
            openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds();
            openAMarketPriceWithoutUpdates_And_DisplayFieldsOnImage();
            openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_UserDispatch();
            openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_UserDispatch();
            openAMarkePriceWithPartialUpdates();
            openAMarkePriceWithAViewDefinedWithFieldIds();
            // openAMarkePriceWithAViewDefinedWithFieldNames();
            
            uninitializeOmmConsumer();
            dispatcher = null;
        }

        // Steps using an OmmConsumer configured with the API_DISPATCH OperationModel
        // No Dispatcher is required as it's an EMA thread that dispatches events
        {
            createOmmConsumer(OperationModel.API_DISPATCH);
            
            openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_ApiDispatch();
            openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_ApiDispatch();
            
            uninitializeOmmConsumer();
        }

        out.println("  >>> Exiting the application");

    }

    /**
     * Example step 1 
     */          
    private static void openAMarketPrice_And_DisplayFieldsWhenComplete()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 1/17 - openAMarketPrice_And_DisplayFieldsWhenComplete()");
        out.println("  .............................................................................");
        out.println("  . In this step we demonstrate how to use a MarketPrice to open the Level 1");
        out.println("  . instrument \"EUR=\". We also demonstrate how to wait for the instrument");
        out.println("  . completion by polling the isComplete() method. Once completed, the fields");
        out.println("  . of the MarketPrice are retrieved using getFields() and displayed on the");
        out.println("  . console.");
        out.println();
        
        // Wait for the user to press <Enter> before we start the step
        waitForKeyPressWhileDispatchingEvents();
               
        // Setup and build the MarketPrice using a MarketPrice.Builder 
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .build();
        
        // Open the MarketPrice
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        // Wait for the MarketPrice to be complete
        // This is where the polling is done. See the method definition for more details.
        dispatchEventsUntilIsComplete(theMarketPrice); 
        
        // Retreive and display the fields
        out.println("        marketPrice.getFields() returned: ");
        print(theMarketPrice.getFields());
        
        // Close the Marketprice
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }
        
            
    private static void openAMarketPrice_And_DisplayFieldsOnComplete()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 2/17 - openAMarketPrice_And_DisplayFieldsOnComplete()");
        out.println("  .............................................................................");
        out.println("  . This step is very similar to the previous one, except that this time we");
        out.println("  . wait for the MarketPrice completion using a lambda expression instead of");
        out.println("  . polling the isComplete() method. The lambda expression is registered at");
        out.println("  . build time and is called as soon as the MarketPrice is complete. In the");
        out.println("  . implementation of this lambda, we display the instrument's fields that we");
        out.println("  . retrieved via the getFields() method.");
        out.println("  . Note:");
        out.println("  .   - When you open a MarketPrice, the onComplete() callback is called only");
        out.println("  .     once. Either when the first RefreshMsg is received or when the first");
        out.println("  .     State is received.");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
        
               
        // This lambda expression will be called when the MarketPrice is complete
        MarketPrice.OnCompleteFunction printStateAndImage = (marketPrice) -> 
        {
            out.println("    >>> onComplete() called for <" + marketPrice.getName() + ">. " + currentThread());
            out.println("        marketPrice.getState() returned : " + marketPrice.getState());
            out.println("        marketPrice.getFields() returned: ");
            print(marketPrice.getFields());
        };
        
        MarketPrice theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .onComplete(printStateAndImage)                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsUntilIsComplete(theMarketPrice);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }

    /**
     * Example step 3 
     */    
    private static void openAMarketPrice_And_DisplayFieldsOnImage()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 3/17 - openAMarketPrice_And_DisplayFieldsOnImage()");
        out.println("  .............................................................................");
        out.println("  . In this step we demonstrate how to be notified of incoming images via an");
        out.println("  . onImage() lambda expression registered at built time.");
        out.println("  . Notes: ");
        out.println("  .   - The onImage() callback may be called several times if the publisher");
        out.println("  .     sendsseveral Refresh messages for the instrument.");
        out.println("  .   - onImage() must not be used to detect completion as this completion may");
        out.println("  .     be caused by a Status message.");
        out.println("  .   - If the completion is caused by an Refresh message then onImage() is");
        out.println("  .     called first, then onComplete() is called.");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .onImage(
                (marketPrice, image, state) -> 
                {
                    out.println("    >>> onImage() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received State: " + state);
                    out.println("        Received image: ");
                    print(image);
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsFor(Dispatcher.TWO_SECONDS);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }

    /**
     * Example step 4
     */
    private static void openAMarketPrice_And_DisplayFieldsOnUpdate()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 4/17 - openAMarketPrice_And_DisplayFieldsOnUpdate()");
        out.println("  .............................................................................");
        out.println("  . In this step we demonstrate how to be notified of incoming updates via an");
        out.println("  . onUpdate() lambda expression registered at built time. As we dispatch");
        out.println("  . events for 5 seconds, the onUpdate() callback is called each time the");
        out.println("  . MarketPrice receives a new update during this period of time.");
        out.println("  . The onUpdate() lambda we defined displays the fields of the received update");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .onUpdate(
                (marketPrice, update) -> 
                {
                    out.println("    >>> onUpdate() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received update: ");
                    print(update);
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsFor(Dispatcher.FIVE_SECONDS);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }
    
    /**
     * Example step 5
     */    
    private static void openAMarketPrice_And_DisplayStatusWhenIsComplete()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 5/17 - openAMarketPrice_And_DisplayStatusWhenIsComplete()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how to retrieve the state of a MarketPrice. As the");
        out.println("  . State is only available after completion, we poll the isComplete() method");
        out.println("  . to wait for this completion (We could have used the onComplete() callback");
        out.println("  . instead). As we opened a valid instrument, the displayed State is the one");
        out.println("  . that came with the first RefreshMsg (the image).");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsUntilIsComplete(theMarketPrice);
        
        out.println("    >>> Current State of <" + theMarketPrice.getName() + ">: " + theMarketPrice.getState());
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();    
    }
    
    /**
     * Example step 6
     */    
    private static void openAMarketPrice_And_DisplayStatusOnStatus()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 6/17 - openAMarketPrice_And_DisplayStatusOnStatus()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how to be notified of incoming States individually ");
        out.println("  . sent (not within a Refresh Message) by the Platform. This may happen when");
        out.println("  . an error occurs for example. To this aim, we registered an onState()");
        out.println("  . lambda expression and opened a MarketPrice that we setup with the name of");
        out.println("  . an inexistent instrument. As the instrument doesn't exist, we expect the");
        out.println("  . lambda to be called with a \"Closed / Suspect / Not found\" State.");
        out.println("  . Notes:"); 
        out.println("  .   - Once the State received, the MarketPrice is considered complete.");
        out.println("  .   - If you also registered an onComplete() callback, the onState() callback");
        out.println("  .     is called first. Then onComplete() is called.");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("UNKNOWN_RIC")
            .withServiceName(SERVICE_NAME)
            .onState(
                (marketPrice, state) -> 
                {
                    out.println("    >>> onState() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received State: " + state);
                }
            )
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsUntilIsComplete(theMarketPrice);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();  
    }
    
    /**
     * Example step 7
     */    
    private static void openAMarketPrice_And_DisplayFieldsByName()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 7/17 - openAMarketPrice_And_DisplayFieldsByName()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how to retrieve a field by its name. This is an.");
        out.println("  . alternative to the fields collection iteration that you can use when your");
        out.println("  . application knows the names of the fields it needs. In this examples,");
        out.println("  . fields are retrieved in the onImage() lambda expression.");
        out.println("  . Note:"); 
        out.println("  .   - If you try to retrieve a field before the MarketPrice the first image");
        out.println("  .     is received, the getField(String) method returns null.");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .onImage(
                (marketPrice, image, state) -> 
                {
                    out.println("    >>> onImage() called for <" + marketPrice.getName() + ">. " + currentThread());
                    print(marketPrice.getField("DSPLY_NAME"));
                    print(marketPrice.getField("BID"));
                    print(marketPrice.getField("ASK"));
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsUntilIsComplete(theMarketPrice);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }
    
    /**
     * Example step 8
     */    
    private static void openAMarketPrice_And_DisplayFieldsById()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 8/17 - openAMarketPrice_And_DisplayFieldsById()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how to retrieve a field by its field Id. This is an.");
        out.println("  . alternative to the fields collection iteration that you can use when your");
        out.println("  . application knows the Ids of the fields it needs. In this examples, fields");
        out.println("  .  are retrieved in the onImage() lambda expression.");
        out.println("  . Note:"); 
        out.println("  .   - If you try to retrieve a field before the MarketPrice the first image");
        out.println("  .     is received, the getField(int) method returns null.");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .onImage(
                (marketPrice, image, state) -> 
                {
                    out.println("    >>> onImage() called for <" + marketPrice.getName() + ">. " + currentThread());
                    print(marketPrice.getField(3));     // DSPLY_NAME
                    print(marketPrice.getField(22));    // BID
                    print(marketPrice.getField(25));    // ASK
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsUntilIsComplete(theMarketPrice);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }
    
    /**
     * Example step 9
     */    
    private static void openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 9/17 - openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds()");
        out.println("  .............................................................................");
        out.println("  . In this step we demonstrate the caching capability of MarketPrice objects.");
        out.println("  . If you open a MarketPrice for an instrument in streaming mode and if you ");
        out.println("  . properly dipatch the related events, the MarkePrice object is maintained ");
        out.println("  . updated with the latest publications (images,updates, states) sent by the");
        out.println("  . source of the instrument.");
        out.println("  . To demonstrate this capability, we open a MarketPrice, we wait for its");
        out.println("  . completion and we display the cached values of 3 fields (DSPLY_NAME, BID");
        out.println("  . and ASK) every 2 seconds.");
        out.println();
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsUntilIsComplete(theMarketPrice);
        
        for(int i=0; i<10; ++i)
        {
            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
            String timeStamp = formater.format(Calendar.getInstance().getTime());        
            out.println("    >>> Fields at " + timeStamp);
            
            print(theMarketPrice.getField("DSPLY_NAME"));
            print(theMarketPrice.getField("BID"));
            print(theMarketPrice.getField("ASK"));
                        
            dispatchEventsFor(Dispatcher.TWO_SECONDS);
        }
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
    }    
        
    /**
     * Example step 10
     */    
    private static void openAMarketPriceWithoutUpdates_And_DisplayFieldsOnImage() 
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 10/17 - openAMarketPriceWithoutUpdates_And_DisplayFieldsOnImage()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how to open a MarkePrice in non-streaming mode");
        out.println("  . (a.k.a snapshot mode). This mode allows to get a snapshot of the MarkePrice");
        out.println("  . fields. When opened in this mode, the MarkePrice never receive any Update");
        out.println("  . message from the Platform. The onUpdate() callback is never called and the");
        out.println("  . MarketPrice is not kept updated with the latest values published for the");
        out.println("  . instrument.");
        out.println("  . To demonstrate this capability, we build a MarketPrice in non-streaming");
        out.println("  . mode set with onImage() and onUpdate() callback methods, we open the");
        out.println("  . MarketPrice and we dispatch events for 2 seconds.");
        out.println("  . The onImage() callback should be called once. The onUpdate() callback");
        out.println("  . should never be called.");
        out.println();    
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .withUpdates(false) // Indicates that we are not interrested in updates (non-streaming/snapshot mode)
            .onImage(           // onImage() may be called once
                (marketPrice, image, state) -> 
                {
                    out.println("    >>> onImage() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received State: " + state);                    
                    out.println("        Received image: ");
                    print(image);
                }
            )
            .onUpdate(          // onUpdate() should never be called
                (marketPrice, update) -> 
                {
                    out.println("    !!! onUpdate() should not be called !!! " + currentThread());
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsFor(Dispatcher.TWO_SECONDS);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();        
        
    }
    
    /**
     * Example step 11
     */    
    private static void openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_UserDispatch()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 11/17 - openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_UserDispatch()");
        out.println("  .............................................................................");
        out.println("  . In this step, we show how to open a MarketPrice in synchronous mode. In");
        out.println("  . this mode, the open() method is blocking and only returns once the");
        out.println("  . MarketPrice is complete.");
        out.println("  . For the purpose of this demonstration, we build a MarketPrice with the");
        out.println("  . SynchronousMode activated and, as soon as the open() method returns, we");
        out.println("  . display the received fields.");
        out.println("  . It is important to note that the OmmConsumer object used for this step");
        out.println("  . has been built using the USER_DISPATCH EMA operation model. For this reason");
        out.println("  . the MarketPrice object is built with the synchronous mode activated but ");
        out.println("  . also with the autodispatch parameter set to true. Thanks to this parameter");
        out.println("  . the open method of the MarketPrice will dispatch EMA events until an image");
        out.println("  . or a status is received from the platform.");
        out.println();   
        
        waitForKeyPressWhileDispatchingEvents();

        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .withUpdates(false)
            .withSynchronousMode(AUTO_DISPATCH)
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        out.println("    >>> Display fields just after open() returned: ");
        print(theMarketPrice.getFields());
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();            
    }
    
    /**
     * Example step 12
     */    
    private static void openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_UserDispatch()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 12/17 - openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_UserDispatch()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates that even if you built the MarkePrice with the");
        out.println("  . synchronous mode activated, you continue to benefit from the automatic");
        out.println("  . update mechanism of the cached image.");
        out.println("  . For the purpose of this demonstration, we build a MarketPrice with the");
        out.println("  . SynchronousMode activated and, as soon as the open() method returns, we");
        out.println("  . display the DSPLY_NAME,the BID and the ASK fields every 2 seconds.");
        out.println("  . It is important to note that the OmmConsumer object used for this step");
        out.println("  . has been built using the USER_DISPATCH EMA operation model. For this reason");
        out.println("  . the MarketPrice object is built with the synchronous mode activated but ");
        out.println("  . also with the autodispatch parameter set to true. Thanks to this parameter");
        out.println("  . the open method of the MarketPrice will dispatch EMA events until an image");
        out.println("  . or a status is received from the platform.");
        out.println();   
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .withSynchronousMode(AUTO_DISPATCH)
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();        
        
        out.println("    >>> Display fields every 2 seconds just after open() returned: ");
        int i=0;
        do
        {
            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
            String timeStamp = formater.format(Calendar.getInstance().getTime());        
            out.println("    >>> Fields at " + timeStamp);
            
            print(theMarketPrice.getField("DSPLY_NAME"));
            print(theMarketPrice.getField("BID"));
            print(theMarketPrice.getField("ASK"));
            
            dispatchEventsFor(Dispatcher.TWO_SECONDS);
            
            i++;
        }
        while(i < 5);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();            
    }
    
    /**
     * Example step 13
     */    
    private static void openAMarkePriceWithPartialUpdates()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 13/17 - openAMarkePriceWithPartialUpdates()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates the automatic partial update management that comes");
        out.println("  . with MarkePrice objects.");
        out.println("  . For the purpose of this demonstration, we open the \"FXFX\" record page that");
        out.println("  . regularly publishes partial updates for its ROW64_n fields. We first open");
        out.println("  . it with the partial update management feature deactivated (default");
        out.println("  . behavior). Then we open the instrument with the partial update management");
        out.println("  . activated. You should notice a difference between the updates received in");
        out.println("  . the two modes. When the feature is not activated, partial updates are");
        out.println("  . directly sent to the application causing incomplete update values and a");
        out.println("  . corrupt instrument image. When the feature is activated, partial updates");
        out.println("  . are applied on the cached and then sent to the application as complete");
        out.println("  . updates. This guarantees the integrity of the received updates and cached");
        out.println("  . instrument image.");
        out.println("  . Note:");
        out.println("  .   - As this feature requires additional processing resource. We recommend");
        out.println("  .     you to use it only with instruments that actually publish partial");
        out.println("  .     updates.");
        out.println();   
        
        out.println("    >>> We are about to open <FXFX> WITHOUT partial update management");
        waitForKeyPressWhileDispatchingEvents();
              
        { // Without partial update management
            MarketPrice theMarketPrice;
            theMarketPrice = new MarketPrice.Builder()
                .withOmmConsumer(ommConsumer)
                .withName("FXFX")
                .withServiceName(SERVICE_NAME)
                .onComplete(
                    (marketPrice) -> 
                    {
                        out.println("    >>> Initial image of <" + marketPrice.getName() + ">. " + currentThread());
                        print(marketPrice.getFields());
                    }
                )                                    
                .onUpdate(
                    (marketPrice, update) -> 
                    {
                        out.println("    >>> New update received for <" + marketPrice.getName() + ">. " + currentThread());
                        print(update);
                    }
                )                
                .build();

            out.println("    >>> Opening <" + theMarketPrice.getName() + "> WITHOUT partial update management");
            theMarketPrice.open();                                    

            dispatchEventsFor(Dispatcher.FIFTEEN_SECONDS);
            
            out.println("    >>> Image cached for <" + theMarketPrice.getName() + "> with Partial Updates Management DEACTIVATED. " + currentThread());
            print(theMarketPrice.getFields());
            
            out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
            theMarketPrice.close();                   
        }
        
        out.println("    >>> We are about to open <FXFX> WITH partial update management");
        waitForKeyPressWhileDispatchingEvents();

        { // With partial update management
            MarketPrice theMarketPrice;
            theMarketPrice = new MarketPrice.Builder()
                .withOmmConsumer(ommConsumer)
                .withName("FXFX")
                .withServiceName(SERVICE_NAME)
                .withPartialUpdatesManagement(true)
                .onComplete(
                    (marketPrice) -> 
                    {
                        out.println("    >>> Initial image of <" + marketPrice.getName() + ">. " + currentThread());
                        print(marketPrice.getFields());
                    }
                )                                    
                .onUpdate(
                    (marketPrice, update) -> 
                    {
                        out.println("    >>> New update received for <" + marketPrice.getName() + ">. " + currentThread());
                        print(update);
                    }
                )                
                .build();

            out.println("    >>> Opening <" + theMarketPrice.getName() + "> WITH partial update management");
            theMarketPrice.open();                                    

            dispatchEventsFor(Dispatcher.FIFTEEN_SECONDS);
            
            out.println("    >>> Image cached for <" + theMarketPrice.getName() + "> with Partial Updates Management ACTIVATED. " + currentThread());
            print(theMarketPrice.getFields());
            
            out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
            theMarketPrice.close();               
        }
    }

    /**
     * Example step 14
     */    
    private static void openAMarkePriceWithAViewDefinedWithFieldIds()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 14/17 - openAMarkePriceWithAViewDefinedWithFieldIds()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how define and open an instrument view using field");
        out.println("  . Ids. As for the other MarketPrice parameters, the view is defined using the");
        out.println("  . MarketPrice.Builder.");
        out.println("  . Once the view defined, the MarketPrice works as usual, except that it only");
        out.println("  . contains the requested fields.");
        out.println();   
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .withField(3)  // DSPLY_NAME
            .withField(22) // BID
            .withField(25) // ASK
            .onImage(
                (marketPrice, image, state) -> 
                {
                    out.println("    >>> onImage() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received State: " + state);    
                    out.println("        Received image: ");
                    print(image);
                }
            )                                
            .onUpdate(
                (marketPrice, update) -> 
                {
                    out.println("    >>> onUpdate() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received update: ");
                    print(update);
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsFor(Dispatcher.TEN_SECONDS);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();            
    }
     
    /**
     * Example step 15
     */    
    private static void openAMarkePriceWithAViewDefinedWithFieldNames()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 15/17 - openAMarkePriceWithAViewDefinedWithFieldNames()");
        out.println("  .............................................................................");
        out.println("  . This step demonstrates how define and open an instrument view using field");
        out.println("  . names. As for the other MarketPrice parameters, the view is defined using the");
        out.println("  . MarketPrice.Builder.");
        out.println("  . Once the view defined, the MarketPrice works as usual, except that it only");
        out.println("  . contains the requested fields.");
        out.println();   
        
        waitForKeyPressWhileDispatchingEvents();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            //.withField("DSPLY_NAME")
            .withField("BID")
            .withField("ASK")
            .onImage(
                (marketPrice, image, state) -> 
                {
                    out.println("    >>> onImage() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received State: " + state);    
                    out.println("        Received image: ");
                    print(image);
                }
            )                                
            .onUpdate(
                (marketPrice, update) -> 
                {
                    out.println("    >>> onUpdate() called for <" + marketPrice.getName() + ">. " + currentThread());
                    out.println("        Received update: ");
                    print(update);
                }
            )                
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        dispatchEventsFor(Dispatcher.TEN_SECONDS);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();            
    }
    
    /**
     * Example step 16
     */    
    private static void openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_ApiDispatch()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 16/17 - openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_ApiDispatch()");
        out.println("  .............................................................................");
        out.println("  . This step the same than step 11 but with an OmmConsumer built using the ");
        out.println("  . API_DISPATCH operation model instead of USER_DISPATCH.");
        out.println("  .");
        out.println("  . In this step, we show how to open a MarketPrice in synchronous mode. In");
        out.println("  . this mode, the open() method is blocking and only returns once the");
        out.println("  . MarketPrice is complete.");
        out.println("  . For the purpose of this demonstration, we build a MarketPrice with the");
        out.println("  . SynchronousMode activated and, as soon as the open() method returns, we");
        out.println("  . display the received fields.");
        out.println("  . It is important to note that the OmmConsumer object used for this step");
        out.println("  . has been built using the API_DISPATCH EMA operation model. For this reason");
        out.println("  . the MarketPrice object is built with the synchronous mode activated but ");
        out.println("  . also with the autodispatch parameter set to false. Because of this parameter");
        out.println("  . the open method of the MarketPrice will not dispatch EMA events but sleep ");
        out.println("  . instead until an image or a status is received from the platform.");
        out.println();   
        out.println();   
        
        waitForKeyPress();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .withUpdates(false)
            .withSynchronousMode(!AUTO_DISPATCH)
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();                                    
        
        out.println("    >>> Display fields just after open() returned: ");
        print(theMarketPrice.getFields());
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();            
    }
    
    /**
     * Example step 17
     */    
    private static void openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_ApiDispatch()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  . 17/17 - openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_ApiDispatch()");
        out.println("  .............................................................................");
        out.println("  . This step the same than step 12 but with an OmmConsumer built using the ");
        out.println("  . API_DISPATCH operation model instead of USER_DISPATCH.");
        out.println("  .");
        out.println("  . This step demonstrates that even if you built the MarkePrice with the");
        out.println("  . synchronous mode activated, you continue to benefit from the automatic");
        out.println("  . update mechanism of the cached image.");
        out.println("  . For the purpose of this demonstration, we build a MarketPrice with the");
        out.println("  . SynchronousMode activated and, as soon as the open() method returns, we");
        out.println("  . display the DSPLY_NAME,the BID and the ASK fields every 2 seconds.");
        out.println("  . It is important to note that the OmmConsumer object used for this step");
        out.println("  . has been built using the API_DISPATCH EMA operation model. For this reason");
        out.println("  . the MarketPrice object is built with the synchronous mode activated but ");
        out.println("  . also with the autodispatch parameter set to false. Because of this parameter");
        out.println("  . the open method of the MarketPrice will not dispatch EMA events but sleep ");
        out.println("  . instead until an image or a status is received from the platform.");
        out.println();   
                
        waitForKeyPress();
               
        MarketPrice theMarketPrice;
        theMarketPrice = new MarketPrice.Builder()
            .withOmmConsumer(ommConsumer)
            .withName("EUR=")
            .withServiceName(SERVICE_NAME)
            .withSynchronousMode(!AUTO_DISPATCH)
            .build();
        
        out.println("    >>> Opening <" + theMarketPrice.getName() + ">");
        theMarketPrice.open();        
        
        out.println("    >>> Display fields every 2 seconds just after open() returned: ");
        int i=0;
        do
        {
            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
            String timeStamp = formater.format(Calendar.getInstance().getTime());        
            out.println("    >>> Fields at " + timeStamp);
            
            print(theMarketPrice.getField("DSPLY_NAME"));
            print(theMarketPrice.getField("BID"));
            print(theMarketPrice.getField("ASK"));
                        
            sleep(Dispatcher.TWO_SECONDS);           
            
            i++;
        }
        while(i < 5);
        
        out.println("    >>> Closing <" + theMarketPrice.getName() + ">");
        theMarketPrice.close();            
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
        
        MarketPriceStepByStepExample.operationModel = operationModel;
        
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
    

    /**
     * Uninitializes the <code>OmmConsumer</code> object used by this 
     * application.
     */     
    private static void uninitializeOmmConsumer()
    {
        out.println();
        out.println("  .............................................................................");
        out.println("  >>> Uninitializing the OmmConsumer");

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
    private static void dispatchEventsUntilIsComplete(MarketPrice theMarketPrice)
    {
        if(operationModel == OperationModel.API_DISPATCH)
        {
            System.err.println("    >>> Cannot dispatch an OmmConsumer configured with the API_DISPATCH OperationModel");
            return;
        }
     
        try
        {
            out.println("    >>> Dispatching events until <" + theMarketPrice.getName() + "> is complete");

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
            dispatcher.dispatchEventsUntilComplete(theMarketPrice);
            
            out.println("    >>> Finished dispatching events. <" + theMarketPrice.getName() + "> is complete.");
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

        if(operationModel == OperationModel.API_DISPATCH)
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

    /**
     * Print a collection of fields on the console. 
     * @param fields collection to print.
     */      
    private static void print(Collection<Field> fields) 
    {
       fields.forEach(
            (field) ->
                out.println("            " + field.description().acronym() + " (" + field.description().fid() + ") = " + field.value())
        );        
    }
    
    /**
     * Print a field on the console. 
     * @param fields collection to print.
     */      
    private static void print(Field field) 
    {
        out.println("        " + field.description().acronym() + "(" + field.description().fid() + ")" +" = " + field.value());
    }
}
