#ifndef MATRIX_H
#define MATRIX_H

#include <iostream>
#include <vector>  // dynamic arrays
#include <cstdlib> // rand() and srand()
#include <ctime>   // time()

#include "Matrix.h"

using namespace std;

// ------------------------------- MATRIX --------------------------------------

class Matrix
{

private:
    vector<vector<int>> data; // 2D vector

public:
    // e.g. two rows and three columns: 2 x 3
    int rows;
    int cols;

    // ------------------------------ FUNCTIONS --------------------------------------

    void fillRandom()
    {
        srand(time(0)); // seed for random

        for (int i = 0; i < this->rows; ++i)
        {
            for (int j = 0; j < this->cols; ++j)
            {
                this->data[i][j] = rand() % 10; // random value between 0 and 9
            }
        }
    }

    void setSize(int rows, int cols)
    {
        this->rows = rows;
        this->cols = cols;

        // clear first
        this->data.clear();
        this->data.shrink_to_fit();

        this->data.resize(rows, vector<int>(cols)); // resize the 2D vector to n x m
    }

    void print() const
    {
        for (int i = 0; i < this->rows; ++i)
        {
            for (int j = 0; j < this->cols; ++j)
            {
                cout << this->data[i][j] << " ";
            }
            cout << endl;
        }
        cout << endl;
    }

    // access matrix elements
    vector<int> &operator[](int index)
    {
        return this->data[index];
    }

    // aassign value to matrix
    int &operator()(int x, int y)
    {
        return this->data[x][y];
    }
};

// ------------------------------- VECTOR ----------------------------

class Vector
{

private:
    vector<int> data; // 1D vector

public:
    int rows;

    // ------------------------------ FUNCTIONS --------------------------------------

    void fillRandom()
    {
        srand(time(0)); // seed for random

        for (int i = 0; i < this->rows; ++i)
        {

            this->data[i] = rand() % 10; // random value between 0 and 9
        }
    }

    void setSize(int rows)
    {
        this->rows = rows;

        // clear first
        this->data.clear();
        this->data.shrink_to_fit();

        this->data.resize(rows); // resize the 1D vector
    }

    void print() const
    {
        for (int i = 0; i < this->rows; ++i)
        {
            cout << this->data[i] << endl;
        }
        cout << endl;
    }

    // access matrix elements
    int &operator[](int index)
    {
        return this->data[index];
    }

    // aassign value to matrix
    int &operator()(int x)
    {
        return this->data[x];
    }
};

#endif
