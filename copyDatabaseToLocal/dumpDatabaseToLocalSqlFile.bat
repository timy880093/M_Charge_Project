@echo off
SET "sourceDatabaseName=charge_db_uat"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "sourceHost=192.168.1.208"
SET "sourceHostUser=gateweb"
SET "backupFilePath=D:\charge_db_20200406_uat.dump"
@SET PGPASSWORD=1qaz2wsx
SET PGPASSWORD=77183770

"%postgresHome%\pg_dump" --dbname=%sourceDatabaseName% --encoding=utf8 --clean -h %sourceHost% -U %sourceHostUser% --file=%backupFilePath% > pgDump.log