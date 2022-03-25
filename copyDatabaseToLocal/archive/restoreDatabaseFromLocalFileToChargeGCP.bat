@echo off
SET "targetDatabaseName=charge_db_prod"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "sourceHost=35.229.211.226"
SET "sourceHostUser=gateweb"
SET "backupFilePath=D:\Charge_db_backup\charge_db_prod_202001030500.dump"
SET PGPASSWORD=1qaz2wsx

"%postgresHome%\psql" -U %sourceHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log
"%postgresHome%\psql" -U %sourceHostUser% -c "DROP DATABASE %targetDatabaseName%;" > dropDatabase.log
"%postgresHome%\psql" -U %sourceHostUser% -c "CREATE DATABASE %targetDatabaseName% WITH ENCODING = 'UTF8';" > createDatabase.log
"%postgresHome%\pg_restore" -n public --clean --dbname=%targetDatabaseName% --host %sourceHost% --username %sourceHostUser% --verbose %backupFilePath% > pgDump.log
