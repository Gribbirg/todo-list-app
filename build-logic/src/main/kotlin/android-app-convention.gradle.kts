import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("org.jetbrains.kotlin.plugin.compose")
}

configure<BaseAppModuleExtension> {
    baseAndroidConfig()
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
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
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}