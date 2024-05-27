#!/bin/bash

echo "Step 1: Move to <Cpp>..."
cd Cpp

echo "Step 2: Running make file..."
make

echo "Step 2: Running ./main with 1000 10 1000 12"
./main 1000 9 1000 12 "../Output/tmt.out"

# Step 3: Run make clean
echo "Step 3: Running make clean..."
make clean

echo "Step 4: Move to <Python>"
cd ../Python

echo "Step 5: Running python show.py..."
python show.py

echo "Done"