# DeviceConnect Android Sample

This sample application demonstrates how to integrate the FinBox DeviceConnect Android SDK into an Android app — including initialization, permission handling, user creation, and background data sync.

## Prerequisites

* Android Studio (preferably recent version)
* Android SDK with minimum API level **21 (Android 5.0 Lollipop)** or above. ([docs.finbox.in][1])
* Kotlin (or Java) project using AndroidX
* Access to the FinBox S3 repository and credentials (provided by FinBox team)
* Client API Key and other credentials (provided by FinBox team)

## Setup Steps

### 1. Configure Gradle & Desugaring

In your module’s `build.gradle` (or `build.gradle.kts`), ensure you have:

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
        coreLibraryDesugaringEnabled true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    defaultConfig {
        minSdk 21
    }
}
dependencies {
    coreLibraryDesugaring "com.android.tools:desugar_jdk_libs:1.1.5"
}
```

This enables Java 8+ language API support and is required by the SDK. ([docs.finbox.in][1])

### 2. Add FinBox SDK Dependencies

In the project-level `build.gradle` or `settings.gradle`, ensure the S3 repository is added:

```groovy
allprojects {
    repositories {
        maven {
            url "s3://risk-manager-android-sdk/artifacts"
            credentials(AwsCredentials) {
                accessKey = "<ACCESS_KEY>"
                secretKey = "<SECRET_KEY>"
            }
            content {
                includeGroup("in.finbox")
            }
        }
        // … other repositories
    }
}
```

Then in the app (module) `build.gradle`:

```groovy
implementation "in.finbox:mobileriskmanager:<DC_SDK_VERSION>:<DC_FLAVOR>-release@aar"
implementation "in.finbox:common:<COMMON_SDK_VERSION>:<COMMON_FLAVOR>-release@aar"
implementation "in.finbox:logger:<LOGGER_SDK_VERSION>:parent-release@aar"
```

Replace `<DC_SDK_VERSION>`, `<DC_FLAVOR>`, `<COMMON_SDK_VERSION>`, `<COMMON_FLAVOR>`, and `<LOGGER_SDK_VERSION>` with the values provided by FinBox. ([docs.finbox.in][1])

### 3. Handling Permissions

Because the SDK collects device data (e.g., SMS, contacts, accounts) you must ask for runtime permissions appropriately. See sample code in the app for requesting permissions, handling callbacks, and enabling the integration button only when required permissions are granted.

### 4. Create User

Once your app is ready and permissions are obtained, call:

```kotlin
FinBox.createUser(
    clientApiKey = "YOUR_CLIENT_API_KEY",
    customerId = "YOUR_CUSTOMER_ID",
    callback = object : FinBox.FinBoxAuthCallback {
        override fun onSuccess(accessToken: String) {
            // User created successfully
        }
        override fun onError(errorCode: Int) {
            // Handle error
        }
    }
)
```

**Important constraints**:

* `CUSTOMER_ID` must be alphanumeric (no special characters) and not exceed 64 characters. ([docs.finbox.in][1])
* Only call the next step once `onSuccess()` returns.

### 5. Start Periodic Sync

After successful user creation, start the periodic data sync:

```kotlin
val finbox = FinBox()
finbox.startPeriodicSync()
```

This ensures the SDK collects and sends data in background at regular intervals. ([docs.finbox.in][1])

### 7. (Optional) Additional Features

* **Device Match**: Provide email, phone or name to enable device-side matching. ([docs.finbox.in][1])
* **Forward Notifications**: If your app uses Firebase Cloud Messaging, forward the data to FinBox so background sync can be resumed even if the app is killed. ([docs.finbox.in][1])
* **Multi-Process Support**: If your app uses multiple processes, disable the auto-init provider and manually initialize the SDK. ([docs.finbox.in][1])
* **Sync Frequency / Stop Sync / Reset / Forget User**: Use the respective SDK methods when needed. ([docs.finbox.in][1])

## Sample App Flow

1. Launch the sample app.
2. Request necessary permissions.
3. Once permissions are granted, enable the “Create User” button.
4. On tapping “Create User”, generate a valid `customerId` (32-char alphanumeric) and call `FinBox.createUser`.
5. On success, start periodic sync and proceed with your app’s logic.
6. On logout, call `finbox.stopPeriodicSync()` and `FinBox.resetData()` (and/or `FinBox.forgetUser()`) to clear data.

## Troubleshooting & Tips

* Ensure `minSdkVersion` is set to 21 (or above).
* If you see missing API errors, double-check desugaring is enabled.
* If user creation fails, log the `errorCode` from callback and consult the SDK’s “Error Codes” section.
* For OEM devices with aggressive background restrictions, ensure the FCM forwarding setup is correct.
* Always test permissions flow for SMS, Contacts, Accounts if using DeviceMatch.

## License & Attribution

This sample is provided for demonstration purposes of the FinBox DeviceConnect SDK. For production usage, refer to the official FinBox documentation and your FinBox integration contract.
