@echo off
SET "sourceDatabaseName=receipt_db"
SET "postgresHome=C:\Program Files\PostgreSQL\11\bin"
SET "sourceHost=35.221.177.67"
SET "sourceHostUser=gateweb"
SET "backupFilePath=D:\Charge_db_backup\%sourceDatabaseName%_IASR_20200720_beforeRegen.dump"
SET "tableName=invoice_amount_summary_report"
SET PGPASSWORD=gateweb87734300

"%postgresHome%\pg_dump" --dbname=%sourceDatabaseName% -Fc --encoding=utf8 --clean --table=%tableName% -h %sourceHost% -U %sourceHostUser% --file=%backupFilePath% > pgDump.log
