#!/bin/bash

filename="$1"
executable="./bin/${filename%.*}"

# Run the make command
echo "Running make..."
make TARGET=${filename%.*}

echo "Running $executable..."
"$executable"
