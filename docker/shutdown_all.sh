#!/bin/bash

docker compose -f ./tmp/docker-services-base.yml --env-file app.env down