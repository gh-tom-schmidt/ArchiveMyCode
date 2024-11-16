import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from tqdm import tqdm

from torch.utils.data import DataLoader
from torchvision import datasets, transforms

# Define workfolder path
workfolder = "Temp/"

# Define transformations tp apply to all images on load
transform = transforms.Compose([
    transforms.Resize((128, 128)),  # Rescale images to 128x128
    transforms.Grayscale(num_output_channels=1), # RGB to Gray
    transforms.ToTensor(),  # Convert images to tensors
    transforms.Normalize((0.5,), (0.5,))  # Normalize the images
])


# Load the data in batches. Data should be normalized, tensors and resized.
train_dataset = datasets.ImageFolder(root=workfolder + "train/", transform=transform)
train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)

test_dataset = datasets.ImageFolder(root=workfolder + "test/", transform=transform)
test_loader = DataLoader(test_dataset, batch_size=1, shuffle=True)

# Check and select Cuda
print(f"Running with Cuda: {torch.cuda.is_available()}")
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Define the model
class MyModel(nn.Module):
    def __init__(self):
        super(MyModel, self).__init__()

        self.conv1 = nn.Conv2d(1, 32, kernel_size=3, padding=1)
        self.conv2 = nn.Conv2d(32, 64, kernel_size=3, padding=1)
        self.pool1 = nn.MaxPool2d(2, 2)

        self.conv3 = nn.Conv2d(64, 64, kernel_size=3, padding=1)
        self.conv4 = nn.Conv2d(64, 128, kernel_size=3, padding=1)
        self.pool2 = nn.MaxPool2d(2, 2)

        self.conv5 = nn.Conv2d(128, 128, kernel_size=3, padding=1)
        self.conv6 = nn.Conv2d(128, 256, kernel_size=3, padding=1)
        self.global_avg_pool = nn.AdaptiveAvgPool2d((1, 1))

        self.fc1 = nn.Linear(256, 1024)
        self.fc2 = nn.Linear(1024, 2)

    def forward(self, x):
        # Define the forward pass
        x = F.relu(self.conv1(x))
        x = F.relu(self.conv2(x))
        x = self.pool1(x)

        x = F.relu(self.conv3(x))
        x = F.relu(self.conv4(x))
        x = self.pool2(x)

        x = F.relu(self.conv5(x))
        x = F.relu(self.conv6(x))
        x = self.global_avg_pool(x)
        x = torch.flatten(x, 1)  # Flatten the tensor

        x = F.relu(self.fc1(x))
        x = self.fc2(x)
        return x

# Instantiate the model and push on gpu if avaible
model = MyModel().to(device)

# Define the optimizer
optimizer = optim.Adam(model.parameters(), lr=0.001)

# Define the loss function
# CrossEntropyLoss need raw output, so no activation and softmax at the end
criterion = nn.CrossEntropyLoss()

# Training
num_epochs = 2

for epoch in range(num_epochs):
    # Set the model to training mode
    model.train()  
    # Metrics
    running_loss = 0.0

    for inputs, labels in tqdm(train_loader, desc="Training"): # tqdm for procress bar
        # Zero the parameter gradients for correct backprop
        # because of accumulation
        optimizer.zero_grad()

        # Move to GPU
        inputs = inputs.to(device)
        labels = labels.to(device)

        # Forward pass
        outputs = model(inputs)

        # Compute the loss
        loss = criterion(outputs, labels)

        # Backward pass and optimization
        loss.backward()
        optimizer.step()

        # Accumulate loss
        running_loss += loss.item()

    # Print statistics after each epoch
    train_loss = running_loss / len(train_loader.dataset)


    # Set the model to evaluate mode
    model.eval()
    # Rest Metrics
    running_loss = 0.0

    # disable backprop step notes
    with torch.no_grad(): 
      for inputs, labels in tqdm(test_loader, desc="Validation"):

        # Move to GPU
        inputs = inputs.to(device)
        labels = labels.to(device)

        # Forward pass
        outputs = model(inputs)

        # Compute the loss
        loss = criterion(outputs, labels)

        # Accumulate loss
        running_loss += loss.item()
    
    val_loss = running_loss / len(test_loader.dataset)

    print(f'Epoch [{epoch+1}/{num_epochs}], Train Loss: {train_loss:.4f}, Val Loss: {val_loss:.4f}')