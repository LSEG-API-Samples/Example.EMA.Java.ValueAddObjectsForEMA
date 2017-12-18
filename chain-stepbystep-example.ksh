#!/bin/ksh

#
# The following batch file assumes the following environment:
# 
#	JAVA_HOME - Root directory of your JDK 8 environment
# 	ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
#

JAVA=$JAVA_HOME/bin/java
EMA_HOME=$ELEKTRON_JAVA_HOME/Ema
ETA_HOME=$ELEKTRON_JAVA_HOME/Eta
BINDIR=build/classes
LOGGINGCONFIGPATH=-Djava.util.logging.config.file=./logging.properties
CLASSPATH=$BINDIR:$EMA_HOME/Libs/ema.jar:$EMA_HOME/Libs/SLF4J/slf4j-1.7.12/slf4j-api-1.7.12.jar:$EMA_HOME/Libs/SLF4J/slf4j-1.7.12/slf4j-jdk14-1.7.12.jar:$EMA_HOME/Libs/apache/commons-configuration-1.10.jar:$EMA_HOME/Libs/apache/commons-logging-1.2.jar:$EMA_HOME/Libs/apache/commons-lang-2.6.jar:$EMA_HOME/Libs/apache/org.apache.commons.collections.jar:$ETA_HOME/Libs/upa.jar:$ETA_HOME/Libs/upaValueAdd.jar:lib/commons-cli-1.4.jar:lib/json-20160810.jar

function run
{
   $JAVA $LOGGINGCONFIGPATH -cp $CLASSPATH com.thomsonreuters.platformservices.objects.elektron.examples.chain.ChainStepByStepExample $*
}

run

