#!/bin/bash

SECRET_NAME="score-server-secret"

export $(sops -d secret.env | xargs)

kubectl -n=score-server delete secret ${SECRET_NAME} 2> /dev/null
kubectl -n=score-server \
create secret generic ${SECRET_NAME} \
--from-literal="REDIRECTION_URL=$REDIRECTION_URL" \
--from-literal="DB_USERNAME=$DB_USERNAME" \
--from-literal="DB_PASSWORD=$DB_PASSWORD" \
--from-literal="DB_URL=$DB_URL" \
--from-literal="DB_NAME=$DB_NAME" \
