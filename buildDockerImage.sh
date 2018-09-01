#!/usr/bin/env bash

set -eo pipefail

modules=( eureka-server user-service zuul )

for module in "${modules[@]}"; do
    docker build -t "trproject-test/${module}:latest" ${module}
done
