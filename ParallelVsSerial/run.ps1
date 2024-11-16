Write-Host "Step 1: Move to <Cpp>..."
Set-Location -Path "Cpp"

Write-Host "Step 2: Running make file..."
make

Write-Host "Step 2: Running ./main with 1000 10 1000 12"
.\main.exe 1000 9 1000 12 "..\Output\tmt.out"

Write-Host "Step 3: Run make clean"
make clean

Write-Host "Step 4: Move to <Python>"
Set-Location -Path "..\Python"

Write-Host "Step 5: Running python show.py..."
python show.py

Write-Host "Done"
