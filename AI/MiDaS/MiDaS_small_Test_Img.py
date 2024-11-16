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

folder_path = "Testimages/Dump/"
image_paths = glob.glob(folder_path + "*.jpeg")
image_paths.extend(glob.glob(folder_path + "*.jpg"))

current_index = 0

while True:

    print(f"Load img: {current_index} ...")
    img = cv2.imread(image_paths[current_index])
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

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

    ax1.imshow(img)
    ax1.axis('off')

    ax2.imshow(output)
    ax2.axis('off')

    plt.tight_layout()
    plt.savefig('temp_plot.png')

    cv2.imshow("Output", cv2.imread('temp_plot.png'))

    # ------------------------------------------------

    print("Wait for button press...")
    key = cv2.waitKey(0)

    # left mouse button = ASCII value 32
    if key == ord('e'):
        print("Next.")
        # if all images are viewed, start again
        current_index = (current_index + 1) % len(image_paths)

    elif key == ord('q'):
        print("End.")
        break

cv2.destroyAllWindows()
plt.close()
