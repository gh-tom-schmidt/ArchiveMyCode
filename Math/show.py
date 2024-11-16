import matplotlib.pyplot as plt


def show(img):
    plt.imshow(img, cmap='gray')
    plt.axis('off')
    plt.show()
