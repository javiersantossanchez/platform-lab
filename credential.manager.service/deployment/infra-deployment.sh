#!/bin/sh

parameterCommand=$1

case $parameterCommand in
   'start')
      echo 'Starting infrastructure'
      docker-compose -f infra-docker-compose.yml up -d
      ;;
   'stop')
      echo 'Stopping infrastructure'
      docker-compose -f infra-docker-compose.yml down
      ;;
   *)
     echo 'Invalid Option'
     ;;
esac