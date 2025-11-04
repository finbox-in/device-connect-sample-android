import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

val properties = Properties().apply {
    val file = File("local.properties")
    if (file.exists()) {
        load(FileInputStream(file))
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("s3://risk-manager-android-sdk/artifacts")
            credentials(AwsCredentials::class) {
                accessKey = properties.getProperty("ACCESS_KEY")
                secretKey = properties.getProperty("SECRET_KEY")
            }
            content {
                includeGroup("in.finbox")
            }
        }

    }
}

rootProject.name = "DeviceConnectSample"
include(":app")
