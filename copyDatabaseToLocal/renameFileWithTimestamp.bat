REM 於bat檔的for變數要寫為%%a,測試可以用%a
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
REM 切分資料
SET timestamp=%dt:~0,14%

ren pgDump.log pgDump_%timestamp%.log