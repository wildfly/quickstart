#!/usr/bin/env bash
# todo-backend/.ci/before-test-quickstart.sh
docker run --rm -d --name todo-backend-db -e POSTGRES_USER=todos -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 postgres