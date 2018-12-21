@echo off
setlocal

rem
rem The following batch file assumes the following environment:
rem 
rem		JAVA_HOME - Root directory of your JDK 8 environment
rem 	ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
rem		

set SCRIPT=%0
set BINDIR=build\classes
set JAVADOCDIR=dist\javadoc
set DISTDIR=dist
set ETA_EMA_VERSION=%1
set EMA_HOME=%ELEKTRON_JAVA_HOME%\Java\Ema
set ETA_HOME=%ELEKTRON_JAVA_HOME%\Java\Eta
set EMA_BINARY_PACK=%ELEKTRON_JAVA_HOME%\Elektron-SDK-BinaryPack\Java\Ema
set ETA_BINARY_PACK=%ELEKTRON_JAVA_HOME%\Elektron-SDK-BinaryPack\Java\Eta

set JAVAC="%JAVA_HOME%\bin\javac"
set JAVADOC="%JAVA_HOME%\bin\javadoc"
set JAR="%JAVA_HOME%\bin\jar"



set CLASSPATH=%BINDIR%;%EMA_HOME%\Libs\ema-%ETA_EMA_VERSION%.jar;%EMA_BINARY_PACK%\Libs\SLF4J\slf4j-1.7.12\slf4j-api-1.7.12.jar;%EMA_BINARY_PACK%\Libs\SLF4J\slf4j-1.7.12\slf4j-jdk14-1.7.12.jar;%EMA_BINARY_PACK%\Libs\apache\commons-configuration-1.10.jar;%EMA_BINARY_PACK%\Libs\apache\commons-logging-1.2.jar;%EMA_BINARY_PACK%\Libs\apache\commons-lang-2.6.jar;%EMA_BINARY_PACK%\Libs\apache\commons-collections-3.2.2.jar;%ETA_HOME%\Libs\upa-%ETA_EMA_VERSION%.jar;%ETA_HOME%\Libs\upaValueAdd-%ETA_EMA_VERSION%.jar

if not exist %BINDIR% (mkdir %BINDIR%)

echo Building ValueAddObjectsForEMA...
%JAVAC% -Xlint -d %BINDIR% src\com\thomsonreuters\platformservices\elektron\objects\common\*.java
if %errorlevel% neq 0 goto :ERROR
%JAVAC% -Xlint  -d %BINDIR% src\com\thomsonreuters\platformservices\elektron\objects\data\*.java
if %errorlevel% neq 0 goto :ERROR
%JAVAC% -Xlint  -d %BINDIR% src\com\thomsonreuters\platformservices\elektron\objects\marketprice\*.java
if %errorlevel% neq 0 goto :ERROR
%JAVAC% -Xlint  -d %BINDIR% src\com\thomsonreuters\platformservices\elektron\objects\chain\*.java
if %errorlevel% neq 0 goto :ERROR


echo Building the ValueAddObjectsForEMA javadoc...
%JAVADOC% -d %JAVADOCDIR% -quiet -sourcepath src com.thomsonreuters.platformservices.elektron.objects.common com.thomsonreuters.platformservices.elektron.objects.data com.thomsonreuters.platformservices.elektron.objects.marketprice com.thomsonreuters.platformservices.elektron.objects.chain
if %errorlevel% neq 0 goto :ERROR

echo Building the MarketPriceStepByStepExample application...
%JAVAC% -Xlint -d %BINDIR% src\com\thomsonreuters\platformservices\elektron\objects\examples\marketprice\*.java
if %errorlevel% neq 0 goto :ERROR

echo Building the ChainStepByStepExample application...
%JAVAC% -Xlint -d %BINDIR% src\com\thomsonreuters\platformservices\elektron\objects\examples\chain\*.java
if %errorlevel% neq 0 goto :ERROR

echo Building jar files...
%JAR% cfm %DISTDIR%\ValueAddObjectsForEMA.jar Manifest321.txt -C build\classes .
%JAR% cf %DISTDIR%\ValueAddObjectsForEMA-javadoc.jar -C %JAVADOCDIR% .
if %errorlevel% neq 0 goto :ERROR

echo Copying dependencies...
xcopy /Y /Q "%EMA_HOME%\Libs\ema-%ETA_EMA_VERSION%.jar" %DISTDIR%
xcopy /Y /Q "%EMA_BINARY_PACK%\Libs\SLF4J\slf4j-1.7.12\slf4j-api-1.7.12.jar" %DISTDIR%
xcopy /Y /Q "%EMA_BINARY_PACK%\Libs\SLF4J\slf4j-1.7.12\slf4j-jdk14-1.7.12.jar" %DISTDIR%
xcopy /Y /Q "%EMA_BINARY_PACK%\Libs\apache\commons-configuration-1.10.jar" %DISTDIR%
xcopy /Y /Q "%EMA_BINARY_PACK%\Libs\apache\commons-logging-1.2.jar" %DISTDIR%
xcopy /Y /Q "%EMA_BINARY_PACK%\Libs\apache\commons-lang-2.6.jar" %DISTDIR%
xcopy /Y /Q "%EMA_BINARY_PACK%\Libs\apache\commons-collections-3.2.2.jar" %DISTDIR%
xcopy /Y /Q "%ETA_HOME%\Libs\upa-%ETA_EMA_VERSION%.jar" %DISTDIR%
xcopy /Y /Q "%ETA_HOME%\Libs\upaValueAdd-%ETA_EMA_VERSION%.jar" %DISTDIR%

goto :DONE

:ERROR
echo.
echo Build failed.  Exiting.
goto :EOF

:DONE
echo ** ValueAddObjectsForEMA project successfully built **
