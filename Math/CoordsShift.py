# shift the python array indecies to a cartesian coordinat system
# so that we can reference values from positiv and negative sites

import numpy as np


class Coords1D:
    def __init__(self, d1_array):
        self.d1_array = np.asarray(d1_array)
        self.d1_array_range = len(self.d1_array) - 1
        self.shift = self.d1_array_range // 2
        self.is_shifted = True

    def __getitem__(self, index):

        if self.is_shifted:
            return self.d1_array[index + self.shift]
        else:
            return self.d1_array[index]

    def __setitem__(self, index, value):

        if self.is_shifted:
            self.d1_array[index + self.shift] = value
        else:
            self.d1_array[index] = value

    def __str__(self):
        return " ".join([str(item) for item in self.d1_array])

    def activate(self):
        self.is_shifted = True

    def deactivate(self):
        self.is_shifted = False


class Coords2D:
    def __init__(self, d2_array):
        self.d2_array = np.asarray([])
        for l in d2_array:
            self.d2_array.append(Coords1D(l))

        # the array has the size (y, x)
        # [[1,2,3,4],
        #  [1,2,3,4],
        #  [1,2,3,4]]

        self.d2_array_range = len(self.d2_array) - 1
        self.shift = self.d2_array_range // 2
        self.is_shifted = True

    def __getitem__(self, index):

        if self.is_shifted:
            return self.d2_array[index + self.shift]
        else:
            return self.d2_array[index]

    def __setitem__(self, index, value):

        if self.is_shifted:
            self.d2_array[index + self.shift] = value
        else:
            self.d2_array[index] = value

    def __str__(self):
        return f'[\n{"\n".join([str(row) for row in self.d2_array])}\n]'

    def activate(self):
        self.is_shifted = True

    def deactivate(self):
        self.is_shifted = False
