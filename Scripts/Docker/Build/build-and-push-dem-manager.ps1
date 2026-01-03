# Build and Push DEM Manager Docker Image
# This script builds the dem-manager service using docker compose and pushes it to the registry

# Set error action preference to stop on errors
$ErrorActionPreference = "Stop"

# Define variables
$ComposeFile = "docker-compose-build-dem-manager.yml"
$ImageName = "docker-registry.marco.selfip.net/ixigo/dem-manager:latest"
$ServiceName = "dem-manager"

# Get the script directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Building DEM Manager Docker Image" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Change to the script directory
Push-Location $ScriptDir

try {
    # Build the service using docker compose
    Write-Host "Building service: $ServiceName" -ForegroundColor Yellow
    Write-Host "Using compose file: $ComposeFile" -ForegroundColor Yellow
    Write-Host ""
    
    docker compose -f $ComposeFile build --no-cache
    
    if ($LASTEXITCODE -ne 0) {
        throw "Docker compose build failed with exit code $LASTEXITCODE"
    }
    
    Write-Host ""
    Write-Host "Build completed successfully!" -ForegroundColor Green
    Write-Host ""
    
    # Push the image to the registry
    Write-Host "Pushing image: $ImageName" -ForegroundColor Yellow
    Write-Host ""
    
    docker push $ImageName
    
    if ($LASTEXITCODE -ne 0) {
        throw "Docker push failed with exit code $LASTEXITCODE"
    }
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "Image pushed successfully!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Image: $ImageName" -ForegroundColor Cyan
    
} catch {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "Error occurred!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
} finally {
    # Return to the original directory
    Pop-Location
}
