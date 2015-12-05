@REM ================================================
@REM Author     :  yangyihao
@REM Date       :  2013-05-09
@REM Description:  launcher of stuhealth client.
@REM ================================================

@echo on
REM cd /d %~dp0
call set_env.bat
start %JAVA_HOME%\bin\javaw -cp %APPHOME%/lib/stuhealth_client_prelude.jar -DappHome=%APPHOME% com.vastcm.stuhealth.client.prelude.Cleaner 

set libpath=%APPHOME%;%APPHOME%/lib
for /R %APPHOME% %%c in (*.jar) do call %APPHOME%/bin/getlibpath %%c
start %JAVA_HOME%\bin\javaw -cp %libpath% -DappHome=%APPHOME% -DupdateXmlUrl=%UPDATEXML% com.vastcm.stuhealth.client.LoginUI

exit
  
