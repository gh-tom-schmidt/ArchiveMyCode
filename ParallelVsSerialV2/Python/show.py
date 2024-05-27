import pandas as pd
import matplotlib.pyplot as plt


class Data:

    def __init__(self, filename):
        self.df = pd.read_csv(filename, sep='|', skipinitialspace=True)
        # drop columns with all NaN values (first and last)
        self.df.dropna(axis=1, how='all', inplace=True)

        self.fig, self.axs = plt.subplots(1, 3, figsize=(16, 5))
        self.current_subplot = 0

    # ------------------------------- HELPER ---------------------------------------

    def selectByNameAndValueAndCol(self, name, m, n, col1, col2):
        filtered_df = self.df[(self.df['Name'] == name)
                              & (self.df['m'] == m)
                              & (self.df['n'] == n)]

        return filtered_df[col1], filtered_df[col2]

    # -------------------------------- DRAW ----------------------------------------

    def draw(self, name1, name2, x_axis, y_axis, select_n, select_m):

        # plot graph 1
        x_values, y_values = self.selectByNameAndValueAndCol(
            name1, select_m, select_n, x_axis, y_axis)
        self.axs[self.current_subplot].plot(
            x_values,
            y_values,
            color='red',
            label=name1,
            marker='x')

        # plot graph 2
        x_values, y_values = self.selectByNameAndValueAndCol(
            name2, select_m, select_n, x_axis, y_axis)
        self.axs[self.current_subplot].plot(
            x_values,
            y_values,
            color='blue',
            label=name2,
            marker='x')

        # set info
        self.axs[self.current_subplot].set_xlabel(x_axis)
        self.axs[self.current_subplot].set_ylabel(y_axis)
        self.axs[self.current_subplot].set_title(
            f'Plot of {name1} and {name2} with n: {select_n} and m: {select_m}')
        self.axs[self.current_subplot].legend()

        self.current_subplot += 1

    def show(self):
        plt.tight_layout()
        plt.show()

    def save(self, filename):
        self.fig.savefig(filename)

    def reset(self):
        self.current_subplot = 0
        self.fig.clf()
        self.fig, self.axs = plt.subplots(1, 3, figsize=(16, 5))


data = Data('../Output/tmt.out')
data.draw('s_a', 'p_a', 'p', 'Time/s', 1000, 9000)
data.draw('s_b', 'p_b', 'p', 'Time/s', 1000, 9000)
data.draw('s_c', 'p_c', 'p', 'Time/s', 1000, 9000)

data.save("../Output/n_1000-m_9000.png")
data.reset()

data.draw('s_a', 'p_a', 'p', 'Time/s', 9000, 1000)
data.draw('s_b', 'p_b', 'p', 'Time/s', 9000, 1000)
data.draw('s_c', 'p_c', 'p', 'Time/s', 9000, 1000)

data.save("../Output/n_9000-m_1000.png")

data.reset()

data.draw('s_a', 'p_a', 'p', 'Time/s', 5000, 5000)
data.draw('s_b', 'p_b', 'p', 'Time/s', 5000, 5000)
data.draw('s_c', 'p_c', 'p', 'Time/s', 5000, 5000)

data.save("../Output/n_5000-m_5000.png")

data.reset()

data.draw('s_a', 'p_a', 'p', 'Time/s', 3000, 7000)
data.draw('s_b', 'p_b', 'p', 'Time/s', 3000, 7000)
data.draw('s_c', 'p_c', 'p', 'Time/s', 3000, 7000)

data.save("../Output/n_3000-m_7000.png")

data.reset()

data.draw('s_a', 'p_a', 'p', 'Time/s', 7000, 3000)
data.draw('s_b', 'p_b', 'p', 'Time/s', 7000, 3000)
data.draw('s_c', 'p_c', 'p', 'Time/s', 7000, 3000)

data.save("../Output/n_7000-m_3000.png")
