import os
import xml.etree.ElementTree as ET
import cv2
import pandas as pd
from scipy.interpolate import interp1d
import numpy as np


def processImg(image, output_path, offset_top, crop, resize_value):
    path = image.get('name')
    # get the name of the image
    filename = os.path.basename(path)

    # open the image
    img = cv2.imread(path)
    height, width = img.shape[:2]
    x, y = crop, crop + offset_top
    width, height = width - x - crop, height - y - crop

    # crop the image
    # because it is array like, we must add x and y back again
    img = img[y:y + height, x:x+width]

    # resize the image to half its size
    img = cv2.resize(img, (width // resize_value, height // resize_value))

    cv2.imwrite(output_path + filename, img)

    return filename, width, height


def processLines(polyline, width, height, crop, offset_top, resize_value, grid_size):
    points = polyline.get('points')

    # split the string into individual coordinate pairs
    coordinate_pairs = points.split(';')

    x_coordinates = []
    y_coordinates = []

    # extract x and y coordinates from each pair
    for pair in coordinate_pairs:
        x, y = pair.split(',')

        # fit the values on the new image shape
        x = (float(x) - crop) / resize_value
        y = (float(y) - crop - offset_top) / resize_value

        # filter possible double x values to make interp easier
        if y not in y_coordinates:
            x_coordinates.append(x)
            y_coordinates.append(y)

    # apply linear interp
    curve = interp1d(y_coordinates, x_coordinates, kind='linear')

    # make sure the smallest value is bigger than the smallest coordinate value
    lower = ((min(y_coordinates) + 10) // 10) * 10
    # make sure the biggest number is smaller thant the biggest coordinate value
    upper = (max(y_coordinates) // 10) * 10

    # define the range for interpolation (10px per step)
    y_range = np.arange(lower, upper, grid_size)

    # perform interpolation
    x_range = np.round(curve(y_range))

    # from float to int
    x_range = x_range.astype(int)
    y_range = y_range.astype(int)

    filtered_x_range = []
    filtered_y_range = []

    # filter the output values to be in range
    # of the image shape
    for x, y in zip(x_range, y_range):
        if 0 <= x <= width and 0 <= y <= height:
            filtered_x_range.append(x)
            filtered_y_range.append(y)

    return [filtered_x_range, filtered_y_range]


def createDfFromAnnotations(xml_file, img_output_path, file_output_path, offset_top, crop, resize_value, grid_size):

    # create an empty DataFrame
    data = None

    # parse the XML file
    tree = ET.parse(xml_file)
    root = tree.getroot()

    processed = 0

    for image in root.findall('.//image'):

        # show some progress information
        if processed % 100 == 0 and processed != 0:
            print(f"Processed: {processed}")

        # process the image
        filename, width, height = processImg(
            image, img_output_path, offset_top, crop, resize_value)

        lane_coordinates = []

        # get each polyline on an image
        for polyline in image.findall('.//polyline'):
            # get the lanes
            lane_coordinates.append(processLines(
                polyline, width, height, crop, offset_top, resize_value, grid_size))

        # add a now row
        row = {
            'Filename': filename,
            'Width': width,
            'Height': height,
            'LaneCoordinates': lane_coordinates
        }

        # fill the df when empty
        if data is None:
            data = pd.DataFrame(row)
            data.loc[0] = row
        else:
            data.loc[len(data.index)] = row

        processed += 1

    data.to_pickle(file_output_path)
    print("File saved.")


def main():
    xml_file = "annotations.xml"
    img_output_path = "./Images/"
    file_output_path = "annotations.pkl"
    offset_top = 300
    crop = 100
    resize_value = 2
    grid_size = 10

    createDfFromAnnotations(xml_file, img_output_path,
                            file_output_path, offset_top, crop, resize_value, grid_size)


main()
