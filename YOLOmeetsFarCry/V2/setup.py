import os
import shutil
import random
from tqdm import tqdm

# make train and test set with desired folder structur to use on yolov8

print("Create an empty working directory with folders in it")
if "Temp" in os.listdir():
  shutil.rmtree("Temp")
os.mkdir("Temp")
workfolder = "Temp/"

os.mkdir(workfolder + "train")
os.mkdir(workfolder + "valid")

os.mkdir(workfolder + "train/" + "images")
os.mkdir(workfolder + "train/" + "labels")

os.mkdir(workfolder + "valid/" + "images")
os.mkdir(workfolder + "valid/" + "labels")

path_annotations = "Data/Input/Annotations"
path_images = "Data/Input/Images"

labels = os.listdir(path_annotations)
labels = random.sample(labels, len(labels))

# use 90% for traing
train_set = labels[:int(0.9 * len(labels))]
valid_set = labels[int(0.9 * len(labels)):]

for file in tqdm(train_set, desc="Copy Trainingdata"):
    # remove the file extension
    file = os.path.splitext(file)[0]
    # copy labels
    shutil.copyfile(path_annotations + "/" + file + ".txt", workfolder + "train/" + "labels/" + file + ".txt")
    # copy images
    shutil.copyfile(path_images + "/" + file + ".jpg", workfolder + "train/" + "images/" + file + ".jpg")


for file in tqdm(valid_set, desc="Copy Validationdata"):
    # remove the file extension
    file = os.path.splitext(file)[0]
    # copy labels
    shutil.copyfile(path_annotations + "/" + file + ".txt", workfolder + "valid/" + "labels/" + file + ".txt")
    # copy images
    shutil.copyfile(path_images + "/" + file + ".jpg", workfolder + "valid/" + "images/" + file + ".jpg")