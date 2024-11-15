# Documentation

(_Note_: The scripts are tested inside a Linux Environment. The .ps1 scripts for Windows users may contain some bugs. Please use .sh scripts if possible.)

## Requirements

- The program is tested with **g++ (GCC) 14.1.1 20240522**
- Please **upgrade** when necessary or use the contained **setup-docker.sh**.

- The python script require **pandas** and **matplotlib**.
- Run **setup.sh** (Mac/Linux) or **setup.ps1** (Windows).
- Please select the virtual environment as interpreter.

## Run with Defaults

- Run **run.sh** (Mac/Linux) and **run.ps1** (Windows)

## Run with Custom Values

- Run **make** inside the /Cpp/ folder
- Run **./main** start_value step_count step_size "../Output/tmt.out"
- Run **python** show.py
  - Add the **Code-block** below to output more plots

(_Note_: There are not many safety features inside the scripts. Errors can be possible.)

## Output

The Output can be found in the **./Output/** folder.
It contains **some** examples as plots and a list of **all** measured values.

(_Note_: If using Docker, please remember to copy the output to your host system.)

## Code-block for show.py

```python
data.reset()


data.draw('s_a', 'p_a', 'p', 'Time/s', <n>, <m>)
data.draw('s_b', 'p_b', 'p', 'Time/s', <n>, <m>)
data.draw('s_c', 'p_c', 'p', 'Time/s', <n>, <m>)


data.save("../Output/<name>.png")
```
