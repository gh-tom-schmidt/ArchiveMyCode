#ifndef TOOLS_HPP
#define TOOLS_HPP

#include <iostream>
#include <random>
#include <sstream>
#include <string>
#include <chrono>
#include <cstddef>
#include <iomanip>

constexpr unsigned int ntest = 4;
constexpr unsigned int n1 = 1024 * 1;     
constexpr unsigned int n4 = 1024 * 4;      
constexpr unsigned int n8 = 1024 * 8;      
constexpr unsigned int n16 = 1024 * 16;    
constexpr unsigned int n32 = 1024 * 32;   
constexpr unsigned int n63 = 1024 * 63;  
constexpr unsigned int n100 = 100;      
constexpr unsigned int n1000 = 1000;     
constexpr unsigned int n10000 = 10000;     
constexpr unsigned int n100000 = 100000;  

// Printer class
class Printer {
public:
    // VALUE PRINT
    template <typename T>
    void value(const T& x);

    // ARRAY PRINT
    template <typename T>
    void array(const T* array, size_t size);

    // MATRIX PRINT (ONLY FOR INTEGER VALUES)
    void matrix(int** matrix, size_t rows, size_t cols);
};

// Timer class
class Timer {
public:
    void start();
    void end(const std::string& unit = "milli");

private:
    std::chrono::time_point<std::chrono::high_resolution_clock> startTimepoint;
};

// Structur class
class Structur {
private:
    std::random_device rd;
    std::mt19937 gen;
    std::uniform_int_distribution<> int_dis;

    int randInt();
    void fillRandom(int* arr, size_t size);
    void fillZeros(int* arr, size_t size);

public:
    // Constructor
    Structur();

    int* createVector(size_t size);
    int* createZeroVector(size_t size);
    int** createMatrix(size_t rows, size_t cols);
    int** createZeroMatrix(size_t rows, size_t cols);
};

#endif // TOOLS_HPP
