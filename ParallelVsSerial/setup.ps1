if (Test-Path -Path "env") {
    Write-Host "Activating existing virtual environment..."
    & "env\bin\Activate.ps1"

} else {
    Write-Host "Creating a new virtual environment..."
    python -m venv env
    
    Write-Host "Activating the new virtual environment..."
    & "env\bin\Activate.ps1"
    
    Write-Host "Installing pandas and matplotlib..."
    pip install pandas matplotlib
}
