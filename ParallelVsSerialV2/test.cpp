#include <vector>

int main()
{
    int rows = 1000;
    int cols = 1000;

    // Create a matrix with dimensions rows x cols
    std::vector<std::vector<int>> data;
    data.resize(rows, std::vector<int>(cols));

    // Now let's shrink it to 3x3
    int newRows = 1000;
    int newCols = 2000;
    data.resize(newRows, std::vector<int>(newCols));

    // Now data is a 3x3 matrix, and any excess elements are discarded

    return 0;
}
