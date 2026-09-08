#!/usr/bin/env bash
docker run -d --rm --name "ejb-txn-remote-call-db" -p 5432:5432 -e POSTGRES_DB=test -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test postgres:9.4 -c max-prepared-transactions=110 -c log-statement=all
