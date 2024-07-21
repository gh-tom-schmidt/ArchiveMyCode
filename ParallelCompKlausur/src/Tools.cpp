#include "Tools.hpp"

void Printer::matrix(int** matrix, size_t rows, size_t cols) {
    for (size_t i = 0; i < rows; ++i) {
        for (size_t j = 0; j < cols; ++j) {
            std::cout << matrix[i][j] << " ";
        }
        std::cout << std::endl;
    }
    std::cout << std::endl;
}

template <typename T>
void Printer::value(const T& x) {
    std::cout << x << std::endl;
}

template <typename T>
void Printer::array(const T* array, size_t size) {
    for (size_t i = 0; i < size; ++i) {
        std::cout << array[i] << " ";
    }
    std::cout << std::endl;
}

// explicit template instantiations
template void Printer::value<int>(const int& x);
template void Printer::value<double>(const double& x);
template void Printer::array<int>(const int* array, size_t size);
template void Printer::array<double>(const double* array, size_t size);


// TIMER

void Timer::start() {
    startTimepoint = std::chrono::high_resolution_clock::now();
}

void Timer::end(const std::string& unit) {
    auto endTimepoint = std::chrono::high_resolution_clock::now();

    if (unit == "micro") {
        auto duration = std::chrono::duration_cast<std::chrono::microseconds>(endTimepoint - startTimepoint);
        std::cout << "Time taken by function: " << duration.count() << " microseconds" << std::endl;
    } else if (unit == "milli") {
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTimepoint - startTimepoint);
        std::cout << std::fixed << std::setprecision(3);
        std::cout << "Time taken by function: " << duration.count() << " milliseconds" << std::endl;
    } else if (unit == "sec") {
        auto duration = std::chrono::duration<double>(endTimepoint - startTimepoint);
        std::cout << std::fixed << std::setprecision(3);
        std::cout << "Time taken by function: " << duration.count() << " seconds" << std::endl;
    } else {
        std::cout << "Invalid time unit. Use 'micro', 'milli', or 'sec'." << std::endl;
    }
}


// STRUCTUR

Structur::Structur() : gen(rd()), int_dis(1, 9) {}

int* Structur::createVector(size_t size) {
    int* v = (int*)malloc(size * sizeof(int));
    fillRandom(v, size);
    return v;
}

int* Structur::createZeroVector(size_t size) {
    int* v = (int*)malloc(size * sizeof(int));
    fillZeros(v, size);
    return v;
}

int** Structur::createMatrix(size_t rows, size_t cols) {
    int** m = (int**)malloc(rows * sizeof(int*));
    for (size_t i = 0; i < rows; i++) {
        m[i] = createVector(cols);
    }
    return m;
}

int** Structur::createZeroMatrix(size_t rows, size_t cols) {
    int** m = (int**)malloc(rows * sizeof(int*));
    for (size_t i = 0; i < rows; i++) {
        m[i] = createZeroVector(cols);
    }
    return m;
}

void Structur::fillRandom(int* arr, size_t size) {

    for(size_t i = 0; i < size; i++){
        arr[i] = randInt();
    }

}

void Structur::fillZeros(int* arr, size_t size) {

    for(size_t i = 0; i < size; i++){
        arr[i] = 0;
    }

}

int Structur::randInt(){
    return int_dis(gen);
}
