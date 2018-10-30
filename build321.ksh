#!/bin/ksh

#
# The following batch file assumes the following environment:
# 
#	JAVA_HOME - Root directory of your JDK 8 environment
# ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
#

BINDIR=build/classes
if [ ! -d $BINDIR ]; then
  mkdir -p $BINDIR
fi
JAVADOCDIR=dist/javadoc
DISTDIR=dist

EMA_HOME=$ELEKTRON_JAVA_HOME/Java/Ema
ETA_HOME=$ELEKTRON_JAVA_HOME/Java/Eta
EMA_BINARY_PACK=$ELEKTRON_JAVA_HOME/Elektron-SDK-BinaryPack/Java/Ema
ETA_BINARY_PACK=$ELEKTRON_JAVA_HOME/Elektron-SDK-BinaryPack/Java/Eta

JAVAC=$JAVA_HOME/bin/javac
JAVADOC=$JAVA_HOME/bin/javadoc
JAR=$JAVA_HOME/bin/jar

CLASSPATH=./src:$EMA_HOME/Libs/ema-3.2.1.0.jar:$EMA_BINARY_PACK/Libs/SLF4J/slf4j-1.7.12/slf4j-api-1.7.12.jar:$EMA_BINARY_PACK/Libs/SLF4J/slf4j-1.7.12/slf4j-jdk14-1.7.12.jar:$EMA_BINARY_PACK/Libs/apache/commons-configuration-1.10.jar:$EMA_BINARY_PACK/Libs/apache/commons-logging-1.2.jar:$EMA_BINARY_PACK/Libs/apache/commons-lang-2.6.jar:$EMA_BINARY_PACK/Libs/apache/commons-collections-3.2.2.jar:$ETA_HOME/Libs/upa-3.2.1.0.jar:$ETA_HOME/Libs/upaValueAdd-3.2.1.0.jar

echo $CLASSPATH

function build
{
   printf "Building ValueAddObjectsForEMA...\n"
   $JAVAC -Xlint -cp $CLASSPATH -d $BINDIR src/com/thomsonreuters/platformservices/elektron/objects/common/*.java; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi
   $JAVAC -Xlint -cp $CLASSPATH -d $BINDIR src/com/thomsonreuters/platformservices/elektron/objects/data/*.java; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi
   $JAVAC -Xlint -cp $CLASSPATH -d $BINDIR src/com/thomsonreuters/platformservices/elektron/objects/marketprice/*.java; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi
   $JAVAC -Xlint -cp $CLASSPATH -d $BINDIR src/com/thomsonreuters/platformservices/elektron/objects/chain/*.java; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi

   printf "Building the ValueAddObjectsForEMA javadoc...\n"
   $JAVADOC -d $JAVADOCDIR -cp $CLASSPATH -quiet -sourcepath src com.thomsonreuters.platformservices.elektron.objects.common com.thomsonreuters.platformservices.elektron.objects.data com.thomsonreuters.platformservices.elektron.objects.marketprice com.thomsonreuters.platformservices.elektron.objects.chain; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi

   printf "Building the MarketPriceStepByStepExample application...\n"
   $JAVAC -Xlint -cp $CLASSPATH -d $BINDIR src/com/thomsonreuters/platformservices/elektron/objects/examples/marketprice/*.java; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi

   printf "Building the ChainStepByStepExample application...\n"
   $JAVAC -Xlint -cp $CLASSPATH -d $BINDIR src/com/thomsonreuters/platformservices/elektron/objects/examples/chain/*.java; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi

   printf "Building jar files...\n"
   $JAR cfm $DISTDIR/ValueAddObjectsForEMA.jar Manifest321.txt -C build/classes .; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi
   $JAR cf  $DISTDIR/ValueAddObjectsForEMA-javadoc.jar -C $JAVADOCDIR .; ret=$?
   if [ $ret != 0 ]; then
      printf "Build failed.  Exiting\n"
      exit $ret
   fi

   printf "Copying dependencies...\n"
  cp -f $EMA_HOME/Libs/ema-3.2.1.0.jar $DISTDIR
  cp -f $EMA_BINARY_PACK/Libs/SLF4J/slf4j-1.7.12/slf4j-api-1.7.12.jar $DISTDIR
  cp -f $EMA_BINARY_PACK/Libs/SLF4J/slf4j-1.7.12/slf4j-jdk14-1.7.12.jar $DISTDIR
  cp -f $EMA_BINARY_PACK/Libs/apache/commons-configuration-1.10.jar $DISTDIR
  cp -f $EMA_BINARY_PACK/Libs/apache/commons-logging-1.2.jar $DISTDIR
  cp -f $EMA_BINARY_PACK/Libs/apache/commons-lang-2.6.jar $DISTDIR
  cp -f $EMA_BINARY_PACK/Libs/apache/commons-collections-3.2.2.jar $DISTDIR
  cp -f $ETA_HOME/Libs/upa-3.2.1.0.jar $DISTDIR
  cp -f $ETA_HOME/Libs/upaValueAdd-3.2.1.0.jar $DISTDIR
  cp -f ../lib/commons-cli-1.4.jar $DISTDIR
  cp -f ../lib/json-20160810.jar $DISTDIR

}

build

printf "** ValueAddObjectsForEMA project successfully built **\n"
