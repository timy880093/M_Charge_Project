@echo off
SET "targetDatabaseName=charge_db"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "targetHostUser=postgres"
SET PGPASSWORD=p@s$w0rd
"%postgresHome%\psql" -U %targetHostUser% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname='%targetDatabaseName%' AND pid <> pg_backend_pid();" > dropConnection.log