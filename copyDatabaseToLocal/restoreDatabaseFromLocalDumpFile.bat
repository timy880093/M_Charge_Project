@echo off
SET "targetDatabaseName=charge_db"
REM SET "targetDatabaseName=charge_db_uat"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "sourceHost=127.0.0.1"
REM SET "sourceHost=192.168.1.208"
SET "sourceHostUser=gateweb"
SET "backupFilePath=D:\Charge_db_backup\charge_db_prod_202005190500.dump"
SET PGPASSWORD=77183770

"%postgresHome%\psql" -U %sourceHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log
"%postgresHome%\psql" -U %sourceHostUser% -c "DROP DATABASE %targetDatabaseName%;" > dropDatabase.log
"%postgresHome%\psql" -U %sourceHostUser% -c "CREATE DATABASE %targetDatabaseName% WITH ENCODING = 'UTF8';" > createDatabase.log
"%postgresHome%\pg_restore" --dbname=%targetDatabaseName% --clean --host %sourceHost% --username %sourceHostUser% --verbose %backupFilePath% > pgDump.log
