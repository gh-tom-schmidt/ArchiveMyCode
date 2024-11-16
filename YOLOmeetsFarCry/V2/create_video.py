# python create_video.py "Data/Input/FarCry_11-08.mp4" "Results" "Trained/medium_100_defaults.pt"
from ultralytics import YOLO
import cv2
import argparse
from tqdm import tqdm


# Set up argument parser
parser = argparse.ArgumentParser(description="Run a video trough the AI.")
parser.add_argument('input_video_path', type=str, help='Input video file.')
parser.add_argument('output_video_path', type=str, help='Path to save the output video file.')
parser.add_argument('model_path', type=str, help='Path to the trained model.')
args = parser.parse_args()

# Load a pretrained YOLOv8n model
model = YOLO(args.model_path)
results = model.predict(args.input_video_path, stream=True, save=True, conf=0.6, project=args.output_video_path, exist_ok=True, device=0) 
for _ in results:
    pass