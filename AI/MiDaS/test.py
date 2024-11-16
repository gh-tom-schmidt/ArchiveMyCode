import glob
import cv2
import torch
import matplotlib.pyplot as plt
import numpy as np
import time
from matplotlib.animation import FuncAnimation


def loadModel():
    midas_transforms = torch.hub.load("intel-isl/MiDaS", "transforms")
    transform = midas_transforms.small_transform

    midas = torch.hub.load("intel-isl/MiDaS", "MiDaS_small")

    device = torch.device(
        "cuda") if torch.cuda.is_available() else torch.device("cpu")
    midas.to(device)
    midas.eval()

    return midas, transform, device


def predict(img, midas, transform, device):
    input_batch = transform(img).to(device)

    with torch.no_grad():
        start_time = time.time()

        prediction = midas(input_batch)

        print(f"Time for prediction: {time.time() - start_time}")

        start_time = time.time()

        prediction = torch.nn.functional.interpolate(
            prediction.unsqueeze(1),
            size=img.shape[:2],
            mode="bicubic",
            align_corners=False,
        ).squeeze()

        print(f"Time for interpolation: {time.time() - start_time}")

    output = prediction.cpu().numpy()

    return output

# Function to read video frame by frame


def read_video():
    cap = cv2.VideoCapture("VENICE Cinematic FPV.mp4")

    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    frame_count = 0

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break

        frame_count += 1
        print(f"Frame: {frame_count} / {total_frames}")

        yield frame
    cap.release()


def update(frame, ax, midas, transform, device):
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    start_time = time.time()

    plt.imshow(predict(frame, midas, transform, device))

    print(f"Time for plt: {time.time() - start_time}")

    return ax,


midas, transform, device = loadModel()

fig, ax = plt.subplots()
ax.axis('off')

for frame in read_video():
    update(frame, ax, midas, transform, device)
    plt.pause(0.001)

    # display.clear_output(wait=True)
    # display.display(plt.gcf())

plt.close()
