import cv2
import numpy as np

clicked_points = []


def onClick(event, x, y, flags, param):
    global img

    # save mouse position in list of points
    if event == cv2.EVENT_LBUTTONDOWN:
        clicked_points.append([x, y])
        copy = img.copy()
        cv2.putText(copy, f"(Added Point: {x}, {y})", (x, y),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
        cv2.imshow('Selection', copy)

    # show current mouse positon
    if event == cv2.EVENT_MOUSEMOVE:
        copy = img.copy()
        cv2.putText(copy, f"({x}, {y})", (x, y),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
        cv2.imshow('Selection', copy)


def run():
    img = cv2.imread('Real_Test.jpg')

    cv2.namedWindow('Selection')
    cv2.setMouseCallback('Selection', onClick)

    cv2.imshow('Selection', img)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

    print("You god:", clicked_points)


run()
