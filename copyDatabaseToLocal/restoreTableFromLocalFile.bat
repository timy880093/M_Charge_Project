@echo off
SET "targetDatabaseName=charge_db"
REM SET "targetDatabaseName=charge_db"
REM SET "targetDatabaseName=charge_db_prod"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
REM SET "sourceHost=35.229.211.226"
SET "sourceHost=127.0.0.1"
REM SET "sourceHost=192.168.1.208"
SET "sourceHostUser=gateweb"
SET "backupFilePath=D:\Charge_db_backup\receipt_db_IASR_20200615_beforeRegen.dump"
SET "tableName=invoice_amount_summary_report"
SET PGPASSWORD=77183770
REM SET PGPASSWORD=1qaz2wsx

"%postgresHome%\pg_restore" --dbname=%targetDatabaseName% --clean --host %sourceHost% --username %sourceHostUser% --table=%tableName% --verbose %backupFilePath% > pgDump.log