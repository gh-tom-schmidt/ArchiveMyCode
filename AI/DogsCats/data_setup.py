import os
import shutil
import random

print("Create an empty working directory with folders in it")
if "Temp" in os.listdir():
  shutil.rmtree("Temp")
os.mkdir("Temp")
workfolder = "Temp/"

os.mkdir(workfolder + "train")
os.mkdir(workfolder + "test")

os.mkdir(workfolder + "train/" + "cats")
os.mkdir(workfolder + "train/" + "dogs")

os.mkdir(workfolder + "test/" + "cats")
os.mkdir(workfolder + "test/" + "dogs")

#define some paths
datapath = "Data/PetImages/"
dog_path = datapath + "Dog/"
cat_path = datapath + "Cat/"

# Some images are empty !!! Thats why we have to remove them.
for file in os.listdir(datapath + 'Dog'):
  path = datapath + "Dog/" + file

  # Check if the file is empty
  if os.path.getsize(path) == 0:
      print(f"Deleting empty file: {file}")
      os.remove(path)

for file in os.listdir(datapath + 'Cat'):
  path = datapath + "Cat/" + file

  # Check if the file is empty
  if os.path.getsize(path) == 0:

      print(f"Deleting empty file: {file}")
      os.remove(path)

# Get the file names out of the folders and shuffel them
dogs = os.listdir(datapath + 'Dog')
cats = os.listdir(datapath + 'Cat')

dogs = random.sample(dogs, len(dogs))
cats = random.sample(cats, len(cats))

# Create a train and test set (90% / 10%)
train_dogs = dogs[:int(0.9 * len(dogs))]
test_dogs = dogs[int(0.9 * len(dogs)):]

train_cats = cats[:int(0.9 * len(cats))]
test_cats = cats[int(0.9 * len(cats)):]

print("Copy from the original folder to the working directory...")
for file in train_dogs:
    shutil.copyfile(datapath + "Dog/" + file, workfolder + "train/" + "dogs/" + file)
for file in test_dogs:
    shutil.copyfile(datapath + "Dog/" + file, workfolder + "test/" + "dogs/" + file)
print("Dogs done.")

for file in train_cats:
    shutil.copyfile(datapath + "Cat/" + file, workfolder + "train/" + "cats/" + file)
for file in test_cats:
    shutil.copyfile(datapath + "Cat/" + file, workfolder + "test/" + "cats/" + file)
print("Cats done.")

