#include "TimeMapTool.h"
#include "Matrix.h"
#include "TaskFunctions.h"
#include <iostream>

#include <thread>

using namespace std;

int main(int argc, char *argv[])
{

    TimeMapTool tmt = TimeMapTool();
    tmt.setHeader("Name", "m", "n", "Time/s");

    int nm_min_value = stoi(argv[1]);
    int nm_max_value = stoi(argv[2]);
    int nm_step_size = stoi(argv[3]);

    Vector x = Vector();
    Vector y = Vector();
    Matrix A = Matrix();

    for (int i = nm_min_value; i <= nm_max_value; i += nm_step_size)
    {
        for (int j = nm_min_value; j <= nm_max_value; j += nm_step_size)
        {

            cout << "Running evaluation for m: " << i << " and n: " << j << endl;

            // reset the matrecies
            x.setSize(j);
            x.fillRandom();
            y.setSize(i);
            y.fillRandom();
            A.setSize(i, j);
            A.fillRandom();

            tmt.map("s_a", i, j, tmt.evaluate(s_a, x, y, A, A.cols, A.rows));
            tmt.map("s_b", i, j, tmt.evaluate(s_b, x, y, A, A.cols, A.rows));
            tmt.map("s_c", i, j, tmt.evaluate(s_c, x, y, A, A.cols, A.rows));

            tmt.map("p_a", i, j, tmt.evaluate(p_a, x, y, A, A.cols, A.rows));
            tmt.map("p_b", i, j, tmt.evaluate(p_b, x, y, A, A.cols, A.rows));
            tmt.map("p_c", i, j, tmt.evaluate(p_c, x, y, A, A.cols, A.rows));
        }
    }

    return 0;
}
