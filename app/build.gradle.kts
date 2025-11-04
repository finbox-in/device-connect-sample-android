plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "in.finbox.deviceconnectsample"
    compileSdk = 36

    defaultConfig {
        applicationId = "in.finbox.deviceconnectsample"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /**********************
     * FinBox Dependencies *
     **********************/
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    val DC_SDK_VERSION = "6.10.0"
    val DC_FLAVOR = "ind"
    val COMMON_SDK_VERSION = "2.10.0"
    val COMMON_FLAVOR = "parent"
    val LOGGER_SDK_VERSION = "2.10.0"

    implementation("in.finbox:mobileriskmanager:${DC_SDK_VERSION}:${DC_FLAVOR}-release@aar") {
        isTransitive = true
    }
    implementation("in.finbox:common:${COMMON_SDK_VERSION}:${COMMON_FLAVOR}-release@aar") {
        isTransitive = true
    }
    implementation("in.finbox:logger:${LOGGER_SDK_VERSION}:parent-release@aar") {
        isTransitive = true
    }
}