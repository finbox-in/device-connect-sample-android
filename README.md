# DeviceConnect Android Sample

This is sample implementation of the DeviceConnect Android SDK which has permission handling, user creation & background data sync.

## Setup
### Download the SDK

### Adding Dependency
In the project level build.gradle file or settings.gradle, add the repository URLs to all allprojects block or repositories block inside dependencyResolutionManagement.
```
maven {
    setUrl("s3://risk-manager-android-sdk/artifacts")
    credentials(AwsCredentials::class) {
        accessKey = <ACCESS_KEY>
        secretKey = <SECRET_KEY>
    }
    content {
        includeGroup("in.finbox")
    }
}
```


Now add the dependency to module level build.gradle.kts or build.gradle file:

```
implementation("in.finbox:mobileriskmanager:<DC_SDK_VERSION>:<DC_FLAVOR>-release@aar") {
    isTransitive = true
}
implementation("in.finbox:common:<COMMON_SDK_VERSION>:<COMMON_FLAVOR>-release@aar") {
    isTransitive = true
}
implementation("in.finbox:logger:<LOGGER_SDK_VERSION>:parent-release@aar") {
    isTransitive = true
}
```

Add the following values to local.properties
```
ACCESS_KEY=<ACCESS_KEY>
SECRET_KEY=<SECRET_KEY>
DC_SDK_VERSION=<DC_SDK_VERSION>
DC_FLAVOR=<DC_FLAVOR>
COMMON_SDK_VERSION=<COMMON_SDK_VERSION>
COMMON_FLAVOR=<COMMON_FLAVOR>
LOGGER_SDK_VERSION=<LOGGER_SDK_VERSION>
```

Note: These values will be provided by the FinBox team during integration.

## Integration
For more details on integration, refer to https://docs.finbox.in/device-connect/android.html
