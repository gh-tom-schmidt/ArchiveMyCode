#!/bin/bash

echo "Building the Docker image..."
docker build -t parallel_vs_serial_v2 .

echo "Running the Docker container..."
docker run -it --rm parallel_vs_serial_v2 /bin/bash

