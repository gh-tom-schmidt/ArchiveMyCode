import time
import glob
import cv2
import torch
import matplotlib.pyplot as plt
import matplotlib
matplotlib.use('TkAgg')

midas_transforms = torch.hub.load("intel-isl/MiDaS", "transforms")
transform = midas_transforms.small_transform

midas = torch.hub.load("intel-isl/MiDaS", "MiDaS_small")

device = torch.device(
    "cuda") if torch.cuda.is_available() else torch.device("cpu")
midas.to(device)
midas.eval()

# ------------------------------------------------------------------

cap = cv2.VideoCapture(0)

while True:

    start_time = time.time()

    ret, img = cap.read()

    input_batch = transform(img).to(device)

    with torch.no_grad():
        prediction = midas(input_batch)

        prediction = torch.nn.functional.interpolate(
            prediction.unsqueeze(1),
            size=img.shape[:2],
            mode="bicubic",
            align_corners=False,
        ).squeeze()

    output = prediction.cpu().numpy()

    # ------------------------------------------------

    plt.imshow(output)
    plt.pause(0.00001)

    cv2.imshow("Original", img)

    print(f"{time.time() - start_time}")

    # ------------------------------------------------

    key = cv2.waitKey(10)

    if key == ord('q'):
        print("End.")
        break

cv2.destroyAllWindows()
plt.close()
cap.release()
