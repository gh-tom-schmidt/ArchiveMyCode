import pandas as pd
import matplotlib.pyplot as plt
import random


class Data:

    def __init__(self, filename):
        self.df = pd.read_csv(filename, sep='|', skipinitialspace=True)
        # drop columns with all NaN values (first and last)
        self.df.dropna(axis=1, how='all', inplace=True)

        self.fig, self.axs = plt.subplots(1, 3, figsize=(16, 5))
        self.current_subplot = 0

    # ------------------------------- HELPER ---------------------------------------

    def selectByNameAndCol(self, name, col1, col2):
        filtered_df = self.df[self.df['Name'] == name]
        return filtered_df[col1], filtered_df[col2]

    def color(self):
        return (random.random(), random.random(), random.random())

    # -------------------------------- DRAW ----------------------------------------

    def draw(self, name1, name2, x_axis, y_axis):

        # plot graph 1
        x_values, y_values = self.selectByNameAndCol(name1, x_axis, y_axis)
        self.axs[self.current_subplot].plot(
            x_values,
            y_values,
            color=self.color(),
            label=name1)

        # plot graph 2
        x_values, y_values = self.selectByNameAndCol(name1, x_axis, y_axis)
        self.axs[self.current_subplot].plot(
            x_values,
            y_values,
            color=self.color(),
            label=name2)

        # set info
        self.axs[self.current_subplot].set_xlabel(x_axis)
        self.axs[self.current_subplot].set_ylabel(y_axis)
        self.axs[self.current_subplot].set_title(f'Plot of {x_axis} vs {
            y_axis} from {name1} and {name2}')
        self.axs[self.current_subplot].legend()

        self.current_subplot += 1

    def show(self):
        plt.tight_layout()
        plt.show()


data = Data('tmt.out')
data.draw('s_a', 'p_a', 'Time/s', 'm')
data.draw('s_b', 'p_b', 'Time/s', 'm')
data.draw('s_c', 'p_c', 'Time/s', 'm')

data.show()
