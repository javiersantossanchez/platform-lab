#!/bin/sh

# to start k8
#minikube start
#minikube cache add

minikube image load com.microservice.platform.general.service
minikube kubectl -- apply -f general-service.yaml
minikube kubectl -- apply -f general-service-deployment.yaml

