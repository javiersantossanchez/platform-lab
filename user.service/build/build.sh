#!/bin/sh
echo 'Start User Service Build'

echo 'Create tmp folder'
mkdir tmp

echo 'Copy resources into tmp folder'
cp -r ../src  tmp/.
cp ../package*.json  tmp/.


docker build -t 'com.microservice.platform.user.service' .

#rm -r tmp

echo 'End User Service Build'
