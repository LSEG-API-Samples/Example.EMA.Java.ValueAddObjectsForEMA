@echo off
setlocal

rem
rem The following batch file assumes the following environment:
rem 
rem		JAVA_HOME - Root directory of your JDK 8 environment
rem 	ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
rem	

set BINDIR=build\classes
set EMA_HOME="%ELEKTRON_JAVA_HOME%"\Ema
set ETA_HOME="%ELEKTRON_JAVA_HOME%"\Eta

rem Java runtime.  Default runtime in path.
set JAVA="%JAVA_HOME%\bin\java"
set LOGGINGCONFIGPATH=-Djava.util.logging.config.file=.\logging.properties
set CLASSPATH=%BINDIR%;%EMA_HOME%\Libs\ema.jar;%EMA_HOME%\Libs\SLF4J\slf4j-1.7.12\slf4j-api-1.7.12.jar;%EMA_HOME%\Libs\SLF4J\slf4j-1.7.12\slf4j-jdk14-1.7.12.jar;%EMA_HOME%\Libs\apache\commons-configuration-1.10.jar;%EMA_HOME%\Libs\apache\commons-logging-1.2.jar;%EMA_HOME%\Libs\apache\commons-lang-2.6.jar;%EMA_HOME%\Libs\apache\org.apache.commons.collections.jar;%ETA_HOME%\Libs\upa.jar;%ETA_HOME%\Libs\upaValueAdd.jar;lib\commons-cli-1.4.jar;lib\json-20160810.jar

%JAVA%  %LOGGINGCONFIGPATH% -cp %CLASSPATH% com.thomsonreuters.platformservices.elektron.objects.examples.chain.ChainStepByStepExample %*
