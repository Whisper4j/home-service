param(
    [ValidateSet('deploy', 'start', 'reload', 'stop', 'test')]
    [string]$Action = 'deploy'
)

$ErrorActionPreference = 'Stop'

$webRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$repoRoot = (Resolve-Path (Join-Path $webRoot '..')).Path
$nginxRoot = Join-Path $repoRoot '.runtime\nginx'
$nginxExe = Join-Path $nginxRoot 'nginx.exe'
$nginxConfigSource = Join-Path $webRoot 'deploy\nginx.conf'
$nginxConfigTarget = Join-Path $nginxRoot 'conf\nginx.conf'
$distRoot = Join-Path $webRoot 'dist'
$siteRoot = Join-Path $nginxRoot 'html\home-service'

function Assert-NginxRuntime {
    if (-not (Test-Path -LiteralPath $nginxExe -PathType Leaf)) {
        throw "未找到本地 Nginx：$nginxExe"
    }
}

function Initialize-NginxDirectories {
    @(
        'conf',
        'html',
        'logs',
        'temp\client_body_temp',
        'temp\proxy_temp',
        'temp\fastcgi_temp',
        'temp\uwsgi_temp',
        'temp\scgi_temp'
    ) | ForEach-Object {
        New-Item -ItemType Directory -Path (Join-Path $nginxRoot $_) -Force | Out-Null
    }
}

function Invoke-Nginx {
    param([string[]]$Arguments)

    Push-Location $nginxRoot
    try {
        & $nginxExe @Arguments
        if ($LASTEXITCODE -ne 0) {
            throw "Nginx 命令执行失败，退出码：$LASTEXITCODE"
        }
    }
    finally {
        Pop-Location
    }
}

function Get-LocalNginxProcess {
    Get-Process nginx -ErrorAction SilentlyContinue |
        Where-Object { $_.Path -and $_.Path.StartsWith($nginxRoot, [System.StringComparison]::OrdinalIgnoreCase) }
}

function Test-NginxConfig {
    Copy-Item -LiteralPath $nginxConfigSource -Destination $nginxConfigTarget -Force
    Invoke-Nginx -Arguments @('-t')
}

function Start-LocalNginx {
    Test-NginxConfig
    if (Get-LocalNginxProcess) {
        Write-Host '本地 Nginx 已经在运行。'
        return
    }
    Start-Process -FilePath $nginxExe -WorkingDirectory $nginxRoot -WindowStyle Hidden
    Start-Sleep -Milliseconds 500
    if (-not (Get-LocalNginxProcess)) {
        throw 'Nginx 启动失败，请检查 .runtime/nginx/logs/error.log。'
    }
    Write-Host 'Nginx 已启动：http://localhost/'
}

Assert-NginxRuntime
Initialize-NginxDirectories

switch ($Action) {
    'deploy' {
        Push-Location $webRoot
        try {
            & npm run build
            if ($LASTEXITCODE -ne 0) {
                throw "前端构建失败，退出码：$LASTEXITCODE"
            }
        }
        finally {
            Pop-Location
        }

        if (-not (Test-Path -LiteralPath (Join-Path $distRoot 'index.html') -PathType Leaf)) {
            throw "未找到前端构建产物：$distRoot"
        }

        $resolvedNginxRoot = [System.IO.Path]::GetFullPath($nginxRoot)
        $resolvedSiteRoot = [System.IO.Path]::GetFullPath($siteRoot)
        if (-not $resolvedSiteRoot.StartsWith($resolvedNginxRoot, [System.StringComparison]::OrdinalIgnoreCase)) {
            throw "拒绝清理 Nginx 运行目录之外的路径：$resolvedSiteRoot"
        }

        if (Test-Path -LiteralPath $siteRoot) {
            Remove-Item -LiteralPath $siteRoot -Recurse -Force
        }
        New-Item -ItemType Directory -Path $siteRoot -Force | Out-Null
        Copy-Item -Path (Join-Path $distRoot '*') -Destination $siteRoot -Recurse -Force

        Test-NginxConfig
        if (Get-LocalNginxProcess) {
            Invoke-Nginx -Arguments @('-s', 'reload')
            Write-Host '前端已重新部署并重载 Nginx：http://localhost/'
        }
        else {
            Start-LocalNginx
        }
    }
    'start' {
        Start-LocalNginx
    }
    'reload' {
        Test-NginxConfig
        if (-not (Get-LocalNginxProcess)) {
            throw '本地 Nginx 尚未启动，请先执行 npm run nginx:start。'
        }
        Invoke-Nginx -Arguments @('-s', 'reload')
        Write-Host 'Nginx 配置已重载。'
    }
    'stop' {
        if (Get-LocalNginxProcess) {
            Invoke-Nginx -Arguments @('-s', 'quit')
            Write-Host 'Nginx 已停止。'
        }
        else {
            Write-Host '本地 Nginx 当前未运行。'
        }
    }
    'test' {
        Test-NginxConfig
    }
}
