#!/bin/bash

if [ -d "env" ]; then
  echo "Activating existing virtual environment..."
  source env/bin/activate

else
  echo "Creating a new virtual environment..."
  python -m venv env
  
  echo "Activating the new virtual environment..."
  source env/bin/activate
  
  echo "Installing pandas and matplotlib..."
  pip install pandas matplotlib

  echo "Done"

fi
