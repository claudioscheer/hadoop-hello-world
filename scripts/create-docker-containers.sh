#!/bin/bash

script_dir=$(dirname "$0")

docker build -t hadoop-image $script_dir/..
docker create -ti --name hadoop-pseudo-distributed hadoop-image
