import numpy as np
from scipy.interpolate import CubicSpline
import matplotlib.pyplot as plt

# Define the given pixel points
x_pixels = np.array([0, 1, 2, 3, 4])  # X coordinates of the points
y_pixels = np.array([0, 1, 2, 1, 0])  # Y coordinates of the points

# Create a cubic spline interpolator
cs = CubicSpline(x_pixels, y_pixels)

# Define the range for interpolation
x_range = np.linspace(min(x_pixels), max(x_pixels), 100)

# Perform interpolation
y_spline = cs(x_range)

# Plot the original points and the cubic spline
plt.plot(x_pixels, y_pixels, 'o', label='Pixel Points')
plt.plot(x_range, y_spline, label='Cubic Spline')
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Cubic Spline Interpolation')
plt.legend()
plt.grid(True)
plt.show()
