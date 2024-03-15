scp -o StrictHostKeyChecking=no marco@ixigo.selfip.net:/home/marco/cs2/game/csgo/replays/*.dem ./

$demFiles = Get-ChildItem -Filter "*.dem"
foreach ($file in $demFiles) {
    $apiUrl = "https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/files"
    
    Write-Host "Uploading file: $($file.FullName)"

    $multipartContent = [System.Net.Http.MultipartFormDataContent]::new()
    $multipartFile =  $file.FullName
    $FileStream = [System.IO.FileStream]::new($multipartFile, [System.IO.FileMode]::Open)
    $fileHeader = [System.Net.Http.Headers.ContentDispositionHeaderValue]::new("form-data")
    $fileHeader.Name = "file"
    $fileHeader.FileName = $($file.Name)
    $fileContent = [System.Net.Http.StreamContent]::new($FileStream)
    $fileContent.Headers.ContentDisposition = $fileHeader
    $multipartContent.Add($fileContent)

    $body = $multipartContent

    Invoke-RestMethod $apiUrl -Method 'POST' -Headers $headers -Body $body
}

Write-Host "Triggering parse DEM files"
$response = Invoke-RestMethod 'https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/parse/all' -Method 'POST' -Headers $headers
$response | ConvertTo-Json