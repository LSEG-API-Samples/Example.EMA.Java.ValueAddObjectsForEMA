@echo off
setlocal

rem
rem The following batch file assumes the following environment:
rem 
rem		JAVA_HOME - Root directory of your JDK 8 environment
rem 	ELEKTRON_JAVA_HOME - Root directory of your (EMA) Elektron Java API installation
rem	

set ETA_EMA_VERSION=3.2.1.0

.\chain-stepbystep-example-generic.bat %ETA_EMA_VERSION%
