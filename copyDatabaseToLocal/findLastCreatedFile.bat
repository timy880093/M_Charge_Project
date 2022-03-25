@echo off
SET "backupFileFolder=D:\Charge_db_backup"
for /f %%i in ('dir %backupFileFolder% /b/a-d/od/t:c') do SET LASTFILENAME=%%i
