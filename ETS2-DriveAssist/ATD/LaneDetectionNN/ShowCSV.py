import pandas as pd
import cv2
import numpy as np

# Load CSV file into a pandas DataFrame
data = pd.read_pickle('annotations.pkl')

for _, row in data.iterrows():
    filename = row["Filename"]

    img = cv2.imread("./Images/" + filename)

    for lane in row["LaneCoordinates"]:
        x, y = lane

        points = np.array(list(zip(x, y)), np.int32)
        points = points.reshape((-1, 1, 2))

        cv2.polylines(img, [points], isClosed=False,
                      color=(0, 255, 0), thickness=2)

    cv2.imshow('Image', img)

    key = cv2.waitKey(0) & 0xFF

    if key == ord('q'):
        cv2.destroyAllWindows()
        break
    elif key == ord('w'):
        continue
