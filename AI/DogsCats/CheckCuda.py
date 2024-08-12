import torch
print(f"Cuda: {torch.cuda.is_available()}")
# torch.zeros(1).cuda()