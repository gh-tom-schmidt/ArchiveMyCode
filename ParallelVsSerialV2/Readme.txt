Exercise 8

Group members:
- Jason Poser
- Maximilian Kaps
- Tom Schmidt

For further information see Doc.md or Doc.pdf.


Analysis 

We can observe from the sample that serial evaluation is consistently slower than parallelization. 
It's worth noting that across all combinations of m and n, approximately the same curve emerged.

For parameters a and b, the runtime decreased significantly down to 8 cores, after which it 
showed a slight increase. This is likely due to the system having a maximum of 8 cores.

Similarly, for parameter c, there was a reduction in runtime down to 8 cores. 
However, anything beyond 8 cores exhibited a significant increase in time, sometimes 
surpassing serial execution. This effect is particularly evident when n << m.