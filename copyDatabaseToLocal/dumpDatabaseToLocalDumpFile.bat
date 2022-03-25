@echo off
REM SET "sourceDatabaseName=charge_db_uat"
SET "sourceDatabaseName=charge_db"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
REM SET "sourceHost=35.229.211.226"
REM SET "sourceHost=192.168.1.208"
SET "sourceHost=127.0.0.1"
SET "sourceHostUser=gateweb"
SET "backupFilePath=D:\Charge_db_backup\charge_db_local_20200519_clean.dump"
REM SET PGPASSWORD=1qaz2wsx
SET PGPASSWORD=77183770
REM SET PGPASSWORD=gateweb87734300

"%postgresHome%\pg_dump" -Fc --dbname=%sourceDatabaseName% --encoding=utf8 --clean -h %sourceHost% -U %sourceHostUser% --file=%backupFilePath% > pgDump.log