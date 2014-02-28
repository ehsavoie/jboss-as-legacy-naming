#!/bin/sh

mvn clean install -Dcheckstyle.skip=false -s config/settings.xml -Plegacy $1 $2 $3 $4
