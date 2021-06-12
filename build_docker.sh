#!/usr/bin/env bash
set -euo pipefail
# Script to build docker container

export IMAGE_REGISTRY=${IMAGE_REGISTRY:-denis256/}
export PUSH_IMAGE=${PUSH_IMAGE:-0}

cd "$(dirname "$0")"

dir="paper-rock-scissors"

echo "Building ${dir}"

tag=$(git rev-parse --short=5 HEAD)

export CONTAINER_IMAGE="${IMAGE_REGISTRY}${dir}:${tag}"
export CONTAINER_IMAGE_LATEST="${IMAGE_REGISTRY}${dir}:latest"

./gradlew clean build
rm -rf build/libs/*-plain.jar

docker build -t "${CONTAINER_IMAGE}" .
docker tag "${CONTAINER_IMAGE}" "${CONTAINER_IMAGE_LATEST}"

if [[ "${PUSH_IMAGE}" == "1" ]]; then
  docker push "${CONTAINER_IMAGE}"
  docker push "${CONTAINER_IMAGE_LATEST}"
fi

echo "Built image ${CONTAINER_IMAGE}"
