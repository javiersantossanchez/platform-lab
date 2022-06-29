#!/bin/sh

echo 'Start User Microservice deployment at local k8'

echo 'Starting minikube'
minikube start

echo 'Upload image to minikube'
minikube image load com.microservice.platform.user.service

echo 'Deploy User Microservice'
minikube kubectl -- apply -f user-microservice-service.yaml
minikube kubectl -- apply -f user-microservice-deployment.yaml

echo 'End User Microservice deployment at local k8'