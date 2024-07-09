import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

android {
    namespace = "ru.gribbirg.todoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.gribbirg.todoapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        manifestPlaceholders["YANDEX_CLIENT_ID"] = properties.getProperty("YANDEX_CLIENT_ID")
    }
}

detekt {
    toolVersion = "1.23.6"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true

}

dependencies {

    // Yandex login sdk
    implementation(libs.authsdk)

    // Work manager
    implementation(libs.androidx.work.runtime.ktx)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Ktor
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.websockets)
    runtimeOnly(libs.ktor.client.auth)
    implementation(libs.ktor.client.android)
    implementation(libs.kotlin.stdlib.jdk8)


    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)


    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Date picker
    implementation(libs.material3)

    // Compose navigation
    implementation(libs.androidx.navigation.compose)

    // System bar colors
    implementation(libs.accompanist.systemuicontroller)

    // Collapsing Toolbar Layout
    implementation(libs.toolbar.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}