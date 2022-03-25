@echo off
SET "sourceDatabaseName=charge_db"
SET "targetDatabaseName=test"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "sourceHost=192.168.1.208"
SET "sourceHostUser=gateweb"
SET "targetHostUser=postgres"
SET "backupFilePath=D:\charge_db_20200323_gcp.sql"

"%postgresHome%\psql" -U %targetHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log
"%postgresHome%\psql" -U %targetHostUser% -c "DROP DATABASE %targetDatabaseName%;" > dropDatabase.log
"%postgresHome%\psql" -U %targetHostUser% -c "CREATE DATABASE %targetDatabaseName% WITH ENCODING = 'UTF8';" > createDatabase.log
"%postgresHome%\pg_dump" --dbname=%sourceDatabaseName% --encoding=utf8 --clean -h %sourceHost% -U %sourceHostUser% --file=%backupFilePath% > pgDump.log
"%postgresHome%\psql" --dbname=%targetDatabaseName% -U %targetHostUser% --file=%backupFilePath%  > pgRestore.log