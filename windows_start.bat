@echo off

REM Written By: Richie Preece
REM richie@minetrocity.com
REM Copyright 2013 Minetrocity
REM All Rights Reserved

REM All npm distros can be found at http://nodejs.org/dist/npm/
REM All node distros can be found at http://nodejs.org/download/

REM Check for x86 vs x64 architecture. If 64, go to 64, otherwise fall through
if %PROCESSOR_ARCHITECTURE% == AMD64 goto 64BIT

:32BIT
REM Verify that latest version of all node_modules are installed
echo Verifying installation of dependencies
REM This is how you run npm with node.js
%~dp0\binaries\win32\node %~dp0\binaries\npm\cli.js install
REM Then start server
echo You have a 32-bit system
%~dp0\binaries\win32\node app.js
goto END

:64BIT
REM Verify that latest version of all node_modules are installed
echo Verifying installation of dependencies
REM This is how you run npm with node.js
%~dp0\binaries\win64\node %~dp0\binaries\npm\cli.js install
REM Then start server
echo You have a 64-bit system
%~dp0\binaries\win64\node app.js
goto END

:END

REM You'll probably never get here, but if you do, here's a nice message
echo Shutting down...