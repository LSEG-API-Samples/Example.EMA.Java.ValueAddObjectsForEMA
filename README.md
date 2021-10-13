# ValueAddObjectsForEMA


## Table of Content

* [Overview](#overview)

* [Disclaimer](#disclaimer)

* [Prerequisites](#prerequisites)

* [Implemented Features](#implemented-features)

* [Building the *ValueAddObjectsForEMA*](#building-the-valueaddobjectsforema)

* [Solution Code](#solution-code)

## <a id="overview" name=""></a>Overview
This project is one of the many learning materials published by Refinitiv to help developers learning Refinitiv APIs. It contains an example library of value add objects, logics and algorithms shared among the Enterprise Message API (EMA) example applications built by Refinitiv Developer Advocates for the Developer Community. The rationales that lead to the development of these value add objects are detailed in the [Simplifying Content Access in EMA](https://developers.refinitiv.com/en/article-catalog/article/simplifying-content-access-in-ema) article available on the [Refinitiv Developer Community portal](https://developers.refinitiv.com). 
The *ValueAddObjectsForEMA* example library is based on the Java edition of the Enterprise Message API that is one of the APIs of the Refinitiv Real-Time SDK. Please consult this [Refinitiv Real-Time SDK page](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java) for more learning materials and documentation about this API.

For any question related to this project please use the Developer Community [Q&A Forum](https://community.developers.refinitiv.com).

_**Note:** To be able to ask questions and to benefit from the full content available on the [Refinitiv Developer Community portal](https://developers.refinitiv.com) we recommend you to [register here]( https://login.refinitiv.com/iamui/UI/createUser?app_id=DevPlatform&realm=DevPlatform) or [login here]( https://developers.refinitiv.com/iam/login?destination_path=Lw%3D%3D)._

## Disclaimer
The _ValueAddObjectsForEMA_ example library has been written by Refinitiv for the only purpose of illustrating a series of articles published on the Refinitiv Developer Community. The _ValueAddObjectsForEMA_ example library has not been tested for a usage in production environments. Refinitiv cannot be held responsible for any issues that may happen if this project or the related source code is used in production or any other client environment.

## <a id="prerequisites" name="prerequisites"></a>Prerequisites

Required software components:

* [Enterprise Message API](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java) (2.0 or greater) - Refinitiv interface to the Refinitiv Real-Time Market Data environment
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java Development Kit - version 8

## <a id="implemented-features" name="implemented-features"></a>Implemented Features
_ValueAddObjectsForEMA_ is an example of a complementary library to be used alongside EMA in order to provide a higher level of abstraction. It provides objects and helpers that complement EMA and that simplify the coding of EMA applications. _ValueAddObjectsForEMA_ is implemented incrementally, in parallel with the EMA example applications built by Refinitiv Developer Advocates. It currently provides the following features: 

- Helpers to dispatch event and wait for conditions
- A synchronous and asynchronous subscribing modes.
- MarketPrice objects with a caching facility, a random access to field values and a full dictionary description associated to each field.
- Chain objects with a caching facility and a random access to chain constituents.

These features are implemented in the following packages:
- **com.refinitiv.platformservices.rt.objects.common**  
This package contains common interfaces and classes used by other modules of the library. It also provides general helper classes used by example applications (e.g. Dispatcher).   
- **com.refinitiv.platformservices.rt.objects.data**  
This package contains in-memory implementations of OMM Data that are required by other modules for caching received data in-memory.
- **com.refinitiv.platformservices.rt.objects.marketprice**  
This package contains the interface and implementation class of MarketPrice objects that provides higher level features like: in-memory data caching, a random access to field values, full field description, synchronous/asynchronous subscription, completion status...<br>
_**Note:** For more details about the logic implemented by the MarketPrice objects, please refer to the [A simple MarketPrice object for EMA](https://developers.refinitiv.com/en/article-catalog/article/simple-marketprice-object-ema-part-1) article._
- **com.refinitiv.platformservices.rt.objects.chain**  
This package contains the interface and implementation class of Chain objects that allow to automaticaly open chains and provides to their constituents.<br>
_**Note:** For more details about the logic implemented by the FlatChain and RecursiveChain objects, please refer to the [Simple Chain objects for EMA](https://developers.refinitiv.com/en/article-catalog/article/simple-chain-objects-ema-part-1) article._
- **com.refinitiv.platformservices.rt.objects.examples.marketprice**  
This package contains an example application that demonstrates the MarketPrice objects capabilities and how to use them. The application starts by creating an EMA OmmConsumer and uses it in with MarketPrice objects in several individual steps that demonstrate the implemented features. Before each step, explanatory text is displayed and you are prompted to press to start the step.
- **com.refinitiv.platformservices.rt.objects.examples.chain**  
This package contains an example application that demonstrates the FlatChain and RecursiveChain objects capabilities and how to use them. The application starts by creating an EMA OmmConsumer and uses it in with Chain objects in several individual steps that demonstrate the implemented features. Before each step, explanatory text is displayed and you are prompted to press to start the step.

The _ValueAddObjectsForEMA_ example library also comes with a Javadoc that fully describes the exposed APIs.

_**Note:** If you do not know yet about the Enterprise Message API (EMA) and how to program and EMA consumer application I recommend you to follow this [EMA Quick Start](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java/quick-start) and these [EMA Tutorials](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java/tutorials)._

## <a id="building-the-valueaddobjectsforema" name="building-the-valueaddobjectsforema"></a>Building the *ValueAddObjectsForEMA* library

### Change the service name and DACS user name if need be

The *MarketPriceStepByStepExample.java* file and  *ChainStepByStepExample.java* file contain two hardcoded values that you may want to change depending on the TREP or Elektron Real-Time platform you use. These values indicate:

* The **service name** used to subscribe: The hardcoded value is "ELEKTRON_DD". This value can be changed to that of the available service in the current environment.
 
## <a id="solution-code" name="solution-code"></a>Solution Code

The *ValueAddObjectsForEMA* example library was developed using the [Refinitiv Real-Time SDK Java API](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java) that is available for download [here](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java/download).

### Built With

* [Enterprise Message API](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java)
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [NetBeans 8.2](https://netbeans.org/) - IDE for Java development

### <a id="contributing" name="contributing"></a>Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

### <a id="authors" name="authors"></a>Authors 

* **Olivier Davant** - Release 1.0.  *Initial version*

### <a id="license" name="license"></a>License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
