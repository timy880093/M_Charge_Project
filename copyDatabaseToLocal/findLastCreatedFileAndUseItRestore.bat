@echo on
SET "backupFileFolder=D:\Temp\FTP\gateweb"
for /f %%i in ('dir %backupFileFolder% /b/a-d/od/t:c') do SET LASTFILENAME=%%i

SET PGPASSWORD=postgres
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "targetHostUser=postgres"
SET "targetDatabaseName=test"
SET backupFilePath=%backupFileFolder%\%LASTFILENAME%

REM 清除local端的db連線
ECHO clean database:%targetDatabaseName%'s connections.
"%postgresHome%\psql" -U %targetHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log
REM 移除並建立新的DB
ECHO drop database %targetDatabaseName%.
"%postgresHome%\psql" -U %targetHostUser% -c "DROP DATABASE %targetDatabaseName%;" > dropDatabase.log
ECHO create database %targetDatabaseName%.
"%postgresHome%\psql" -U %targetHostUser% -c "CREATE DATABASE %targetDatabaseName% WITH ENCODING = 'UTF8';" > createDatabase.log
REM 從目標檔案還原
ECHO restore database from file %backupFilePath%.
"%postgresHome%\psql" -U %targetHostUser% -d %targetDatabaseName% -f %backupFilePath% > restoreDatabase.log