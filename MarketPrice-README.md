# Running the *MarketPriceStepByStepExample*

**Before you start the application** you must configure the *EmaConfig.xml file* to specify the host name of the server (the TREP or Elektron platform) to which the EMA connects. This is set thanks to the value of the *\<ChannelGroup>\<ChannelList>\<Channel>\<Host>* node. This value can be a remote host name or IP address.

To start the *MarketPriceStepByStepExample* run the *marketprice-stepbystep-example-with-esdk.x.x.x.bat* or the *marketprice-stepbystep-example-with-esdk.x.x.x.ksh* script matching the version of your Elektron SDK. These scripts depend on the *JAVA_HOME* and *ELEKTRON_JAVA_HOME* environment variables that must have been defined for the build scripts.

## Expected output

This is an example of the *MarketPriceStepByStepExample* output for each step:

    -------------------------------------------------------------------------------
    |                                                                             |
    |         MarketPrice value add object for EMA - Example application          |
    |                                                                             |
    | This example application illustrates the concepts explained in the          |
    | "A simple MarketPrice object or how to consume real-time Level1 data?"      |
    | article published on the Thomson Reuters Developer Portal.                  |
    | More specifically, this application demonstrates how to use the value add   |
    | MarketPrice class that implements these concepts.                           |
    |                                                                             |
    | The application starts by creating an EMA OmmConsumer and then uses it with |
    | the different MarketPrice objects to subscribe to Level 1 real-time         |
    | instruments, demonstrating the implemented capabilities. These MarketPrice  |
    | examples are run in 17 individual steps. Before each step, explanatory text |
    | is displayed and you are prompted to press <Enter> to start the step.       |
    -------------------------------------------------------------------------------


    .............................................................................
    >>> Creating the OmmConsumer (USER_DISPATCH)

    .............................................................................
    . 1/17 - openAMarketPrice_And_DisplayFieldsWhenComplete()
    .............................................................................
    . In this step we demonstrate how to use a MarketPrice to open the Level 1
    . instrument "EUR=". We also demonstrate how to wait for the instrument
    . completion by polling the isComplete() method. Once completed, the fields
    . of the MarketPrice are retrieved using getFields() and displayed on the
    . console.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events until <EUR=> is complete
        >>> Finished dispatching events. <EUR=> is complete.
            marketPrice.getFields() returned: 
                PROD_PERM (1) = 213
                RDNDISPLAY (2) = 153
                DSPLY_NAME (3) = NBU UZB      TAK
                TIMACT (5) = 13:10:00:000:000:000
                NETCHNG_1 (11) = -0.003
                HIGH_1 (12) = 1.1777
                LOW_1 (13) = 1.1732
                CURRENCY (15) = USD
                ACTIV_DATE (17) =  8 DEC 2017 
                OPEN_PRC (19) = 1.1771
                HST_CLOSE (21) = 1.1771
                BID (22) = 1.1741
                        .
                        .
                        .
                BID_HR_MS (14208) = 13:00:00:722:000:000
        >>> Closing <EUR=>

    .............................................................................
    . 2/17 - openAMarketPrice_And_DisplayFieldsOnComplete()
    .............................................................................
    . This step is very similar to the previous one, except that this time we
    . wait for the MarketPrice completion using a lambda expression instead of
    . polling the isComplete() method. The lambda expression is registered at
    . build time and is called as soon as the MarketPrice is complete. In the
    . implementation of this lambda, we display the instrument's fields that we
    . retrieved via the getFields() method.
    . Note:
    .   - When you open a MarketPrice, the onComplete() callback is called only
    .     once. Either when the first RefreshMsg is received or when the first
    .     State is received.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events until <EUR=> is complete
        >>> onComplete() called for <EUR=>.  (Thread: main)
            marketPrice.getState() returned : Open / Ok / None / '*All is well'
            marketPrice.getFields() returned: 
                PROD_PERM (1) = 213
                RDNDISPLAY (2) = 153
                DSPLY_NAME (3) = Commerzbank  FFT
                        .
                        .
                        .

                BID_HR_MS (14208) = 13:00:00:722:000:000
        >>> Finished dispatching events. <EUR=> is complete.
        >>> Closing <EUR=>

    .............................................................................
    . 3/17 - openAMarketPrice_And_DisplayFieldsOnImage()
    .............................................................................
    . In this step we demonstrate how to be notified of incoming images via an
    . onImage() lambda expression registered at built time.
    . Notes: 
    .   - The onImage() callback may be called several times if the publisher
    .     sendsseveral Refresh messages for the instrument.
    .   - onImage() must not be used to detect completion as this completion may
    .     be caused by a Status message.
    .   - If the completion is caused by an Refresh message then onImage() is
    .     called first, then onComplete() is called.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events for 2 seconds
        >>> onImage() called for <EUR=>.  (Thread: main)
            Received State: Open / Ok / None / '*All is well'
            Received image: 
                PROD_PERM (1) = 213
                RDNDISPLAY (2) = 153
                DSPLY_NAME (3) = BARCLAYS     LON
                        .
                        .
                        .

                BID_HR_MS (14208) = 13:00:00:722:000:000
        >>> Finished dispatching events after 2 seconds
        >>> Closing <EUR=>

    .............................................................................
    . 4/17 - openAMarketPrice_And_DisplayFieldsOnUpdate()
    .............................................................................
    . In this step we demonstrate how to be notified of incoming updates via an
    . onUpdate() lambda expression registered at built time. As we dispatch
    . events for 5 seconds, the onUpdate() callback is called each time the
    . MarketPrice receives a new update during this period of time.
    . The onUpdate() lambda we defined displays the fields of the received update

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events for 5 seconds
        >>> onUpdate() called for <EUR=>.  (Thread: main)
            Received update: 
                BID (22) = 1.1739
                PRIMACT_1 (393) = 1.1739
                ASK (25) = 1.1743
                SEC_ACT_1 (275) = 1.1743
                VALUE_DT1 (875) =  8 DEC 2017 
                VALUE_TS1 (1010) = 13:10:26:000:000:000
                TIMACT (5) = 13:10:00:000:000:000
                NETCHNG_1 (11) = -0.0032
                ACTIV_DATE (17) =  8 DEC 2017 
                ACVOL_1 (32) = 67792.0
                PCTCHNG (56) = -0.27
                BID_NET_CH (114) = -0.0032
                BID_TICK_1 (115) = �
                MID_PRICE (134) = 1.1741
                MID_NET_CH (135) = -0.0032
                NUM_BIDS (211) = 67792.0
                ACT_TP_1 (270) = B�
                CTBTR_1 (831) = BARCLAYS    
                CTB_LOC1 (836) = LON
                CTB_PAGE1 (841) = BCFX
                QUOTIM (1025) = 13:10:26:000:000:000
                QUOTE_DATE (3386) =  8 DEC 2017 
                QUOTIM_MS (3855) = 47426545
                BIDPCTCHNG (7854) = -0.27
                DSPLY_NAME (3) = BARCLAYS     LON
                OFFCL_CODE (78) =             
                BCKGRNDPAG (105) = BCFX
                ACT_TP_2 (271) = B�
                ACT_TP_3 (272) = B�
                DLG_CODE1 (826) =       
                VALUE_TS2 (1011) = 13:10:25:000:000:000
                VALUE_TS3 (1012) = 13:10:23:000:000:000
                EURO_BNC (7885) = -0.005
                US_BNC (7888) = -0.0032
        >>> onUpdate() called for <EUR=>.  (Thread: main)
            Received update: 
                EURO_NETCH (3284) = -0.005
                US_NETCH (3293) = -0.0032
        >>> onUpdate() called for <EUR=>.  (Thread: main)
            Received update: 
                PCTCHG_3M (3378) = -2.44
                PCTCHG_6M (3379) = 4.7
                PCTCHG_MTD (3380) = -1.37
                PCTCHG_YTD (3381) = 11.66
        >>> onUpdate() called for <EUR=>.  (Thread: main)
                        .
                        .
                        .
        >>> onUpdate() called for <EUR=>.  (Thread: main)
            Received update: 
                BID_NET_CH (114) = -0.0031
                IRGPRC (372) = -0.26
        >>> Finished dispatching events after 5 seconds
        >>> Closing <EUR=>

    .............................................................................
    . 5/17 - openAMarketPrice_And_DisplayStatusWhenIsComplete()
    .............................................................................
    . This step demonstrates how to retrieve the state of a MarketPrice. As the
    . State is only available after completion, we poll the isComplete() method
    . to wait for this completion (We could have used the onComplete() callback
    . instead). As we opened a valid instrument, the displayed State is the one
    . that came with the first RefreshMsg (the image).

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events until <EUR=> is complete
        >>> Finished dispatching events. <EUR=> is complete.
        >>> Current State of <EUR=>: Open / Ok / None / '*All is well'
        >>> Closing <EUR=>

    .............................................................................
    . 6/17 - openAMarketPrice_And_DisplayStatusOnStatus()
    .............................................................................
    . This step demonstrates how to be notified of incoming States individually 
    . sent (not within a Refresh Message) by the Platform. This may happen when
    . an error occurs for example. To this aim, we registered an onState()
    . lambda expression and opened a MarketPrice that we setup with the name of
    . an inexistent instrument. As the instrument doesn't exist, we expect the
    . lambda to be called with a "Closed / Suspect / Not found" State.
    . Notes:
    .   - Once the State received, the MarketPrice is considered complete.
    .   - If you also registered an onComplete() callback, the onState() callback
    .     is called first. Then onComplete() is called.

        <<< Press <Enter> to continue...

        >>> Opening <UNKNOWN_RIC>
        >>> Dispatching events until <UNKNOWN_RIC> is complete
        >>> onState() called for <UNKNOWN_RIC>.  (Thread: main)
            Received State: Closed / Suspect / Not found / '*The record could not be found'
        >>> Finished dispatching events. <UNKNOWN_RIC> is complete.
        >>> Closing <UNKNOWN_RIC>

    .............................................................................
    . 7/17 - openAMarketPrice_And_DisplayFieldsByName()
    .............................................................................
    . This step demonstrates how to retrieve a field by its name. This is an.
    . alternative to the fields collection iteration that you can use when your
    . application knows the names of the fields it needs. In this examples,
    . fields are retrieved in the onImage() lambda expression.
    . Note:
    .   - If you try to retrieve a field before the MarketPrice the first image
    .     is received, the getField(String) method returns null.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events until <EUR=> is complete
        >>> onImage() called for <EUR=>.  (Thread: main)
            DSPLY_NAME(3) = BARCLAYS     LON
            BID(22) = 1.174
            ASK(25) = 1.1744
        >>> Finished dispatching events. <EUR=> is complete.
        >>> Closing <EUR=>

    .............................................................................
    . 8/17 - openAMarketPrice_And_DisplayFieldsById()
    .............................................................................
    . This step demonstrates how to retrieve a field by its field Id. This is an.
    . alternative to the fields collection iteration that you can use when your
    . application knows the Ids of the fields it needs. In this examples, fields
    .  are retrieved in the onImage() lambda expression.
    . Note:
    .   - If you try to retrieve a field before the MarketPrice the first image
    .     is received, the getField(int) method returns null.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events until <EUR=> is complete
        >>> onImage() called for <EUR=>.  (Thread: main)
            DSPLY_NAME(3) = BARCLAYS     LON
            BID(22) = 1.1741
            ASK(25) = 1.1745
        >>> Finished dispatching events. <EUR=> is complete.
        >>> Closing <EUR=>

    .............................................................................
    . 9/17 - openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds()
    .............................................................................
    . In this step we demonstrate the caching capability of MarketPrice objects.
    . If you open a MarketPrice for an instrument in streaming mode and if you 
    . properly dipatch the related events, the MarkePrice object is maintained 
    . updated with the latest publications (images,updates, states) sent by the
    . source of the instrument.
    . To demonstrate this capability, we open a MarketPrice, we wait for its
    . completion and we display the cached values of 3 fields (DSPLY_NAME, BID
    . and ASK) every 2 seconds.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events until <EUR=> is complete
        >>> Finished dispatching events. <EUR=> is complete.
        >>> Fields at 14:10:40
            DSPLY_NAME(3) = CITIBANK     PRG
            BID(22) = 1.1742
            ASK(25) = 1.1743
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:10:42
            DSPLY_NAME(3) = BELAGROPROM  MIN
            BID(22) = 1.1741
            ASK(25) = 1.1744
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:10:44
            DSPLY_NAME(3) = MDM BANK     MOW
            BID(22) = 1.1742
            ASK(25) = 1.1743
        >>> Dispatching events for 2 seconds
                        .
                        .
                        .
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:10:58
            DSPLY_NAME(3) = BARCLAYS     LON
            BID(22) = 1.174
            ASK(25) = 1.1744
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Closing <EUR=>

    .............................................................................
    . 10/17 - openAMarketPriceWithoutUpdates_And_DisplayFieldsOnImage()
    .............................................................................
    . This step demonstrates how to open a MarkePrice in non-streaming mode
    . (a.k.a snapshot mode). This mode allows to get a snapshot of the MarkePrice
    . fields. When opened in this mode, the MarkePrice never receive any Update
    . message from the Platform. The onUpdate() callback is never called and the
    . MarketPrice is not kept updated with the latest values published for the
    . instrument.
    . To demonstrate this capability, we build a MarketPrice in non-streaming
    . mode set with onImage() and onUpdate() callback methods, we open the
    . MarketPrice and we dispatch events for 2 seconds.
    . The onImage() callback should be called once. The onUpdate() callback
    . should never be called.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events for 2 seconds
        >>> onImage() called for <EUR=>.  (Thread: main)
            Received State: Non-streaming / Ok / None / '*All is well'
            Received image: 
                PROD_PERM (1) = 213
                RDNDISPLAY (2) = 153
                DSPLY_NAME (3) = BARCLAYS     LON
                        .
                        .
                        .
                BID_HR_MS (14208) = 13:00:00:722:000:000
        >>> Finished dispatching events after 2 seconds
        >>> Closing <EUR=>

    .............................................................................
    . 11/17 - openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_AutoDispatchTrue()
    .............................................................................
    . In this step, we show how to open a MarketPrice in synchronous mode. In
    . this mode, the open() method is blocking and only returns once the
    . MarketPrice is complete.
    . For the purpose of this demonstration, we build a MarketPrice with the
    . SynchronousMode activated and, as soon as the open() method returns, we
    . display the received fields.
    . It is important to note that the OmmConsumer object used for this step
    . has been built using the USER_DISPATCH EMA operation model. For this reason
    . the MarketPrice object is built with the synchronous mode activated but 
    . also with the autodispatch parameter set to true. Thanks to this parameter
    . the open method of the MarketPrice will dispatch EMA events until an image
    . or a status is received from the platform.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Display fields just after open() returned: 
                PROD_PERM (1) = 213
                RDNDISPLAY (2) = 153
                DSPLY_NAME (3) = SOC GENERALE PAR
                        .
                        .
                        .
                BID_HR_MS (14208) = 13:00:00:722:000:000
        >>> Closing <EUR=>

    .............................................................................
    . 12/17 - openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_AutoDispatchFalse()
    .............................................................................
    . This step demonstrates that even if you built the MarkePrice with the
    . synchronous mode activated, you continue to benefit from the automatic
    . update mechanism of the cached image.
    . For the purpose of this demonstration, we build a MarketPrice with the
    . SynchronousMode activated and, as soon as the open() method returns, we
    . display the DSPLY_NAME,the BID and the ASK fields every 2 seconds.
    . It is important to note that the OmmConsumer object used for this step
    . has been built using the USER_DISPATCH EMA operation model. For this reason
    . the MarketPrice object is built with the synchronous mode activated but 
    . also with the autodispatch parameter set to true. Thanks to this parameter
    . the open method of the MarketPrice will dispatch EMA events until an image
    . or a status is received from the platform.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Display fields every 2 seconds just after open() returned: 
        >>> Fields at 14:11:10
            DSPLY_NAME(3) = BARCLAYS     LON
            BID(22) = 1.174
            ASK(25) = 1.1744
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:11:12
            DSPLY_NAME(3) = BARCLAYS     LON
            BID(22) = 1.1739
            ASK(25) = 1.1743
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:11:14
            DSPLY_NAME(3) = INTERPROMBAN MOW
            BID(22) = 1.174
            ASK(25) = 1.1742
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:11:16
            DSPLY_NAME(3) = RBS          LON
            BID(22) = 1.174
            ASK(25) = 1.1743
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Fields at 14:11:18
            DSPLY_NAME(3) = RBS          LON
            BID(22) = 1.174
            ASK(25) = 1.1742
        >>> Dispatching events for 2 seconds
        >>> Finished dispatching events after 2 seconds
        >>> Closing <EUR=>

    .............................................................................
    . 13/17 - openAMarkePriceWithPartialUpdates()
    .............................................................................
    . This step demonstrates the automatic partial update management that comes
    . with MarkePrice objects.
    . For the purpose of this demonstration, we open the "FXFX" record page that
    . regularly publishes partial updates for its ROW64_n fields. We first open
    . it with the partial update management feature deactivated (default
    . behavior). Then we open the instrument with the partial update management
    . activated. You should notice a difference between the updates received in
    . the two modes. When the feature is not activated, partial updates are
    . directly sent to the application causing incomplete update values and a
    . corrupt instrument image. When the feature is activated, partial updates
    . are applied on the cached and then sent to the application as complete
    . updates. This guarantees the integrity of the received updates and cached
    . instrument image.
    . Note:
    .   - As this feature requires additional processing resource. We recommend
    .     you to use it only with instruments that actually publish partial
    .     updates.

        >>> We are about to open <FXFX> WITHOUT partial update management
        <<< Press <Enter> to continue...

        >>> Opening <FXFX> WITHOUT partial update management
        >>> Dispatching events for 15 seconds
        >>> Initial image of <FXFX>.  (Thread: main)
                PROD_PERM (1) = 131
                RDNDISPLAY (2) = 132
                BOND_TYPE (104) =    
                ROW64_1 (215) = 1311 CCY PAGE NAME * REUTER SPOT RATES     * CCY HI*EURO*LO FXFX
                ROW64_2 (216) = 1311 EUR RBSL RBS          LON 1.1740/42   * EUR  1.1777  1.1729
                ROW64_3 (217) = 1311 GBP BCFX BARCLAYS     LON 1.3419/23   * GBP  1.3519  1.3407
                ROW64_4 (218) = 1311 CHF BCFX BARCLAYS     LON 0.9966/70   * CHF  0.9977  0.9938
                ROW64_5 (219) = 1311 JPY KASP KASPI BANK   ALA 113.50/53   * JPY  113.58  113.06
                ROW64_6 (220) = 1311 AUD BNZW BK OF NZ     WEL 0.7516/17   * AUD  0.7517  0.7498
                ROW64_7 (221) = 1311 CAD CIBT CIBC         TOR 1.2837/41   * CAD  1.2868  1.2829
                ROW64_8 (222) = 1311 DKK      BROKER       GFX 6.3375/85   * DKK  6.3433  6.3183
                ROW64_9 (223) = 1311 NOK CIBT CIBC         TOR 8.3155/05   * NOK  8.3410  8.3082
                ROW64_10 (224) = ----------------------------------------------------------------
                ROW64_11 (225) = XAU LMX 1247.12/1247.25* ED3  1.52/ 1.72 * FED        * WGVS 30Y
                ROW64_12 (226) = XAG LMX  15.82/15.82   * US30Y YTM  2.77 * 1.16- 1.19 * 99.19/19
                ROW64_13 (227) =                                                                 
                ROW64_14 (228) =                                                                 
                RECORDTYPE (259) = 218
                REG_ID1 (456) = 17
                REG_FIELD1 (457) = UnsupportedDataType
                ROW1_TIME (701) = (blank data)
                ROW2_TIME (702) = (blank data)
                ROW3_TIME (703) = (blank data)
                ROW4_TIME (704) = (blank data)
                ROW5_TIME (705) = (blank data)
                ROW6_TIME (706) = (blank data)
                ROW7_TIME (707) = (blank data)
                ROW8_TIME (708) = (blank data)
                ROW9_TIME (709) = (blank data)
                ROW10_TIME (710) = (blank data)
                ROW11_TIME (711) = (blank data)
                ROW12_TIME (712) = (blank data)
                ROW13_TIME (713) = (blank data)
                ROW14_TIME (714) = (blank data)
                BYTE_BMAP (1079) = 8
                PREF_DISP (1080) = (blank data)
                DSO_ID (1383) = 16416
                CONTEXT_ID (5357) = 3284.0
                DDS_DSO_ID (6401) = 12349
                SPS_SP_RIC (6480) = .[SPSEVAI-VAH11-P0
        >>> New update received for <FXFX>.  (Thread: main)
                ROW64_1 (215) = 1311
                ROW64_2 (216) = IPBMINTERPROMBAN MOW1.1740/42
                ROW64_3 (217) = CKLUCARL KLIEM   LUX1.3421/22
                ROW64_4 (218) =     ZUERCHER KB  ZUR0.9967/68
                ROW64_5 (219) = BCFXBARCLAYS     LON113.50/53
                ROW64_6 (220) = BNZWBK OF NZ     WEL0.7515/17
                ROW64_7 (221) = BCFXBARCLAYS     LON1.2841/45
                ROW64_8 (222) =     BROKER       GFX6.3376/94
                ROW64_9 (223) = CIBTCIBC         TOR8.3155/05
                ROW64_11 (225) = EBS 1246.711247.48WGVS
                ROW64_12 (226) = LMX 15.8115.822.77 99.19/19
        >>> New update received for <FXFX>.  (Thread: main)
                ROW64_1 (215) = 1311
                ROW64_2 (216) = ASANASANPACIFIBK MOW1.1740/41
                ROW64_3 (217) = CSBKKASPI BANK   ALA1.3420/23
                ROW64_4 (218) = MDMBMDM BANK     MOW0.9968/69
                ROW64_5 (219) = BCFXBARCLAYS     LON113.51/54
                ROW64_6 (220) = PBGRPIRAEUS BANK ATH0.7515/16
                ROW64_7 (221) = CIBTCIBC         TOR1.2838/42
                ROW64_8 (222) =     BROKER       GFX6.3376/86
                ROW64_9 (223) = CIBTCIBC         TOR8.3152/02
                ROW64_11 (225) = LMX 1246.811246.96WGVS
                ROW64_12 (226) = UBSZ15.8015.852.77 99.18/19
        >>> New update received for <FXFX>.  (Thread: main)
                ROW64_1 (215) = 1311
                ROW64_2 (216) = BCFXBARCLAYS     LON1.1738/42
                ROW64_3 (217) = IPBMINTERPROMBAN MOW1.3420/22
                ROW64_4 (218) = MDMBMDM BANK     MOW0.9967/69
                ROW64_5 (219) = BCFXBARCLAYS     LON113.51/54
                ROW64_6 (220) = PBGRPIRAEUS BANK ATH0.7515/16
                ROW64_7 (221) = KASPKASPI BANK   ALA1.2840/44
                ROW64_8 (222) = RBSLRBS          LON6.3381/91
                ROW64_9 (223) = CIBTCIBC         TOR8.3156/06
                ROW64_11 (225) = LMX 1246.811246.92WGVS
                ROW64_12 (226) = LMX 15.8115.812.77 99.18/19
        >>> Finished dispatching events after 15 seconds
        >>> Image cached for <FXFX> with Partial Updates Management DEACTIVATED.  (Thread: main)
                PROD_PERM (1) = 131
                RDNDISPLAY (2) = 132
                BOND_TYPE (104) =    
                ROW64_1 (215) = 1311
                ROW64_2 (216) = BCFXBARCLAYS     LON1.1738/42
                ROW64_3 (217) = IPBMINTERPROMBAN MOW1.3420/22
                ROW64_4 (218) = MDMBMDM BANK     MOW0.9967/69
                ROW64_5 (219) = BCFXBARCLAYS     LON113.51/54
                ROW64_6 (220) = PBGRPIRAEUS BANK ATH0.7515/16
                ROW64_7 (221) = KASPKASPI BANK   ALA1.2840/44
                ROW64_8 (222) = RBSLRBS          LON6.3381/91
                ROW64_9 (223) = CIBTCIBC         TOR8.3156/06
                ROW64_10 (224) = ----------------------------------------------------------------
                ROW64_11 (225) = LMX 1246.811246.92WGVS
                ROW64_12 (226) = LMX 15.8115.812.77 99.18/19
                ROW64_13 (227) =                                                                 
                ROW64_14 (228) =                                                                 
                RECORDTYPE (259) = 218
                REG_ID1 (456) = 17
                REG_FIELD1 (457) = UnsupportedDataType
                ROW1_TIME (701) = (blank data)
                ROW2_TIME (702) = (blank data)
                ROW3_TIME (703) = (blank data)
                ROW4_TIME (704) = (blank data)
                ROW5_TIME (705) = (blank data)
                ROW6_TIME (706) = (blank data)
                ROW7_TIME (707) = (blank data)
                ROW8_TIME (708) = (blank data)
                ROW9_TIME (709) = (blank data)
                ROW10_TIME (710) = (blank data)
                ROW11_TIME (711) = (blank data)
                ROW12_TIME (712) = (blank data)
                ROW13_TIME (713) = (blank data)
                ROW14_TIME (714) = (blank data)
                BYTE_BMAP (1079) = 8
                PREF_DISP (1080) = (blank data)
                DSO_ID (1383) = 16416
                CONTEXT_ID (5357) = 3284.0
                DDS_DSO_ID (6401) = 12349
                SPS_SP_RIC (6480) = .[SPSEVAI-VAH11-P0
        >>> Closing <FXFX>
        >>> We are about to open <FXFX> WITH partial update management
        <<< Press <Enter> to continue...

        >>> Opening <FXFX> WITH partial update management
        >>> Dispatching events for 15 seconds
        >>> Initial image of <FXFX>.  (Thread: main)
                PROD_PERM (1) = 131
                RDNDISPLAY (2) = 132
                BOND_TYPE (104) =    
                ROW64_1 (215) = 1311 CCY PAGE NAME * REUTER SPOT RATES     * CCY HI*EURO*LO FXFX
                ROW64_2 (216) = 1311 EUR BCFX BARCLAYS     LON 1.1738/42   * EUR  1.1777  1.1729
                ROW64_3 (217) = 1311 GBP IPBM INTERPROMBAN MOW 1.3420/22   * GBP  1.3519  1.3407
                ROW64_4 (218) = 1311 CHF MDMB MDM BANK     MOW 0.9967/69   * CHF  0.9977  0.9938
                ROW64_5 (219) = 1311 JPY BCFX BARCLAYS     LON 113.51/54   * JPY  113.58  113.06
                ROW64_6 (220) = 1311 AUD PBGR PIRAEUS BANK ATH 0.7515/16   * AUD  0.7517  0.7498
                ROW64_7 (221) = 1311 CAD KASP KASPI BANK   ALA 1.2840/44   * CAD  1.2868  1.2829
                ROW64_8 (222) = 1311 DKK RBSL RBS          LON 6.3381/91   * DKK  6.3433  6.3183
                ROW64_9 (223) = 1311 NOK CIBT CIBC         TOR 8.3156/06   * NOK  8.3410  8.3082
                ROW64_10 (224) = ----------------------------------------------------------------
                ROW64_11 (225) = XAU LMX 1246.81/1246.92* ED3  1.52/ 1.72 * FED        * WGVS 30Y
                ROW64_12 (226) = XAG LMX  15.81/15.81   * US30Y YTM  2.77 * 1.16- 1.19 * 99.18/19
                ROW64_13 (227) =                                                                 
                ROW64_14 (228) =                                                                 
                RECORDTYPE (259) = 218
                REG_ID1 (456) = 17
                REG_FIELD1 (457) = UnsupportedDataType
                ROW1_TIME (701) = (blank data)
                ROW2_TIME (702) = (blank data)
                ROW3_TIME (703) = (blank data)
                ROW4_TIME (704) = (blank data)
                ROW5_TIME (705) = (blank data)
                ROW6_TIME (706) = (blank data)
                ROW7_TIME (707) = (blank data)
                ROW8_TIME (708) = (blank data)
                ROW9_TIME (709) = (blank data)
                ROW10_TIME (710) = (blank data)
                ROW11_TIME (711) = (blank data)
                ROW12_TIME (712) = (blank data)
                ROW13_TIME (713) = (blank data)
                ROW14_TIME (714) = (blank data)
                BYTE_BMAP (1079) = 8
                PREF_DISP (1080) = (blank data)
                DSO_ID (1383) = 16416
                CONTEXT_ID (5357) = 3284.0
                DDS_DSO_ID (6401) = 12349
                SPS_SP_RIC (6480) = .[SPSEVAI-VAH11-P0
        >>> New update received for <FXFX>.  (Thread: main)
                ROW64_1 (215) = 1311 CCY PAGE NAME * REUTER SPOT RATES     * CCY HI*EURO*LO FXFX
                ROW64_2 (216) = 1311 EUR      KIBRIS BANK  MER 1.1739/41   * EUR  1.1777  1.1729
                ROW64_3 (217) = 1311 GBP ASAN ASANPACIFIBK MOW 1.3421/22   * GBP  1.3519  1.3407
                ROW64_4 (218) = 1311 CHF CKLU CARL KLIEM   LUX 0.9967/69   * CHF  0.9977  0.9938
                ROW64_5 (219) = 1311 JPY MDMB MDM BANK     MOW 113.52/53   * JPY  113.58  113.06
                ROW64_6 (220) = 1311 AUD BCFX BARCLAYS     LON 0.7514/16   * AUD  0.7517  0.7498
                ROW64_7 (221) = 1311 CAD COB1 Commerzbank  FFT 1.2837/42   * CAD  1.2868  1.2829
                ROW64_8 (222) = 1311 DKK      BROKER       GFX 6.3379/89   * DKK  6.3433  6.3183
                ROW64_9 (223) = 1311 NOK RBSL RBS          LON 8.3163/03   * NOK  8.3410  8.3082
                ROW64_11 (225) = XAU EBS 1246.41/1247.18* ED3  1.52/ 1.72 * FED        * WGVS 30Y
                ROW64_12 (226) = XAG LMX  15.81/15.81   * US30Y YTM  2.77 * 1.16- 1.19 * 99.18/19
        >>> New update received for <FXFX>.  (Thread: main)
                ROW64_2 (216) = 1311 EUR BCFX BARCLAYS     LON 1.1739/43   * EUR  1.1777  1.1729
                ROW64_3 (217) = 1311 GBP NBJX NEDBANK LTD  JHB 1.3422/23   * GBP  1.3519  1.3407
                ROW64_4 (218) = 1311 CHF BCFX BARCLAYS     LON 0.9966/70   * CHF  0.9977  0.9938
                ROW64_5 (219) = 1311 JPY ASAN ASANPACIFIBK MOW 113.51/52   * JPY  113.58  113.06
                ROW64_6 (220) = 1311 AUD BNZW BK OF NZ     WEL 0.7515/17   * AUD  0.7517  0.7498
                ROW64_7 (221) = 1311 CAD KASP KASPI BANK   ALA 1.2840/44   * CAD  1.2868  1.2829
                ROW64_8 (222) = 1311 DKK RBSL RBS          LON 6.3378/88   * DKK  6.3433  6.3183
                ROW64_9 (223) = 1311 NOK RBSL RBS          LON 8.3161/01   * NOK  8.3410  8.3082
                ROW64_11 (225) = XAU LMX 1246.77/1246.87* ED3  1.52/ 1.72 * FED        * WGVS 30Y
                ROW64_12 (226) = XAG EBS  15.79/15.83   * US30Y YTM  2.77 * 1.16- 1.19 * 99.18/19
        >>> New update received for <FXFX>.  (Thread: main)
                ROW64_1 (215) = 1312 CCY PAGE NAME * REUTER SPOT RATES     * CCY HI*EURO*LO FXFX
                ROW64_2 (216) = 1311 EUR      EMIRATES NBD DXB 1.1740/42   * EUR  1.1777  1.1729
                ROW64_3 (217) = 1311 GBP BCFX BARCLAYS     LON 1.3420/24   * GBP  1.3519  1.3407
                ROW64_4 (218) = 1311 CHF NBTK NBU UZB      TAK 0.9966/70   * CHF  0.9977  0.9938
                ROW64_5 (219) = 1312 JPY RZBM Raiffeisen   MOW 113.51/52   * JPY  113.58  113.06
                ROW64_6 (220) = 1311 AUD PBGR PIRAEUS BANK ATH 0.7514/19   * AUD  0.7517  0.7498
                ROW64_7 (221) = 1311 CAD NDEA NORDEA       CPH 1.2841/42   * CAD  1.2868  1.2829
                ROW64_8 (222) = 1311 DKK      BROKER       GFX 6.3377/87   * DKK  6.3433  6.3183
                ROW64_9 (223) = 1311 NOK CIBT CIBC         TOR 8.3157/07   * NOK  8.3410  8.3082
                ROW64_11 (225) = XAU LMX 1246.72/1246.86* ED3  1.52/ 1.72 * FED        * WGVS 30Y
        >>> Finished dispatching events after 15 seconds
        >>> Image cached for <FXFX> with Partial Updates Management ACTIVATED.  (Thread: main)
                PROD_PERM (1) = 131
                RDNDISPLAY (2) = 132
                BOND_TYPE (104) =    
                ROW64_1 (215) = 1312 CCY PAGE NAME * REUTER SPOT RATES     * CCY HI*EURO*LO FXFX
                ROW64_2 (216) = 1311 EUR      EMIRATES NBD DXB 1.1740/42   * EUR  1.1777  1.1729
                ROW64_3 (217) = 1311 GBP BCFX BARCLAYS     LON 1.3420/24   * GBP  1.3519  1.3407
                ROW64_4 (218) = 1311 CHF NBTK NBU UZB      TAK 0.9966/70   * CHF  0.9977  0.9938
                ROW64_5 (219) = 1312 JPY RZBM Raiffeisen   MOW 113.51/52   * JPY  113.58  113.06
                ROW64_6 (220) = 1311 AUD PBGR PIRAEUS BANK ATH 0.7514/19   * AUD  0.7517  0.7498
                ROW64_7 (221) = 1311 CAD NDEA NORDEA       CPH 1.2841/42   * CAD  1.2868  1.2829
                ROW64_8 (222) = 1311 DKK      BROKER       GFX 6.3377/87   * DKK  6.3433  6.3183
                ROW64_9 (223) = 1311 NOK CIBT CIBC         TOR 8.3157/07   * NOK  8.3410  8.3082
                ROW64_10 (224) = ----------------------------------------------------------------
                ROW64_11 (225) = XAU LMX 1246.72/1246.86* ED3  1.52/ 1.72 * FED        * WGVS 30Y
                ROW64_12 (226) = XAG EBS  15.79/15.83   * US30Y YTM  2.77 * 1.16- 1.19 * 99.18/19
                ROW64_13 (227) =                                                                 
                ROW64_14 (228) =                                                                 
                RECORDTYPE (259) = 218
                REG_ID1 (456) = 17
                REG_FIELD1 (457) = UnsupportedDataType
                ROW1_TIME (701) = (blank data)
                ROW2_TIME (702) = (blank data)
                ROW3_TIME (703) = (blank data)
                ROW4_TIME (704) = (blank data)
                ROW5_TIME (705) = (blank data)
                ROW6_TIME (706) = (blank data)
                ROW7_TIME (707) = (blank data)
                ROW8_TIME (708) = (blank data)
                ROW9_TIME (709) = (blank data)
                ROW10_TIME (710) = (blank data)
                ROW11_TIME (711) = (blank data)
                ROW12_TIME (712) = (blank data)
                ROW13_TIME (713) = (blank data)
                ROW14_TIME (714) = (blank data)
                BYTE_BMAP (1079) = 8
                PREF_DISP (1080) = (blank data)
                DSO_ID (1383) = 16416
                CONTEXT_ID (5357) = 3284.0
                DDS_DSO_ID (6401) = 12349
                SPS_SP_RIC (6480) = .[SPSEVAI-VAH11-P0
        >>> Closing <FXFX>

    .............................................................................
    . 14/17 - openAMarkePriceWithAViewDefinedWithFieldIds()
    .............................................................................
    . This step demonstrates how define and open an instrument view using field
    . Ids. As for the other MarketPrice parameters, the view is defined using the
    . MarketPrice.Builder.
    . Once the view defined, the MarketPrice works as usual, except that it only
    . contains the requested fields.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Dispatching events for 10 seconds
        >>> onImage() called for <EUR=>.  (Thread: main)
            Received State: Open / Ok / None / '*All is well'
            Received image: 
                DSPLY_NAME (3) = OTP BANK RT  BUD
                BID (22) = 1.1741
                ASK (25) = 1.1742
        >>> onUpdate() called for <EUR=>.  (Thread: main)
            Received update: 
                BID (22) = 1.1739
                ASK (25) = 1.1742
                DSPLY_NAME (3) = NBU UZB      TAK
        >>> onUpdate() called for <EUR=>.  (Thread: main)
                        .
                        .
                        .
        >>> onUpdate() called for <EUR=>.  (Thread: main)
            Received update: 
                BID (22) = 1.174
                ASK (25) = 1.1741
                DSPLY_NAME (3) = DNB BANK     OSL
        >>> Finished dispatching events after 10 seconds
        >>> Closing <EUR=>

    .............................................................................
    >>> Uninitializing the OmmConsumer

    .............................................................................
    >>> Creating the OmmConsumer (API_DISPATCH)

    .............................................................................
    . 16/17 - openAMarketPriceWithoutUpdates_And_DisplayFields_SynchronousMode_ApiDispatch()
    .............................................................................
    . This step the same than step 11 but with an OmmConsumer built using the 
    . API_DISPATCH operation model instead of USER_DISPATCH.
    .
    . In this step, we show how to open a MarketPrice in synchronous mode. In
    . this mode, the open() method is blocking and only returns once the
    . MarketPrice is complete.
    . For the purpose of this demonstration, we build a MarketPrice with the
    . SynchronousMode activated and, as soon as the open() method returns, we
    . display the received fields.
    . It is important to note that the OmmConsumer object used for this step
    . has been built using the API_DISPATCH EMA operation model. For this reason
    . the MarketPrice object is built with the synchronous mode activated but 
    . also with the autodispatch parameter set to false. Because of this parameter
    . the open method of the MarketPrice will not dispatch EMA events but sleep 
    . instead until an image or a status is received from the platform.


        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Display fields just after open() returned: 
                PROD_PERM (1) = 213
                RDNDISPLAY (2) = 153
                DSPLY_NAME (3) = CITIBANK     PRG
                        .
                        .
                        .
                BID_HR_MS (14208) = 13:00:00:722:000:000
        >>> Closing <EUR=>

    .............................................................................
    . 17/17 - openAMarketPrice_And_DisplayCachedFieldsEvery2Seconds_SynchronousMode_ApiDispatch()
    .............................................................................
    . This step the same than step 12 but with an OmmConsumer built using the 
    . API_DISPATCH operation model instead of USER_DISPATCH.
    .
    . This step demonstrates that even if you built the MarkePrice with the
    . synchronous mode activated, you continue to benefit from the automatic
    . update mechanism of the cached image.
    . For the purpose of this demonstration, we build a MarketPrice with the
    . SynchronousMode activated and, as soon as the open() method returns, we
    . display the DSPLY_NAME,the BID and the ASK fields every 2 seconds.
    . It is important to note that the OmmConsumer object used for this step
    . has been built using the API_DISPATCH EMA operation model. For this reason
    . the MarketPrice object is built with the synchronous mode activated but 
    . also with the autodispatch parameter set to false. Because of this parameter
    . the open method of the MarketPrice will not dispatch EMA events but sleep 
    . instead until an image or a status is received from the platform.

        <<< Press <Enter> to continue...

        >>> Opening <EUR=>
        >>> Display fields every 2 seconds just after open() returned: 
        >>> Fields at 14:12:34
            DSPLY_NAME(3) = ASANPACIFIBK MOW
            BID(22) = 1.174
            ASK(25) = 1.1741
        <<< Sleeping for 2 seconds...
        >>> Fields at 14:12:36
            DSPLY_NAME(3) = MDM BANK     MOW
            BID(22) = 1.174
            ASK(25) = 1.1741
        <<< Sleeping for 2 seconds...
        >>> Fields at 14:12:38
            DSPLY_NAME(3) = MDM BANK     MOW
            BID(22) = 1.174
            ASK(25) = 1.1741
        <<< Sleeping for 2 seconds...
        >>> Fields at 14:12:40
            DSPLY_NAME(3) = CITIBANK     PRG
            BID(22) = 1.174
            ASK(25) = 1.1741
        <<< Sleeping for 2 seconds...
        >>> Fields at 14:12:42
            DSPLY_NAME(3) = CITIBANK     PRG
            BID(22) = 1.174
            ASK(25) = 1.1741
        <<< Sleeping for 2 seconds...
        >>> Closing <EUR=>

    .............................................................................
    >>> Uninitializing the OmmConsumer
    >>> Exiting the application

