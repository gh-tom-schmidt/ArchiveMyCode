#!/bin/bash

echo "------------------------------------------------------------"
echo " Installation Script for IR FSU JENA 2024/25 on Arch Linux  "
echo "------------------------------------------------------------"
echo ""
echo "This script will install the following packages:"
echo "- ir-datasets"
echo "- tira"
echo "- python-terrier"
echo "- pyterrier_doc2query"
echo ""
echo "Script written by Tom Schmidt"
echo "------------------------------------------------------------"
echo "DISCLAIMER:"
echo "By running this script, you acknowledge that errors may occur during installation."
echo "These errors may be due to system configurations, package versions, or other factors."
echo "The script author is not responsible for any issues that arise during or after the installation."
echo ""

# --------------------- CHECK DEPENDECIES ----------------------------

check_installed() {
    # Check if command is found (silence the output with &> /dev/null)
    command -v "$1" &> /dev/null
    if [ $? -eq 0 ]; then
        echo "$1 is installed."
    else
        echo "$1 is not installed."
        return 1
    fi
}

# check for dependecies
check_installed gcc
check_installed cmake
check_installed python

# check for Java 11 (java-11-openjdk) used by pyjnius 
# check current installation
if java -version 2>&1 | grep -q "11"; then
    echo "java-11-openjdk is installed."
else
    echo "java-11-openjdk is not installed or the version is incorrect. Checking /usr/lib/jvm."
    
    # check other installations
    if [ -d "/usr/lib/jvm/java-11-openjdk" ]; then
        # if found, export to the current shell session
        echo "java-11-openjdk found in /usr/lib/jvm. Exporting JAVA_HOME."
        export JAVA_HOME="/usr/lib/jvm/java-11-openjdk"
        export PATH="$JAVA_HOME/bin:$PATH"
        echo "JAVA_HOME is set to $JAVA_HOME."
    else
        echo "java-11-openjdk is not installed in /usr/lib/jvm. Exiting script."
        return 1
    fi

fi

# fix some errors with gcc compiler
export CFLAGS="-fpermissive"

# --------------------- BUILD FROM SOURCE ----------------------------

# build env
if [ -d "./env" ]; then
    echo "The virtual enviroment "env" is already installed."
    return 1
else
    # build env
    echo "Create virtual enviroment (env)."
    python -m venv env
    # activate env
    echo "Activate virtual enviroment (env)."
    source env/bin/activate
    # install packeges
    echo "Install packeges into the virtual enviroment (env)."
    pip install ir-datasets tira python-terrier git+https://github.com/terrierteam/pyterrier_doc2query.git 
fi