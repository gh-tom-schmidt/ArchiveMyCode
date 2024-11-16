#ifndef TASKFUNCTIONS_H
#define TASKFUNCTIONS_H

#include "Matrix.h"
#include "TaskFunctions.h"

#include <omp.h>

// ------------------------- Parallel -------------------------------------

void p_a(Vector &x, Vector &y, Matrix &A, int n, int m, int num_threads)
{

    omp_set_num_threads(num_threads);

    int i, j = 0;
    double temp = 0.0;

#pragma omp parallel for shared(x, y, A, n, m) private(i, j, temp) // reduction(+ : y[ : m])
    for (i = 0; i < m; i++)
    {
        temp = 0.0; // prevent false sharing
        for (j = 0; j < n; j++)
        {
            temp += A[i][j] * x[j];
        }
        y[i] = temp;
    }
}

void p_b(Vector &x, Vector &y, Matrix &A, int n, int m, int num_threads)
{
    // Set the number of threads
    omp_set_num_threads(num_threads);

    int i, j, s = 0;
    double temp = 0.0;

#pragma omp parallel for shared(x, y, A, n, m) private(i, j, temp) reduction(+ : s)
    for (i = 0; i < m; i++)
    {
        temp = 0.0; // false sharing
        for (j = 0; j < n; j++)
        {
            temp += A[i][j] * x[j];
        }
        y[i] = temp;

// merge safely
#pragma omp atomic
        s += temp;
    }
}

void p_c(Vector &x, Vector &y, Matrix &A, int n, int m, int num_threads)
{
    omp_set_num_threads(num_threads);

    y[0] = 0;

#pragma omp parallel shared(x, y, A, n, m)
    {
        for (int i = 1; i < m; i++)
        {
            double temp = y[i - 1];
#pragma omp for
            for (int j = 0; j < n; j++)
            {
                temp += A[i][j] * x[j];
            }
#pragma omp critical
            {
                y[i] = temp;
            }
        }
    }
}

// ------------------------ Serial ---------------------------

void s_a(Vector &x, Vector &y, Matrix &A, int n, int m)
{
    for (int i = 0; i < m; i++)
    {
        y[i] = 0;
        for (int j = 0; j < n; j++)
        {
            y[i] = y[i] + A[i][j] * x[j];
        }
    }
}

void s_b(Vector &x, Vector &y, Matrix &A, int n, int m)
{
    int s = 0;
    for (int i = 0; i < m; i++)
    {
        y[i] = 0;
        for (int j = 0; j < n; j++)
        {
            y[i] = y[i] + A[i][j] * x[j];
        }
        s += y[i];
    }
}

void s_c(Vector &x, Vector &y, Matrix &A, int n, int m)
{
    y[0] = 0;
    for (int i = 1; i < m; i++)
    {
        y[i] = y[i - 1];
        for (int j = 0; j < n; j++)
        {
            y[i] = y[i] + A[i][j] * x[j];
        }
    }
}

#endif