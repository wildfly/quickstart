#!/bin/sh
set -x

# Start keycloak with required configuration
docker run -d --rm --name "keycloak" \
  -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -v ${GITHUB_WORKSPACE}/quickstarts/ejb-security-jwt/keycloak/realm:/opt/keycloak/data/import \
  quay.io/keycloak/keycloak:21.0.0 start-dev --import-realm
