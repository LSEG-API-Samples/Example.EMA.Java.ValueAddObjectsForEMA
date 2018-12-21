#!/bin/ksh

#
# The following batch file assumes the following environment:
# 
#	JAVA_HOME - Root directory of your JDK 8 environment
# 	ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
#

ETA_EMA_VERSION=$1
EMA_HOME=$ELEKTRON_JAVA_HOME/Java/Ema
ETA_HOME=$ELEKTRON_JAVA_HOME/Java/Eta
EMA_BINARY_PACK=$ELEKTRON_JAVA_HOME/Elektron-SDK-BinaryPack/Java/Ema
ETA_BINARY_PACK=$ELEKTRON_JAVA_HOME/Elektron-SDK-BinaryPack/Java/Eta
BINDIR=build/classes

JAVA=$JAVA_HOME/bin/java
LOGGINGCONFIGPATH=-Djava.util.logging.config.file=./logging.properties

CLASSPATH=./src:$EMA_HOME/Libs/ema-$ETA_EMA_VERSION.jar:$EMA_BINARY_PACK/Libs/SLF4J/slf4j-1.7.12/slf4j-api-1.7.12.jar:$EMA_BINARY_PACK/Libs/SLF4J/slf4j-1.7.12/slf4j-jdk14-1.7.12.jar:$EMA_BINARY_PACK/Libs/apache/commons-configuration-1.10.jar:$EMA_BINARY_PACK/Libs/apache/commons-logging-1.2.jar:$EMA_BINARY_PACK/Libs/apache/commons-lang-2.6.jar:$EMA_BINARY_PACK/Libs/apache/commons-collections-3.2.2.jar:$ETA_HOME/Libs/upa-$ETA_EMA_VERSION.jar:$ETA_HOME/Libs/upaValueAdd-$ETA_EMA_VERSION.jar


function run
{
   $JAVA $LOGGINGCONFIGPATH -cp $CLASSPATH com.thomsonreuters.platformservices.objects.elektron.examples.marketprice.MarketPriceStepByStepExample $*
}

run

