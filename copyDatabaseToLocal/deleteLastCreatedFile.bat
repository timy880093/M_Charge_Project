@echo off
SET "backupFileFolder=D:\Temp\FTP\gateweb"
for /f %%i in ('dir %backupFileFolder% /b/a-d/od/t:c') do SET LASTFILENAME=%%i
del /f %backupFileFolder%\%LASTFILENAME%