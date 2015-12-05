@echo off
cd /d %~dp0
pause
set libpath=.;./lib
for /R %~dp0 %%c in (*.jar) do call getlibpath %%c

echo on
java -cp %libpath% com.vastcm.stuhealth.client.MainUI
pause