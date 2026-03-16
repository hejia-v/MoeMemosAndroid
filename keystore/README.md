# Release Signing

This document is stored under the ignored `keystore/` directory on purpose.

## Keystore

- File: `C:\Dev\MoeMemosAndroid\keystore\moememos-release.jks`
- Alias: `moememos`
- Store password: `1jyLW8#FQW4DWorwtUG@gB2J`
- Key password: `1jyLW8#FQW4DWorwtUG@gB2J`
- Validity: `13000` days

## Certificate

- DN: `CN=MoeMemosAndroid, OU=Android, O=Local Build, L=Shanghai, ST=Shanghai, C=CN`
- SHA-256: `b54f8e66dbc86c8d289224eb12894442df7d9ee3b323ced1497b50d912327ec1`
- SHA-1: `bd30cd03f12ee49c63b1a947df849baed04b1956`
- MD5: `24e8b2dd2f20e9496f8fe0523a9115d9`

## Build Command

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
$env:ANDROID_HOME='C:\Android\Sdk'
$env:ANDROID_SDK_ROOT='C:\Android\Sdk'
$env:ANDROID_SIGNING_STORE_FILE='C:\Dev\MoeMemosAndroid\keystore\moememos-release.jks'
$env:ANDROID_SIGNING_STORE_PASSWORD='1jyLW8#FQW4DWorwtUG@gB2J'
$env:ANDROID_SIGNING_KEY_ALIAS='moememos'
$env:ANDROID_SIGNING_KEY_PASSWORD='1jyLW8#FQW4DWorwtUG@gB2J'
.\gradlew.bat clean assembleRelease
```

## Output

- Signed APK: `C:\Dev\MoeMemosAndroid\app\build\outputs\apk\release\app-release.apk`
