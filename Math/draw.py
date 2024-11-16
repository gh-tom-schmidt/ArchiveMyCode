import numpy as np
import matplotlib.pyplot as plt


def draw(function, x_from=-10, x_to=10, density=100, int=False, scatter=False):

    # Generate x values
    if int:
        x_values = np.round(np.linspace(x_from, x_to, density))
    else:
        # Generating 100 points between -10 and 10
        x_values = np.linspace(x_from, x_to, density)

    y_values = []

    # Calculate y values using the function
    for x in x_values:
        y_values.append(function(x))

    # Plot the function
    if scatter:
        plt.scatter(x_values, y_values, s=5)
    else:
        plt.plot(x_values, y_values)

    plt.grid(True)
    plt.show()
