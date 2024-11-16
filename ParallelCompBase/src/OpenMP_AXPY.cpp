#include "Tools.hpp"

#include <omp.h>

void saxmpy(int a, int* x, int* y, size_t size){

    for(size_t i = 0; i < size; i++){

        y[i] += a * x[i];

    }

}

void paxmpy(int a, int* x, int* y, size_t size, int threads) {

    omp_set_num_threads(threads);

    #pragma omp parallel
    {
        #pragma omp for
        for (size_t i = 0; i < size; i++) {
            y[i] += a * x[i];
        }
    }
}

int main(){
    
    Timer time;
    Structur structur;
    // Printer printer;


    int* x = structur.createVector(n1000);
    int* y = structur.createVector(n1000);

    int a = 6;
    int threads = 2;

    time.start();
    saxmpy(a, x, y, n1000);
    time.end("sec");

    time.start();
    paxmpy(a, x, y, n1000, threads);
    time.end("sec");


}

