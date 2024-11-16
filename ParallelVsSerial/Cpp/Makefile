CXX = g++ # compiler

CXXFLAGS = -Wall -g -fopenmp # flags

TARGET = main # executable

SRCS = main.cpp # source files

OBJS = $(SRCS:.cpp=.o) # object files

# rule to build the executable
$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $(TARGET) $(OBJS)

# rule to build object files
%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

# clean rule to remove generated files
# call with "$ make clean"
clean:
	rm -f $(TARGET) $(OBJS)

# phony targets
.PHONY: clean
