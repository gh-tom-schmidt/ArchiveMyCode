# python extract.py "Data/Input/FarCry_11-08.mp4" "Data/Output/Video" "Data/Output/Images" 0.04

import cv2
import argparse
import random
from tqdm import tqdm

# Outputs frame by frame to use in a for-loop
def frame_generator(cap):
    # Set a progress bar that counts up to the maximum numver of frames
    with tqdm(total=int(cap.get(cv2.CAP_PROP_FRAME_COUNT))) as pbar:
        while True:
            ret, frame = cap.read()
            if not ret:
                break
            yield frame
            # Update Progress-Bar
            pbar.update(1)

# Set up argument parser
parser = argparse.ArgumentParser(description="Extract frames from a video.")
parser.add_argument('input_video_path', type=str, help='Input video file.')
parser.add_argument('output_video_path', type=str, help='Path to save the output video file.')
parser.add_argument('output_image_path', type=str, help='Path to save the extracted image files.')
parser.add_argument('chance', type=float, help='Chance between 0 and 1')
args = parser.parse_args()

# Load Video
cap = cv2.VideoCapture(args.input_video_path)

# Check if the video opened successfully
if not cap.isOpened():
    print("Error: Could not open video.")
    exit()

# Get video's width, height, and frame rate
frame_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
frame_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
fps = cap.get(cv2.CAP_PROP_FPS)

# .mp4 output
fourcc = cv2.VideoWriter_fourcc(*'X264') 
out = cv2.VideoWriter(f"{args.output_video_path}/output.mp4", fourcc, fps, (frame_width, frame_height))

# Read frames
# tqdm for a progress bar (otherwise a while-loop would be sufficent)
for i, frame in enumerate(tqdm(frame_generator(cap), desc="Frames")):
    if random.random() < args.chance:
        cv2.imwrite(f'{args.output_image_path}/{i}.jpg', frame)
    else:
        out.write(frame)

# Release everything
cap.release()
out.release()
cv2.destroyAllWindows()
