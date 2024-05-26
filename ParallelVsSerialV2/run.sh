#!/bin/bash

# Step 1: Run the make file
echo "Step 1: Running make file..."
make

# Step 2: Call ./main with parameters
echo "Step 2: Running ./main 1000 2000 1000..."
./main 1000 2000 1000

# Step 3: Run make clean
echo "Step 3: Running make clean..."
make clean

# Step 4: Run python show.py
echo "Step 4: Running python show.py..."
python show.py

echo "Done"