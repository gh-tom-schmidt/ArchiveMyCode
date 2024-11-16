from ultralytics import YOLO

# Load a model
model = YOLO("yolov8m.pt")  # load a pretrained model (recommended for training)

# the if line prevent an error that comes possible from an multip processing error
if __name__ == '__main__':
    # Train the model
    model.train(data="data.yaml", epochs=100, cache=True, batch=-1, device=0)
    model.val()
    model.export(format="torchscript")