@echo off
 
Set RegQry=HKLM\Hardware\Description\System\CentralProcessor\0
 
REG.exe Query %RegQry% > nodejs\checkOS.txt
 
Find /i "x86" < nodejs\checkOS.txt > nodejs\StringCheck.txt

DEL nodejs\checkOS.txt
DEL nodejs\StringCheck.txt
 
If %ERRORLEVEL% == 0 (
    nodejs\windowsx86\node http.js
) ELSE (
    nodejs\windowsx64\node http.js
)