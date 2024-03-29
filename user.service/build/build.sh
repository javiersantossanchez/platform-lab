#!/bin/sh

echo 'Start User Service Build'

echo 'Remove tmp folder if there is'
rm -r tmp

echo 'Create tmp folder'
mkdir tmp

echo 'Copy resources into tmp folder'
cp -r ../src  tmp/.
cp ../package*.json  tmp/.




echo 'Build docker image'
VERSION_NUMBER=$(jq .version ../package.json)
VERSION_NUMBER=$(echo $VERSION_NUMBER | sed -e 's/^"//' -e 's/"$//') 
#echo $VERSION_NUMBER
docker build -t "com.microservice.platform.user.service:${VERSION_NUMBER}" .

echo 'Remove tmp folder'
rm -r tmp

echo 'End User Service Build'
