#ifndef TIMEMAPTOOL_H
#define TIMEMAPTOOL_H

#include <iostream>
#include <functional>
#include <chrono>
#include <fstream>
#include <string>
#include <sstream>
#include <tuple>
#include <utility>

#include "TimeMapTool.h"

using namespace std;

class TimeMapTool
{
private:
    ofstream outFile;

public:
    TimeMapTool(char *filename)
    {
        outFile.open(filename, ios::out | ios::trunc); // open file in append mode
    }

    ~TimeMapTool()
    {
        outFile.close(); // close the file when the object is destroyed
    }

    void write(const string &str)
    {
        outFile << str << endl; // append string
    }

    // -----------------------------------HELPER ------------------------------

    // convert non-string arguments to string
    template <typename T>
    string to_str(T &&arg)
    {
        return to_string(forward<T>(arg));
    }

    // for char[] arguments
    template <size_t N>
    string to_str(const char (&str)[N])
    {
        return string(str);
    }

    // ------------------------------- MAP TIME -----------------------------

    template <typename... Args>
    void setHeader(Args &&...args)
    {
        ostringstream oss;
        oss << "|";
        ((oss << to_str(args) << "|"), ...);
        write(oss.str());
    }

    template <typename Func, typename... Args>
    double evaluate(Func f, Args &&...args)
    {
        auto start = chrono::steady_clock::now(); // start time

        // call the function
        f(forward<Args>(args)...);

        auto end = chrono::steady_clock::now();                                   // End time
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start); // calculate duration

        // convert from ms to s
        return duration.count() * 0.001;
    }

    template <typename... Args>
    void map(Args &&...args)
    {
        ostringstream oss;
        oss << "|";
        ((oss << to_str(args) << "|"), ...);
        write(oss.str());
    }
};

#endif