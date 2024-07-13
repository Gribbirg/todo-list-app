import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.implementation
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    baseAndroidConfig()
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AndroidConst.COMPOSE_COMPILER_VERSION
    }
    defaultConfig {
        val properties = Properties()
        properties.load(File("secrets.properties").inputStream())
        manifestPlaceholders["YANDEX_CLIENT_ID"] = properties.getProperty("YANDEX_CLIENT_ID")
    }
}

dependencies {
    // Yandex login sdk
    implementation(libs.authsdk)

    // Dagger 2
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

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
}