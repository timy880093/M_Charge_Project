#!/bin/bash
newfilesize=$(stat --format=%s "/home/gateweb/charge_db_prod_20190912.dump.redundant")
lastfilesize=$(cat /home/gateweb/lastfilesize.log)

if [ $newfilesize -gt $lastfilesize ]
then
    echo "yes{$newfilesize, $lastfilesize}"
else
    echo "no"
fi
