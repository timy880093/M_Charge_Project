@echo on
SET "zipCommandHome=C:\Program Files\7-Zip\7za920"
SET "backupFileFolder=D:\Temp\FTP\gateweb"
SET PGPASSWORD=postgres
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "targetHostUser=postgres"
SET "targetDatabaseName=test"

chcp 65001

REM 找到最新的檔案-只找附檔為7z的
for /f %%i in ('dir %backupFileFolder%\*.7z /b/a-d/od/t:c') do (SET lastCreatedFileName=%%i)

SET backupFilePath=%backupFileFolder%\%lastCreatedFileName%
SET unzipFolder=%backupFileFolder%

REM 解壓縮檔案
"%zipCommandHome%\7za" e -y %backupFilePath% -o%unzipFolder%
REM 取得解壓後的檔案
SET extractedFile=%backupFilePath:.7z=.sql% 
REM 清除local端的db連線
"%postgresHome%\psql" -U %targetHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log
REM 移除並建立新的DB
"%postgresHome%\psql" -U %targetHostUser% -c "DROP DATABASE %targetDatabaseName%;" 
"%postgresHome%\psql" -U %targetHostUser% -c "CREATE DATABASE %targetDatabaseName% WITH ENCODING = 'UTF8';" 
REM 從目標檔案還原
"%postgresHome%\psql" -U %targetHostUser% -d %targetDatabaseName% -f %extractedFile% 
REM 刪除解壓後的檔案
del /f %extractedFile%

