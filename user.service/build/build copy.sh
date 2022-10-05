#!/bin/sh

echo 'Start User Service Build'

echo 'Remove tmp folder if there is'
rm -r tmp

echo 'Create tmp folder'
mkdir tmp

echo 'Copy resources into tmp folder'
#cp -r ../src  tmp/.
#cp ../package*.json  tmp/.

var1="adadadasdsada"
var2=`sed -e 's/^"//' -e 's/"$//' <<<"$var1"`
echo $var2

echo 'Build docker image'
VERSION_NUMBER=$(jq .version ../package.json)
#VERSION_NUMBER=$(sed -e 's/^"//' -e 's/"$//' <<< $VERSION_NUMBER)
echo $VERSION_NUMBER
docker build -t 'com.microservice.platform.user.service' .

echo 'Remove tmp folder'
rm -r tmp

echo 'End User Service Build'
