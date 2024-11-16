from ultralytics import YOLO
import cv2

# Load a model
model = YOLO("best.pt")  # pretrained YOLOv8n model

# Read an image using OpenCV
source = cv2.imread("4902.jpg")

# Run inference on the source
results = model(source)

# Process results list
for result in results:
    boxes = result.boxes  # Boxes object for bounding box outputs
    masks = result.masks  # Masks object for segmentation masks outputs
    keypoints = result.keypoints  # Keypoints object for pose outputs
    probs = result.probs  # Probs object for classification outputs
    obb = result.obb  # Oriented boxes object for OBB outputs
    result.show()  # display to screen
    result.save(filename="result.jpg")  # save to disk