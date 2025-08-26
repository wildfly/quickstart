#!/bin/sh

# Start greenmail with the required configuration
docker run -d --rm --name "greenmail" \
  -p 1465:3465 \
  -p 1993:3993 \
  -p 1025:3025 \
  -p 1110:3110 \
  -p 8081:8080 \
  -p 1143:3143 \
  greenmail/standalone:2.0.1