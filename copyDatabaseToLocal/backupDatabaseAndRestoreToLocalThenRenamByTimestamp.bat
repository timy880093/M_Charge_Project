@echo off
SET "backupFilePath=D:\charge_db_regularBack.sql"
SET "sourceDatabaseName=charge_db"
SET "targetDatabaseName=test"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "sourceHost=127.0.0.1"
SET "sourceHostUser=postgres"
SET "targetHostUser=postgres"
SET "PGPASSWORD=postgres"

REM 從目標db備份成檔案
"%postgresHome%\pg_dump" --dbname=%sourceDatabaseName% --encoding=utf8 --clean -h %sourceHost% -U %sourceHostUser% --file=%backupFilePath% > pgDump.log
REM 清除local端的db連線
"%postgresHome%\psql" -U %targetHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log
REM 移除並建立新的DB
"%postgresHome%\psql" -U %targetHostUser% -c "DROP DATABASE %targetDatabaseName%;" > dropDatabase.log
"%postgresHome%\psql" -U %targetHostUser% -c "CREATE DATABASE %targetDatabaseName% WITH ENCODING = 'UTF8';" > createDatabase.log
REM 從目標檔案還原
"%postgresHome%\psql" -U %targetHostUser% -d %targetDatabaseName% -f %backupFilePath% > restoreDatabase.log

REM 於bat檔的for變數要寫為%%a,測試可以用%a
FOR /f "tokens=2 delims==" %%a IN ('wmic OS Get localdatetime /value') DO SET "dt=%%a"
REM 切分資料
SET "timestamp=%dt:~0,14%"
REM 修改目標檔案名稱
SET "newFileName=%sourceDatabaseName%_%timestamp%.sql"
REM REN 目標完整路徑 新檔名
REN "%backupFilePath%" "%newFileName%"
