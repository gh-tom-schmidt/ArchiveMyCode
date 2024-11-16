import glob
import cv2
import torch
import matplotlib.pyplot as plt
import matplotlib
matplotlib.use('TkAgg')


def capture_image():

    cap = cv2.VideoCapture(0)
    print("Press 'e' to take a foto.")

    while True:

        ret, frame = cap.read()

        cv2.imshow('Preview', frame)
        key = cv2.waitKey(10)

        if key == ord('e'):
            cv2.destroyAllWindows()
            cap.release()

            return frame


midas_transforms = torch.hub.load("intel-isl/MiDaS", "transforms")
transform = midas_transforms.small_transform

midas = torch.hub.load("intel-isl/MiDaS", "MiDaS_small")

device = torch.device(
    "cuda") if torch.cuda.is_available() else torch.device("cpu")
midas.to(device)
midas.eval()

# ------------------------------------------------------------------

while True:

    print(f"Take image ...")
    img = capture_image()

    print("Transforme...")
    input_batch = transform(img).to(device)

    with torch.no_grad():
        print("Make prediction...")
        prediction = midas(input_batch)

        print("Interpolate to output...")
        prediction = torch.nn.functional.interpolate(
            prediction.unsqueeze(1),
            size=img.shape[:2],
            mode="bicubic",
            align_corners=False,
        ).squeeze()

    output = prediction.cpu().numpy()

    # ------------------------------------------------

    print("Done.")
    fig, axes = plt.subplots(1, 2, figsize=(15, 7))
    ax1, ax2 = axes.ravel()

    ax1.imshow(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
    ax1.axis('off')

    ax2.imshow(output)
    ax2.axis('off')

    plt.tight_layout()
    plt.savefig('temp_plot.png')

    cv2.imshow("Output", cv2.imread('temp_plot.png'))

    # ------------------------------------------------

    print("Press 'e' to continue or 'q' to quit.")
    key = cv2.waitKey(0)

    # left mouse button = ASCII value 32
    if key == ord('e'):
        print("Next.")

    elif key == ord('q'):
        print("End.")
        break

cv2.destroyAllWindows()
plt.close()
