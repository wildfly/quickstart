#!/usr/bin/env bash
# microprofile-reactive-messaging-kafka/.ci/before-test-quickstart.sh
# This image will be moved to SmallRye at some point
docker run --rm -d --name "microprofile-reactive-messaging-kafka-kafka" -p 9092:9092 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 quay.io/ogunalp/kafka-native:0.5.0-kafka-3.6.0
