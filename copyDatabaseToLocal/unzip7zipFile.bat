@echo on
SET "zipCommandHome=C:\Program Files\7-Zip\7za920"
SET "backupFileFolder=D:\Temp\FTP\gateweb"
for /f %%i in ('dir %backupFileFolder%\*.7z /b/a-d/o:d/t:c') 
IF EXIST "temp.txt" (
    do SET lastCreatedFileName=%%i
) ELSE (
    
)

SET backupFilePath=%backupFileFolder%\%lastCreatedFileName%
SET unzipFolder=%backupFileFolder%

ECHO unzip last created file in %backupFilePath%
"%zipCommandHome%\7za" e -y %backupFilePath% -o%unzipFolder%

