# Running the *ChainStepByStepExample*

**Before you start the application** you must configure the *EmaConfig.xml file* to specify the host name of the server (the TREP or Elektron platform) to which the EMA connects. This is set thanks to the value of the *\<ChannelGroup>\<ChannelList>\<Channel>\<Host>* node. This value can be a remote host name or IP address.

To start the *ChainStepByStepExample* run the *chain-stepbystep-example.bat* or the *chain-stepbystep-example.ksh* script. These scripts depend on the *JAVA_HOME* and *ELEKTRON_JAVA_HOME* environment variables that must have been defined for the build scripts.

### Expected output

This is an example of the *ChainStepByStepExample* output for each step:

    -------------------------------------------------------------------------------
    |                                                                             |
    |                    Chain step by step example application                   |
    |                                                                             |
    | This example application illustrates the concepts explained in the          |
    | "Simple Chain objects or How to easily open chains with EMA?" article       |
    | published on the Thomson Reuters Developer Portal. More specifically, this  |
    | application demonstrates how to use the ElektronObjects example SDK that    |
    | implements the different concepts, algorithms and optimizations described   |
    | in the article.                                                             |
    |                                                                             |
    | The application starts by creating an EMA OmmConsumer and uses it with the  |
    | toolkit to expand a number of different chains, demonstrating the           |
    | implemented capabilities. The chains are expanded one by one in 12          |
    | individual steps. For each step an explanatory text is displayed and you    |
    | are prompted to press <Enter> to start the step.                            |
    -------------------------------------------------------------------------------


      ..............................................................................
      >>> Creating the OmmConsumer (USER_DISPATCH)

      ..............................................................................
      . 1/12 - openAChainAndDisplayElementNamesWhenFinished_1()
      ..............................................................................
      . In this step we open the Dow Jones chain. When the chain decoding is
      . completed we display the names of all elements that constitute this chain.
      . We also display errors if any. In order to determine if the chain is
      . complete we poll the isComplete() method at regular intervals (after each
      . OMM consumer dispatch). In the next step we will see another technique to
      . detect chains completion.

        <<< Press <Enter> to continue...

        >>> Opening <0#.DJI>
        >>> Dispathing events until the chain is complete or in error
            0#.DJI[0] = .DJI
            0#.DJI[1] = AAPL.OQ
            0#.DJI[2] = AXP.N
            0#.DJI[3] = BA.N
            0#.DJI[4] = CAT.N
            0#.DJI[5] = CSCO.OQ
            0#.DJI[6] = CVX.N
            0#.DJI[7] = DD.N
            0#.DJI[8] = DIS.N
            0#.DJI[9] = GE.N
            0#.DJI[10] = GS.N
            0#.DJI[11] = HD.N
            0#.DJI[12] = IBM.N
            0#.DJI[13] = INTC.OQ
            0#.DJI[14] = JNJ.N
            0#.DJI[15] = JPM.N
            0#.DJI[16] = KO.N
            0#.DJI[17] = MCD.N
            0#.DJI[18] = MMM.N
            0#.DJI[19] = MRK.N
            0#.DJI[20] = MSFT.OQ
            0#.DJI[21] = NKE.N
            0#.DJI[22] = PFE.N
            0#.DJI[23] = PG.N
            0#.DJI[24] = TRV.N
            0#.DJI[25] = UNH.N
            0#.DJI[26] = UTX.N
            0#.DJI[27] = V.N
            0#.DJI[28] = VZ.N
            0#.DJI[29] = WMT.N
            0#.DJI[30] = XOM.N
        >>> Closing <0#.DJI>

      ..............................................................................
      . 2/12 - openAChainAndDisplayElementNamesWhenFinished_2()
      ..............................................................................
      . In this step we open the Dow Jones chain and display its elements names
      . when the chain is complete. This step displays the exact same information
      . than the previous one, but this time we use another technique to detect the
      . chain’s completion: we leverage the ChainCompleteFunction that is called as
      . soon as the chain is complete (no is polling required).

        <<< Press <Enter> to continue...

        >>> Opening <0#.DJI>
        >>> Dispathing events until the chain is complete or in error
            0#.DJI[0] = .DJI
            0#.DJI[1] = AAPL.OQ
            0#.DJI[2] = AXP.N
            0#.DJI[3] = BA.N
            0#.DJI[4] = CAT.N
            0#.DJI[5] = CSCO.OQ
            0#.DJI[6] = CVX.N
            0#.DJI[7] = DD.N
            0#.DJI[8] = DIS.N
            0#.DJI[9] = GE.N
            0#.DJI[10] = GS.N
            0#.DJI[11] = HD.N
            0#.DJI[12] = IBM.N
            0#.DJI[13] = INTC.OQ
            0#.DJI[14] = JNJ.N
            0#.DJI[15] = JPM.N
            0#.DJI[16] = KO.N
            0#.DJI[17] = MCD.N
            0#.DJI[18] = MMM.N
            0#.DJI[19] = MRK.N
            0#.DJI[20] = MSFT.OQ
            0#.DJI[21] = NKE.N
            0#.DJI[22] = PFE.N
            0#.DJI[23] = PG.N
            0#.DJI[24] = TRV.N
            0#.DJI[25] = UNH.N
            0#.DJI[26] = UTX.N
            0#.DJI[27] = V.N
            0#.DJI[28] = VZ.N
            0#.DJI[29] = WMT.N
            0#.DJI[30] = XOM.N
        >>> Closing <0#.DJI>

      ..............................................................................
      . 3/12 - openAChainAndDisplayElementsNamesAsSoonAsTheyAreDetected()
      ..............................................................................
      . In this step we open the Dow Jones chain and display the name of new
      . elements as soon as they are detected by the decoding algorithm. To this
      . aim we leverage the ElementAddedFunction functional interface.

        <<< Press <Enter> to continue...

        >>> Opening <0#.DJI>
        >>> Dispathing events until the chain is complete or in error
            Element added to <0#.DJI> at position 0: .DJI
            Element added to <0#.DJI> at position 1: AAPL.OQ
            Element added to <0#.DJI> at position 2: AXP.N
            Element added to <0#.DJI> at position 3: BA.N
            Element added to <0#.DJI> at position 4: CAT.N
            Element added to <0#.DJI> at position 5: CSCO.OQ
            Element added to <0#.DJI> at position 6: CVX.N
            Element added to <0#.DJI> at position 7: DD.N
            Element added to <0#.DJI> at position 8: DIS.N
            Element added to <0#.DJI> at position 9: GE.N
            Element added to <0#.DJI> at position 10: GS.N
            Element added to <0#.DJI> at position 11: HD.N
            Element added to <0#.DJI> at position 12: IBM.N
            Element added to <0#.DJI> at position 13: INTC.OQ
            Element added to <0#.DJI> at position 14: JNJ.N
            Element added to <0#.DJI> at position 15: JPM.N
            Element added to <0#.DJI> at position 16: KO.N
            Element added to <0#.DJI> at position 17: MCD.N
            Element added to <0#.DJI> at position 18: MMM.N
            Element added to <0#.DJI> at position 19: MRK.N
            Element added to <0#.DJI> at position 20: MSFT.OQ
            Element added to <0#.DJI> at position 21: NKE.N
            Element added to <0#.DJI> at position 22: PFE.N
            Element added to <0#.DJI> at position 23: PG.N
            Element added to <0#.DJI> at position 24: TRV.N
            Element added to <0#.DJI> at position 25: UNH.N
            Element added to <0#.DJI> at position 26: UTX.N
            Element added to <0#.DJI> at position 27: V.N
            Element added to <0#.DJI> at position 28: VZ.N
            Element added to <0#.DJI> at position 29: WMT.N
            Element added to <0#.DJI> at position 30: XOM.N
        >>> Closing <0#.DJI>

      ..............................................................................
      . 4/12 - openAChainAndSkipSummaryLinks()
      ..............................................................................
      . In this step we open the Dow Jones chain once again, but this time we skip
      . the summary links. As the Dow Jones chain as one summary link, the chain 
      . will be made of 30 elements instead of 31. The number of summary links may
      . be different for other chains and depends on the display template used by
      . the chain. For example, it's 2 for the British FTSE 100 (0#.FTSE), 6 for
      . the Italian FTSE 100 (0#.FTMIB) and 6 for the French CAC40 (0#.FCHI).
      . The SummaryLinksToSkip object used in this method is setup for these 4
      . cases.

        <<< Press <Enter> to continue...

        >>> Opening <0#.DJI>
        >>> Dispathing events until the chain is complete or in error
            0#.DJI[0] = AAPL.OQ
            0#.DJI[1] = AXP.N
            0#.DJI[2] = BA.N
            0#.DJI[3] = CAT.N
            0#.DJI[4] = CSCO.OQ
            0#.DJI[5] = CVX.N
            0#.DJI[6] = DD.N
            0#.DJI[7] = DIS.N
            0#.DJI[8] = GE.N
            0#.DJI[9] = GS.N
            0#.DJI[10] = HD.N
            0#.DJI[11] = IBM.N
            0#.DJI[12] = INTC.OQ
            0#.DJI[13] = JNJ.N
            0#.DJI[14] = JPM.N
            0#.DJI[15] = KO.N
            0#.DJI[16] = MCD.N
            0#.DJI[17] = MMM.N
            0#.DJI[18] = MRK.N
            0#.DJI[19] = MSFT.OQ
            0#.DJI[20] = NKE.N
            0#.DJI[21] = PFE.N
            0#.DJI[22] = PG.N
            0#.DJI[23] = TRV.N
            0#.DJI[24] = UNH.N
            0#.DJI[25] = UTX.N
            0#.DJI[26] = V.N
            0#.DJI[27] = VZ.N
            0#.DJI[28] = WMT.N
            0#.DJI[29] = XOM.N
        >>> Closing <0#.DJI>

      ..............................................................................
      . 5/12 - openAVeryLongChain()
      ..............................................................................
      . In this step we open the NASDAQ BASIC chain that contains more than 8000 
      . elements. This kind of chain may take more than 20 seconds to open with the
      . normal decoding algorithm. For easier comparison with the optimized
      . algorithm, the time spent to decode the chain is displayed.

        <<< Press <Enter> to continue...

        >>> Opening <0#UNIVERSE.NB>
        >>> Dispathing events until the chain is complete or in error
            1 elements decoded for <0#UNIVERSE.NB>. Latest: A.NB
            101 elements decoded for <0#UNIVERSE.NB>. Latest: ADMA.NB
            201 elements decoded for <0#UNIVERSE.NB>. Latest: AGN_pa.NB
            301 elements decoded for <0#UNIVERSE.NB>. Latest: ALL_pb.NB
                .
                .
                .
            8401 elements decoded for <0#UNIVERSE.NB>. Latest: ZLTQ.NB
            Chain <0#UNIVERSE.NB> contains 8436 elements and opened in 31 seconds.
        >>> Closing <0#UNIVERSE.NB>

      ..............................................................................
      . 6/12 - openAVeryLongChainWithTheOptimizedAlgorithm()
      ..............................................................................
      . In this step we open the NASDAQ BASIC chain with the optimized decoding
      . algorithm. You should observe much better performance than with the normal
      . algorithm.

        <<< Press <Enter> to continue...

        >>> Opening <0#UNIVERSE.NB>
        >>> Dispathing events until the chain is complete or in error
            1 elements decoded for <0#UNIVERSE.NB>. Latest: A.NB
            101 elements decoded for <0#UNIVERSE.NB>. Latest: ADMA.NB
            201 elements decoded for <0#UNIVERSE.NB>. Latest: AGN_pa.NB
            301 elements decoded for <0#UNIVERSE.NB>. Latest: ALL_pb.NB
                .
                .
                .
            8401 elements decoded for <0#UNIVERSE.NB>. Latest: ZLTQ.NB
            Chain <0#UNIVERSE.NB> contains 8436 elements and opened in 1 seconds.
        >>> Closing <0#UNIVERSE.NB>

      ..............................................................................
      . 7/12 - openChainWithUpdates()
      ..............................................................................
      . In this step we open the "NYSE Active Volume leaders" tile (.AV.O), this
      . type of chain that updates very frequently. Tiles follow the same naming
      . convention than classical chains, except for the name of their first chain
      . record that doesn't start by "0#". This example leverages the
      . ElementAddedFunction, ElementChangedFunction and ElementRemovedFunction
      . functional interfaces to display chain changes. For this step, EMA events
      . are displayed for 2 minutes. In order to help you visualizing the changes
      . that happened to the chain, the complete list of chain elements is
      . displayed when the chain is complete and just before it is closed, after
      . the 2 minutes wait. If this step is executed when the NYSE is opened, you
      . should observe changes in the chain.

        <<< Press <Enter> to continue...

        >>> Opening <.AV.O>
        >>> Dispatching events for 120 seconds
        Element added to <.AV.O> at position 0: AMD.O
        Element added to <.AV.O> at position 1: QQQ.O
        Element added to <.AV.O> at position 2: FNSR.O
        Element added to <.AV.O> at position 3: INTC.O
        Element added to <.AV.O> at position 4: DRYS.O
        Element added to <.AV.O> at position 5: USLV.O
        Element added to <.AV.O> at position 6: TVIX.O
        Element added to <.AV.O> at position 7: FB.O
        Element added to <.AV.O> at position 8: NVDA.O
        Element added to <.AV.O> at position 9: OCRX.O
        Element added to <.AV.O> at position 10: OPK.O
        Element added to <.AV.O> at position 11: AUPH.O
        Element added to <.AV.O> at position 12: TSLA.O
        Element added to <.AV.O> at position 13: CERU.O
        Element added to <.AV.O> at position 14: RADA.O
        Element added to <.AV.O> at position 15: VCEL.O
        Element added to <.AV.O> at position 16: SRRA.O
        Element added to <.AV.O> at position 17: CERC.O
        Element added to <.AV.O> at position 18: GNMX.O
        Element added to <.AV.O> at position 19: PAAS.O
        Element added to <.AV.O> at position 20: ZSAN.O
        Element added to <.AV.O> at position 21: MYL.O
        Element added to <.AV.O> at position 22: UGLD.O
        Element added to <.AV.O> at position 23: GOLD.O
        Element added to <.AV.O> at position 24: AAPL.O

        The chain is complete and contains the following elements:
            .AV.O[0] = AMD.O
            .AV.O[1] = QQQ.O
            .AV.O[2] = FNSR.O
            .AV.O[3] = INTC.O
            .AV.O[4] = DRYS.O
            .AV.O[5] = USLV.O
            .AV.O[6] = TVIX.O
            .AV.O[7] = FB.O
            .AV.O[8] = NVDA.O
            .AV.O[9] = OCRX.O
            .AV.O[10] = OPK.O
            .AV.O[11] = AUPH.O
            .AV.O[12] = TSLA.O
            .AV.O[13] = CERU.O
            .AV.O[14] = RADA.O
            .AV.O[15] = VCEL.O
            .AV.O[16] = SRRA.O
            .AV.O[17] = CERC.O
            .AV.O[18] = GNMX.O
            .AV.O[19] = PAAS.O
            .AV.O[20] = ZSAN.O
            .AV.O[21] = MYL.O
            .AV.O[22] = UGLD.O
            .AV.O[23] = GOLD.O
            .AV.O[24] = AAPL.O
        Waiting for updates...

        Element changed in <.AV.O> at position 1
            Previous name: QQQ.O New name: INTC.O
        Element changed in <.AV.O> at position 2
            Previous name: FNSR.O New name: QQQ.O
        Element changed in <.AV.O> at position 3
            Previous name: INTC.O New name: FNSR.O

        The chain is about to be closed. It now contains the following elements:
            .AV.O[0] = AMD.O
            .AV.O[1] = INTC.O
            .AV.O[2] = QQQ.O
            .AV.O[3] = FNSR.O
            .AV.O[4] = DRYS.O
            .AV.O[5] = USLV.O
            .AV.O[6] = TVIX.O
            .AV.O[7] = FB.O
            .AV.O[8] = NVDA.O
            .AV.O[9] = OCRX.O
            .AV.O[10] = OPK.O
            .AV.O[11] = AUPH.O
            .AV.O[12] = TSLA.O
            .AV.O[13] = CERU.O
            .AV.O[14] = RADA.O
            .AV.O[15] = VCEL.O
            .AV.O[16] = SRRA.O
            .AV.O[17] = CERC.O
            .AV.O[18] = GNMX.O
            .AV.O[19] = PAAS.O
            .AV.O[20] = ZSAN.O
            .AV.O[21] = MYL.O
            .AV.O[22] = UGLD.O
            .AV.O[23] = GOLD.O
            .AV.O[24] = AAPL.O

        >>> Closing <.AV.O>

      ..............................................................................
      . 8/12 - openARecursiveChain()
      ..............................................................................
      . In this step we open the chain for the Equity Japanese Contracts (0#JP-EQ).
      . This chain contains elements that are also chains. In this step we use a 
      . RecursivesChain object to open all elements of this chain of chains 
      . recursively. With recursive chains, the position is represented by a list
      . of numbers (Each number representing a position at a given depth).
      . The element name is made of a list of strings (Each string representing
      . the name of the element at a given level).

        <<< Press <Enter> to continue...

        >>> Opening <0#JP-EQ>
        >>> Dispathing events until the chain is complete or in error
            0#JP-EQ[0] = [.TOPXC]
            0#JP-EQ[1, 0] = [.TSEI, .TOPX]
            0#JP-EQ[1, 1] = [.TSEI, .TSI2]
            0#JP-EQ[1, 2] = [.TSEI, .MTHR]
            0#JP-EQ[1, 3] = [.TSEI, .TSIL]
            0#JP-EQ[1, 4] = [.TSEI, .TSIM]
            0#JP-EQ[1, 5] = [.TSEI, .TSIS]
            0#JP-EQ[1, 6] = [.TSEI, .TOPXC]
            0#JP-EQ[1, 7] = [.TSEI, .TOPXL]
                .
                .
                .
            0#JP-EQ[5, 11, 27] = [0#JP-INDICES, .TSEK, .IBNKS.T]
            0#JP-EQ[5, 11, 28] = [0#JP-INDICES, .TSEK, .ISECU.T]
            0#JP-EQ[5, 11, 29] = [0#JP-INDICES, .TSEK, .IINSU.T]
            0#JP-EQ[5, 11, 30] = [0#JP-INDICES, .TSEK, .IFINS.T]
            0#JP-EQ[5, 11, 31] = [0#JP-INDICES, .TSEK, .IRLTY.T]
            0#JP-EQ[5, 11, 32] = [0#JP-INDICES, .TSEK, .ISVCS.T]
            0#JP-EQ[5, 12] = [0#JP-INDICES, .TSA1]
            0#JP-EQ[5, 13] = [0#JP-INDICES, .TSA2]
        >>> Closing <0#JP-EQ>

      ..............................................................................
      . 9/12 - openARecursiveChainWithMaxDepth()
      ..............................................................................
      . In this step we recursively open the chain for the Equity Japanese
      . Contracts (0#JP-EQ) and we limit the recursion depth to 2 levels.

        <<< Press <Enter> to continue...

        >>> Opening <0#JP-EQ>
        >>> Dispathing events until the chain is complete or in error
            0#JP-EQ[0] = [.TOPXC]
            0#JP-EQ[1, 0] = [.TSEI, .TOPX]
            0#JP-EQ[1, 1] = [.TSEI, .TSI2]
            0#JP-EQ[1, 2] = [.TSEI, .MTHR]
            0#JP-EQ[1, 3] = [.TSEI, .TSIL]
            0#JP-EQ[1, 4] = [.TSEI, .TSIM]
            0#JP-EQ[1, 5] = [.TSEI, .TSIS]
            0#JP-EQ[1, 6] = [.TSEI, .TOPXC]
            0#JP-EQ[1, 7] = [.TSEI, .TOPXL]
                .
                .
                .
            0#JP-EQ[5, 11] = [0#JP-INDICES, .TSEK]
            0#JP-EQ[5, 12] = [0#JP-INDICES, .TSA1]
            0#JP-EQ[5, 13] = [0#JP-INDICES, .TSA2]
        >>> Closing <0#JP-EQ>

      ..............................................................................
      . 10/12 - openAChainThatDoesntExist()
      ..............................................................................
      . In this step we try to open a chain that doesn't exist and display the 
      . error detected by the decoding algorithm.

        <<< Press <Enter> to continue...

        >>> Opening <THIS_CHAIN_DOESNT_EXIST>
        >>> Dispathing events until the chain is complete or in error
            Error received for <THIS_CHAIN_DOESNT_EXIST>: Invalid status received for <THIS_CHAIN_DOESNT_EXIST>: Closed / Suspect / Not found / '*The record could not be found'
        >>> Closing <THIS_CHAIN_DOESNT_EXIST>

	  ..............................................................................
	  . 11/12 - openAChain_SynchronousMode_UserDispatch()
	  ..............................................................................
	  . In this step, we show how to open a Chain in synchronous mode. In
	  . this mode, the open() method is blocking and only returns once the
	  . Chain is complete.
	  . For the purpose of this demonstration, we build a FlatChain with the
	  . SynchronousMode activated and, as soon as the open() method returns, we
	  . display the names of all elements that constitute this chain..
	  . It is important to note that the OmmConsumer object used for this step
	  . has been built using the USER_DISPATCH EMA operation model. For this reason
	  . the FlatChain object is built with the synchronous mode activated but
	  . also with the autodispatch parameter set to true. Thanks to this parameter
	  . the open method of the FlatChain will dispatch EMA events until it is complete.

	    <<< Press <Enter> to continue...

	    >>> Opening <0#.DJI>
	        0#.DJI[0] = .DJI
	        0#.DJI[1] = AAPL.OQ
	        0#.DJI[2] = AXP.N
	        0#.DJI[3] = BA.N
	        0#.DJI[4] = CAT.N
	        0#.DJI[5] = CSCO.OQ
	        0#.DJI[6] = CVX.N
	        0#.DJI[7] = DIS.N
	        0#.DJI[8] = DWDP.N
	        0#.DJI[9] = GE.N
	        0#.DJI[10] = GS.N
	        0#.DJI[11] = HD.N
	        0#.DJI[12] = IBM.N
	        0#.DJI[13] = INTC.OQ
	        0#.DJI[14] = JNJ.N
	        0#.DJI[15] = JPM.N
	        0#.DJI[16] = KO.N
	        0#.DJI[17] = MCD.N
	        0#.DJI[18] = MMM.N
	        0#.DJI[19] = MRK.N
	        0#.DJI[20] = MSFT.OQ
	        0#.DJI[21] = NKE.N
	        0#.DJI[22] = PFE.N
	        0#.DJI[23] = PG.N
	        0#.DJI[24] = TRV.N
	        0#.DJI[25] = UNH.N
	        0#.DJI[26] = UTX.N
	        0#.DJI[27] = V.N
	        0#.DJI[28] = VZ.N
	        0#.DJI[29] = WMT.N
	        0#.DJI[30] = XOM.N
	    >>> Closing <0#.DJI>

	  ..............................................................................

	  >>> Uninitializing the OmmConsumer

	  .............................................................................
	  >>> Creating the OmmConsumer (API_DISPATCH)

	  ..............................................................................

	  . 12/12 - openAChain_SynchronousMode_UserDispatch()
	  ..............................................................................
	  . In this step, we show how to open a Chain in synchronous mode. In
	  . this mode, the open() method is blocking and only returns once the
	  . Chain is complete.
	  . For the purpose of this demonstration, we build a FlatChain with the
	  . SynchronousMode activated and, as soon as the open() method returns, we
	  . display the names of all elements that constitute this chain..
	  . It is important to note that the OmmConsumer object used for this step
	  . has been built using the API_DISPATCH EMA operation model. For this reason
	  . the FlatChain object is built with the synchronous mode activated but
	  . also with the autodispatch parameter set to false. Thanks to this parameter
	  . the open method of the FlatChain will not dispatch EMA events but sleep
	  . instead until it is complete.

	    <<< Press <Enter> to continue...

	    >>> Opening <0#.DJI>
	        0#.DJI[0] = .DJI
	        0#.DJI[1] = AAPL.OQ
	        0#.DJI[2] = AXP.N
	        0#.DJI[3] = BA.N
	        0#.DJI[4] = CAT.N
	        0#.DJI[5] = CSCO.OQ
	        0#.DJI[6] = CVX.N
	        0#.DJI[7] = DIS.N
	        0#.DJI[8] = DWDP.N
	        0#.DJI[9] = GE.N
	        0#.DJI[10] = GS.N
	        0#.DJI[11] = HD.N
	        0#.DJI[12] = IBM.N
	        0#.DJI[13] = INTC.OQ
	        0#.DJI[14] = JNJ.N
	        0#.DJI[15] = JPM.N
	        0#.DJI[16] = KO.N
	        0#.DJI[17] = MCD.N
	        0#.DJI[18] = MMM.N
	        0#.DJI[19] = MRK.N
	        0#.DJI[20] = MSFT.OQ
	        0#.DJI[21] = NKE.N
	        0#.DJI[22] = PFE.N
	        0#.DJI[23] = PG.N
	        0#.DJI[24] = TRV.N
	        0#.DJI[25] = UNH.N
	        0#.DJI[26] = UTX.N
	        0#.DJI[27] = V.N
	        0#.DJI[28] = VZ.N
	        0#.DJI[29] = WMT.N
	        0#.DJI[30] = XOM.N
	    >>> Closing <0#.DJI>

	  ..............................................................................

	  >>> Uninitializing the OmmConsumer
	  >>> Exiting the application
