import com.android.build.gradle.BaseExtension
import java.io.File
import java.util.Properties

fun BaseExtension.baseAndroidConfig() {
    setCompileSdkVersion(AndroidConst.COMPILE_SKD)

    defaultConfig {
        minSdk = AndroidConst.MIN_SKD

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release-signed") {
            val properties = Properties()
            properties.load(File("secrets.properties").inputStream())

            storeFile = File(properties.getProperty("KEY_STORE_PATH"))
            storePassword = properties.getProperty("KEY_STORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("release-signed") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release-signed")
        }
    }

    compileOptions {
        sourceCompatibility = AndroidConst.COMPILE_JDK_VERSION
        targetCompatibility = AndroidConst.COMPILE_JDK_VERSION
    }

    kotlinOptions {
        jvmTarget = AndroidConst.KOTLIN_JVM_TARGET
    }
}

