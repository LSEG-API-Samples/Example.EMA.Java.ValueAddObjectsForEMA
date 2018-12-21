#!/bin/ksh

#
# The following batch file assumes the following environment:
# 
#	JAVA_HOME - Root directory of your JDK 8 environment
# 	ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
#

ETA_EMA_VERSION=3.2.0.1

function run
{
   ./build-generic.bat $ETA_EMA_VERSION
}

run

