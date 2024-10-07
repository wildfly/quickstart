#!/usr/bin/env bash
# todo-jakarta-data/.ci/before-test-quickstart.sh
docker run --rm -d --name todo-jakarta-data-db -e POSTGRES_USER=todos -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 postgres