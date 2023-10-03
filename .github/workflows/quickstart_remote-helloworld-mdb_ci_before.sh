#!/bin/sh
docker run -d --rm --name artemis -e AMQ_USER=admin -e AMQ_PASSWORD=admin -p8161:8161 -p61616:61616 -e AMQ_DATA_DIR=/home/jboss/data quay.io/artemiscloud/activemq-artemis-broker-kubernetes