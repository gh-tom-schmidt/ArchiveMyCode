#ifndef TASKFUNCTIONS_H
#define TASKFUNCTIONS_H

#include "Matrix.h"
#include "TaskFunctions.h"

// ------------------------- Parallel -------------------------------------

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

// ------------------------ Serial ---------------------------

void p_a(Vector &x, Vector &y, Matrix &A, int n, int m)
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

void p_b(Vector &x, Vector &y, Matrix &A, int n, int m)
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

void p_c(Vector &x, Vector &y, Matrix &A, int n, int m)
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