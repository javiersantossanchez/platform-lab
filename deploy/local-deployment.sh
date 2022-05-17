#!/bin/sh

#All the sonar section is comment because its no ready
# Sonar Configuration
#  Check the sonar instance is running
#response=$(curl --request GET -u admin:1234  --header "Content-Type: text/plain"  --url 'http://localhost:9000/api/system/ping')
#if [ "$response" != 'pong' ]; then
#  echo 'Starting SonarQube'
#  docker-compose up -d
#  response='no-started'
#  while [ "$response" != 'pong' ]; do
#    sleep 5s
#    response=$(curl --request GET -u admin:1234  --header "Content-Type: text/plain"  --url 'http://localhost:9000/api/system/ping')
#  done
#  echo 'SonarQube ready'
#fi

#TODO: I need to complete the logic to create a new project and a new token (I need to validate if the project was created)
#token=26cbec4319df0ed1c5143c0637751f645650dd01
#token=$(curl --request POST -u admin:1234 --url 'http://localhost:9000/api/user_tokens/generate?name=microservice-platform' --header "Content-Type: application/json" | jq -r '.token')
#echo "$token"
#curl --request POST -u 26cbec4319df0ed1c5143c0637751f645650dd01: --url 'http://localhost:9000/api/projects/create?name=microservice-platform&project=microservice-platform' --header "Content-Type: application/json"
#End sonnar configuration

echo "Starting Platform"
docker-compose  \
-f docker-compose-infra-structure.yml \
-f ../general.service/deployment/docker-compose.yml \
up keykloak-security krakend-proxy general-service graphite  grafana influxdb

#
#  grafana URL: http://localhost:3000/
#  graphite URL: http://localhost/
#  krakend  URL: http:localhost:8080
#  general-service  URL: http://localhost:8889
#
