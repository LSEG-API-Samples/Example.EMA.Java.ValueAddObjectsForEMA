# ValueAddObjectsForEMA


## Table of Content

* [Overview](#overview)

* [Disclaimer](#disclaimer)

* [Prerequisites](#prerequisites)

* [Implemented Features](#implemented-features)

* [Building the *ValueAddObjectsForEMA*](#building-the-valueaddobjectsforema)

* [Running the example applications](#running-the-example)

* [Troubleshooting](#troubleshooting)

* [Solution Code](#solution-code)

## <a id="overview" name=""></a>Overview
This project is one of the many learning materials published by Refinitiv to help developers learning Refinitiv APIs. It contains an example library of value add objects, logics and algorithms shared among the Enterprise Message API (EMA) example applications built by Refinitiv Developer Advocates for the Developer Community. The rationales that lead to the development of these value add objects are detailed in the [Simplifying Content Access in EMA](https://developers.refinitiv.com/article/simplifying-content-access-ema/article.mdown) article available on the [Refinitiv Developer Community portal](https://developers.refinitiv.com). 
The *ValueAddObjectsForEMA* example library is based on the Java edition of the Enterprise Message API that is one of the APIs of the Refinitiv Real-Time SDK. Please consult this [Refinitiv Real-Time SDK page](https://developers.refinitiv.com/elektron/elektron-sdk-java) for more learning materials and documentation about this API.

For any question related to this project please use the Developer Community [Q&A Forum](https://community.developers.refinitiv.com).

_**Note:** To be able to ask questions and to benefit from the full content available on the [TR Developer Community portal](https://developers.refinitiv.com) we recommend you to [register here]( https://login.refinitiv.com/iamui/UI/createUser?app_id=DevPlatform&realm=DevPlatform) or [login here]( https://developers.refinitiv.com/iam/login?destination_path=Lw%3D%3D)._

## Disclaimer
The _ValueAddObjectsForEMA_ example library has been written by Refinitiv for the only purpose of illustrating a series of articles published on the Refinitiv Developer Community. The _ValueAddObjectsForEMA_ example library has not been tested for a usage in production environments. Refinitiv cannot be held responsible for any issues that may happen if this project or the related source code is used in production or any other client environment.

## <a id="prerequisites" name="prerequisites"></a>Prerequisites

Required software components:

* [Enterprise Message API](https://developers.refinitiv.com/elektron/elektron-sdk-java) (1.1.0 or greater) - Refinitiv interface to the Refinitiv Real-Time Market Data environment
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java Development Kit - version 8

## <a id="implemented-features" name="implemented-features"></a>Implemented Features
_ValueAddObjectsForEMA_ is an example of a complementary library to be used alongside EMA in order to provide a higher level of abstraction. It provides objects and helpers that complement EMA and that simplify the coding of EMA applications. _ValueAddObjectsForEMA_ is implemented incrementally, in parallel with the EMA example applications built by Refinitiv Developer Advocates. It currently provides the following features: 

- Helpers to dispatch event and wait for conditions
- A synchronous and asynchronous subscribing modes.
- MarketPrice objects with a caching facility, a random access to field values and a full dictionary description associated to each field.
- Chain objects with a caching facility and a random access to chain constituents.

These features are implemented in the following packages:
- **com.refinitiv.platformservices.elektron.objects.common**  
This package contains common interfaces and classes used by other modules of the library. It also provides general helper classes used by example applications (e.g. Dispatcher).   
- **com.refinitiv.platformservices.elektron.objects.data**  
This package contains in-memory implementations of OMM Data that are required by other modules for caching received data in-memory.
- **com.refinitiv.platformservices.elektron.objects.marketprice**  
This package contains the interface and implementation class of MarketPrice objects that provides higher level features like: in-memory data caching, a random access to field values, full field description, synchronous/asynchronous subscription, completion status...<br>
_**Note:** For more details about the logic implemented by the MarketPrice objects, please refer to the [A simple MarketPrice object for EMA](https://developers.refinitiv.com/article/simple-marketprice-object-ema-part-1) article._
- **com.refinitiv.platformservices.elektron.objects.chain**  
This package contains the interface and implementation class of Chain objects that allow to automaticaly open chains and provides to their constituents.<br>
_**Note:** For more details about the logic implemented by the FlatChain and RecursiveChain objects, please refer to the [Simple Chain objects for EMA](https://developers.refinitiv.com/article/simple-chain-objects-ema-part-1) article._
- **com.refinitiv.platformservices.elektron.objects.examples.marketprice**  
This package contains an example application that demonstrates the MarketPrice objects capabilities and how to use them. The application starts by creating an EMA OmmConsumer and uses it in with MarketPrice objects in several individual steps that demonstrate the implemented features. Before each step, explanatory text is displayed and you are prompted to press to start the step.
- **com.refinitiv.platformservices.elektron.objects.examples.chain**  
This package contains an example application that demonstrates the FlatChain and RecursiveChain objects capabilities and how to use them. The application starts by creating an EMA OmmConsumer and uses it in with Chain objects in several individual steps that demonstrate the implemented features. Before each step, explanatory text is displayed and you are prompted to press to start the step.

The _ValueAddObjectsForEMA_ example library also comes with a Javadoc that fully describes the exposed APIs.

_**Note:** If you do not know yet about the Elektron Message API (EMA) and how to program and EMA consumer application I recommend you to follow this [EMA Quick Start](https://developers.refinitiv.com/elektron/elektron-sdk-java/quick-start?content=8656&type=quick_start) and these [EMA Tutorials](https://developers.refinitiv.com/elektron/elektron-sdk-java/learning)._

## <a id="building-the-valueaddobjectsforema" name="building-the-valueaddobjectsforema"></a>Building the *ValueAddObjectsForEMA* library

### Set the required environment variables

This package includes some convenient files which will enable the developer to quickly build and run the example application. These scripts rely on the *JAVA_HOME* and *ELEKTRON_JAVA_HOME* environment variables. These variables must be set appropriately before you run any of the *build* or *run* scripts.
* *JAVA_HOME* must be set with the root directory of your JDK 8 environment.
* *ELEKTRON_JAVA_HOME* must be set with the root directory of your (EMA) Elektron Java API installation

### Change the service name and DACS user name if need be

The *MarketPriceStepByStepExample.java* file and  *ChainStepByStepExample.java* file contain two hardcoded values that you may want to change depending on the TREP or Elektron Real-Time platform you use. These values indicate:

* The **service name** used to subscribe: The hardcoded value is "ELEKTRON_DD". This value can be changed thanks to the *SERVICE\_NAME* class members of the *MarketPriceStepByStepExample* and the *ChainStepByStepExample* classes.
* The **DACS user name** used to connect the application to the infrastructure. If the Data Access Control System (DACS) is activated on your TREP and if your DACS username is different than your operating system user name, you will need to set it thanks to the *DACS\_USER\_NAME* class members of the *MarketPriceStepByStepExample* and the *ChainStepByStepExample* classes.

### Run the *build* script

Once these environment variables setup and hardcoded values are properly set, you must run the *build-with-esdk-x.x.x.bat* or the *build-with-esdk-x.x.x.ksh* script to build the _ValueAddObjectsForEMA_ example library and the related example applications. 

_**Note:** The build script to be used must match the version of your installed Elektron SDK. For example: If you installed Elektron SDK 1.1.1 on a Windows machine then you should use the "build-with-esdk-1.1.x.bat" script. On the other hand, if you installed Elektron SDK 1.2.2 on a Linux machine then you should use the "build-with-esdk-1.2.2.ksh" script._
 

#### Expected output

    Building ValueAddObjectsForEMA...
    Building the ValueAddObjectsForEMA javadoc...
    Building the MarketPriceStepByStepExample application...
    Building the ChainStepByStepExample application...
    Building jar files...
    Copying dependencies...
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    1 File(s) copied
    ** ValueAddObjectsForEMA project successfully built **

_**Note:** Alternatively to the build scripts, you can use the NetBeans IDE to build the applications. NetBeans 8.2 project files are provided with the applications source code._

## <a id="running-the-example" name="running-the-example"></a>Running the example applications

### [Running the *MarketPriceStepByStepExample*](MarketPrice-README.md)

### [Running the *ChainStepByStepExample*](Chain-README.md)


## <a id="troubleshooting" name="troubleshooting"></a>Troubleshooting

**Q: When I build _ValueAddObjectsForEMA_ library or run the applications, it fails with an error like:**

    The system cannot find the path specified

**A:** The JAVA_HOME environment variable is not set, or set to the wrong path. See the [Building the *ValueAddObjectsForEMA* library](#building-the-valueaddobjectsforema) section above.

<br>

**Q: When I build the _ValueAddObjectsForEMA_ library I get warnings and "package ... does not exist" errors like:**

    Building the ValueAddObjectsForEMA library...
    warning: [path] bad path element "\Ema\Libs\ema.jar": no such file or directory
    warning: [path] bad path element "\Ema\Libs\SLF4J\slf4j-1.7.12\slf4j-api-1.7.12.jar": no such file or directory
    warning: [path] bad path element "\Ema\Libs\SLF4J\slf4j-1.7.12\slf4j-jdk14-1.7.12.jar": no such file or directory
    warning: [path] bad path element "\Ema\Libs\apache\commons-configuration-1.10.jar": no such file or directory
    warning: [path] bad path element "\Ema\Libs\apache\commons-logging-1.2.jar": nosuch file or directory
    warning: [path] bad path element "\Ema\Libs\apache\commons-lang-2.6.jar": no such file or directory
    warning: [path] bad path element "\Ema\Libs\apache\org.apache.commons.collections.jar": no such file or directory
    warning: [path] bad path element "\Eta\Libs\upa.jar": no such file or directory
    warning: [path] bad path element "\Eta\Libs\upaValueAdd.jar": no such file or directory
    src\com\thomsonreuters\platformservices\elektron\objects\common\Dispatcher.java:3: error: package com.thomsonreuters.ema.access does not exist
    import com.thomsonreuters.ema.access.OmmConsumer;
                                        ^
    src\com\thomsonreuters\platformservices\elektron\objects\common\Dispatcher.java:4: error: package com.thomsonreuters.ema.access does not exist
    import com.thomsonreuters.ema.access.OmmException;
                                        ^

**A:** The ELEKTRON_JAVA_HOME environment variable is not set, or set to the wrong path.  See the [Building the *ValueAddObjectsForEMA* library](#building-the-valueaddobjectsforema) section above.

<br>

**Q: When I run the example applications, I get a JNI error with a NoClassDefFoundError exception like:**

    Error: A JNI error has occurred, please check your installation and try again
    Exception in thread "main" java.lang.NoClassDefFoundError: com/thomsonreuters/ema/access/OmmException
            at java.lang.Class.getDeclaredMethods0(Native Method)
            at java.lang.Class.privateGetDeclaredMethods(Class.java:2701)
            at java.lang.Class.privateGetMethodRecursive(Class.java:3048)
            at java.lang.Class.getMethod0(Class.java:3018)
            at java.lang.Class.getMethod(Class.java:1784)
            at sun.launcher.LauncherHelper.validateMainClass(LauncherHelper.java:544)
            at sun.launcher.LauncherHelper.checkAndLoadMain(LauncherHelper.java:526)

    Caused by: java.lang.ClassNotFoundException: com.thomsonreuters.ema.access.OmmException
            at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
            at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
            at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:331)
            at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
            ... 7 more

**A:** The ELEKTRON_JAVA_HOME environment variable is not set, or set to the wrong path. See the [Building the *ValueAddObjectsForEMA* library](#building-the-valueaddobjectsforema) section above.

<br>

**Q: The example application is stuck after the *">>> Creating the OmmConsumer"* message is displayed.**

After a while the application displays an error like: 

      ERROR - Can't create the OmmConsumer because of the following error: login failed (timed out after waiting 45000 milliseconds) for 10.2.43.49:14002)

**A:** Verify that you properly set the *<host>* parameter in the EmaConfig.xml file (see [Running the *MarketPriceStepByStepExample*](MarketPrice-README.md) or  [Running the *ChainStepByStepExample*](Chain-README.md) for more). 
Ultimately, ask your TREP administrator to help you to investigate with TREP monitoring tools like adsmon.

 
## <a id="solution-code" name="solution-code"></a>Solution Code

The *ValueAddObjectsForEMA* example library was developed using the [Elektron SDK Java API](https://developers.thomsonreuters.com/elektron/elektron-sdk-java) that is available for download [here](https://developers.thomsonreuters.com/elektron/elektron-sdk-java/downloads).

### Built With

* [Elektron Message API](https://developers.thomsonreuters.com/elektron/elektron-sdk-java)
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [NetBeans 8.2](https://netbeans.org/) - IDE for Java development

### <a id="contributing" name="contributing"></a>Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

### <a id="authors" name="authors"></a>Authors

* **Olivier Davant** - Release 1.0.  *Initial version*

### <a id="license" name="license"></a>License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
