# Variables
CXX = g++
CXXFLAGS = -O2 -Wall -fopenmp
SRC_DIR = src
BIN_DIR = bin
TOOLS_SRC = $(SRC_DIR)/Tools.cpp
TOOLS_OBJ = $(BIN_DIR)/Tools.o

# Create bin directory if it does not exist
$(shell mkdir -p $(BIN_DIR))

# Targets
all: $(BIN_DIR)/$(TARGET)

# Rule to compile the main .cpp file and link with Tools.o to create the executable
$(BIN_DIR)/$(TARGET): $(BIN_DIR)/$(TARGET).o $(TOOLS_OBJ)
	$(CXX) $(CXXFLAGS) -o $@ $^

# Rule to compile main .cpp file into object file
$(BIN_DIR)/%.o: $(SRC_DIR)/%.cpp
	$(CXX) $(CXXFLAGS) -c -o $@ $<

# Rule to compile Tools.cpp
$(TOOLS_OBJ): $(TOOLS_SRC)
	$(CXX) $(CXXFLAGS) -c -o $@ $<

# Rule to clean build artifacts
clean:
	rm -rf $(BIN_DIR)/*

.PHONY: all clean
