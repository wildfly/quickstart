#!/bin/sh
docker run --name todo-backend-db -e POSTGRES_USER=todos -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
