@REM ================================================
@REM Author     :  yangyihao
@REM Date       :  2013-05-09
@REM Description:  launcher of stuhealth client.
@REM ================================================

@echo off
REM cd /d %~dp0
set APPHOME=C:\stuhealth\app

IF "%1" == "direct" ( GOTO APP ) ELSE ( GOTO UPDATER )

:APP
  echo direct...
  set libpath=%APPHOME%;%APPHOME%/lib
  for /R %APPHOME% %%c in (*.jar) do call %APPHOME%/bin/getlibpath %%c
  start javaw -cp %libpath% -DappHome=%APPHOME% com.vastcm.stuhealth.client.LoginUI
  
EXIT

:UPDATER
  echo autoupdate...
  echo %APPHOME%
  set libpath=%APPHOME%/lib/autoupdater.jar;%APPHOME%/lib/jupidator-0.8.0.jar;%APPHOME%/lib/log4j-1.2.17.jar;%APPHOME%/lib/slf4j-api-1.7.2.jar;%APPHOME%/lib/slf4j-log4j12-1.7.2.jar
  start javaw -cp %libpath% -DappHome=%APPHOME% com.vastcm.stuhealth.client.autoupdate.AppUpdater
  echo autoupdate complete.
  
EXIT

pause