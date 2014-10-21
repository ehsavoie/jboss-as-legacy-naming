#!/bin/sh

mvn clean install -s config/settings.xml $1 $2 $3 $4 -Dcheckstyle.skip=false
