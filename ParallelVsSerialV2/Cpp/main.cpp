#include "TimeMapTool.h"
#include "Matrix.h"
#include "TaskFunctions.h"
#include <iostream>

#include <thread>

using namespace std;

int main(int argc, char *argv[])
{

    int nm_start_value = stoi(argv[1]);
    int nm_step_count = stoi(argv[2]);
    int nm_step_size = stoi(argv[3]);
    int p = stoi(argv[4]);
    char *s = argv[5];

    TimeMapTool tmt = TimeMapTool(s);
    tmt.setHeader("Name", "m", "n", "p", "Time/s");

    Vector x = Vector();
    Vector y = Vector();
    Matrix A = Matrix();

    int i = nm_start_value;
    int j = nm_step_count * nm_step_size;

    while (i <= nm_step_count * nm_step_size && j >= nm_start_value)
    {
        cout << "Running evaluation for m: " << i << " and n: " << j << endl;

        // reset the matrecies
        x.setSize(j);
        x.fillRandom();
        y.setSize(i);
        y.fillRandom();
        A.setSize(i, j);
        A.fillRandom();

        // -1 = Serial
        cout << "serial" << endl;
        tmt.map("s_a", i, j, -1, tmt.evaluate(s_a, x, y, A, A.cols, A.rows));
        tmt.map("s_b", i, j, -1, tmt.evaluate(s_b, x, y, A, A.cols, A.rows));
        tmt.map("s_c", i, j, -1, tmt.evaluate(s_c, x, y, A, A.cols, A.rows));

        for (int k = 1; k <= p; k++)
        {
            cout << "parallel with p: " << k << endl;
            tmt.map("p_a", i, j, k, tmt.evaluate(p_a, x, y, A, A.cols, A.rows, k));
            tmt.map("p_b", i, j, k, tmt.evaluate(p_b, x, y, A, A.cols, A.rows, k));
            tmt.map("p_c", i, j, k, tmt.evaluate(p_c, x, y, A, A.cols, A.rows, k));
        }

        i += nm_step_size;
        j -= nm_step_size;
    }

    return 0;
}
