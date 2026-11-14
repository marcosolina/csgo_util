<#
.SYNOPSIS
    Builds the IxiGo images for amd64 and arm64 and pushes them to the private registry.

.DESCRIPTION
    Wraps "docker buildx bake -f docker/compose.build.yml". Each image is pushed with -Tag
    (default "latest") and with the short git commit, so a host can roll back to an exact build.
    The images are built from the local working tree, so a push refuses to run with uncommitted
    changes unless -AllowDirty is given (then only -Tag is pushed, without the commit tag).
    Log in once before pushing: docker login docker-registry.marco.selfip.net

.EXAMPLE
    .\docker\build-and-push.ps1                          # build and push every image
.EXAMPLE
    .\docker\build-and-push.ps1 -Images dem-manager,ui   # only these images
.EXAMPLE
    .\docker\build-and-push.ps1 -NoPush                  # build check only, nothing is pushed
#>
param(
    # Compose service names from compose.build.yml; empty means every image
    [string[]] $Images = @(),
    [string] $Tag = "latest",
    [switch] $NoPush,
    [switch] $AllowDirty,
    [string] $Builder = "ixigo"
)

$ErrorActionPreference = "Stop"

function Assert-ExitCode([string] $What) {
    if ($LASTEXITCODE -ne 0) { throw "$What failed with exit code $LASTEXITCODE" }
}

# Run from this folder: buildx bake resolves the compose "context: .." against the current directory
Push-Location $PSScriptRoot
try {
    # Multi-arch images need a builder with the docker-container driver
    docker buildx inspect $Builder *> $null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Creating buildx builder '$Builder'" -ForegroundColor Yellow
        docker buildx create --name $Builder --driver docker-container --bootstrap | Out-Null
        Assert-ExitCode "docker buildx create"
    }

    $tags = @($Tag)
    if (-not $NoPush) {
        $dirty = [bool](git status --porcelain)
        if ($dirty -and -not $AllowDirty) {
            throw "The working tree has uncommitted changes and the images are built from it. Commit first, or pass -AllowDirty to push only '$Tag'."
        }
        if (-not $dirty) { $tags += (git rev-parse --short HEAD).Trim() }
    }

    foreach ($t in $tags) {
        $env:TAG = $t
        $bakeArgs = @("buildx", "bake", "--builder", $Builder, "-f", "compose.build.yml")
        if (-not $NoPush) { $bakeArgs += "--push" }
        $bakeArgs += $Images
        Write-Host "TAG=$t docker $($bakeArgs -join ' ')" -ForegroundColor Cyan
        & docker @bakeArgs
        Assert-ExitCode "docker buildx bake (TAG=$t)"
    }

    if ($NoPush) {
        Write-Host "Build finished; nothing pushed (the result stays in the '$Builder' builder cache)." -ForegroundColor Green
    } else {
        Write-Host "Pushed tags: $($tags -join ', ')" -ForegroundColor Green
    }
}
finally {
    Remove-Item Env:TAG -ErrorAction SilentlyContinue
    Pop-Location
}
