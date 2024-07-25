import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.androidTestImplementation
import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.debugImplementation
import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.implementation
import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.testImplementation
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
        viewBinding = true
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

tasks.withType<Test> {
    useJUnitPlatform()
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

    // divkit
    implementation(libs.yandex.div)
    implementation(libs.yandex.div.core)
    implementation(libs.yandex.div.json)
    implementation(libs.yandex.div.utils)
    implementation(libs.yandex.div.picasso)
    implementation(libs.okhttp3)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.mock)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.test.runner)

    implementation(libs.androidx.ui.viewbinding)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.tooling.preview)
}