import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

configure<BaseAppModuleExtension> {
    baseAndroidConfig()
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AndroidConst.COMPOSE_COMPILER_VERSION
    }
    applicationVariants.all {
        outputs.all {
            val variantOutputImpl =
                this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            variantOutputImpl.outputFileName =
                "todoapp-${buildType.name}-${defaultConfig.versionName}.apk"
        }
    }
    defaultConfig {
        val properties = Properties()
        properties.load(File("secrets.properties").inputStream())
        manifestPlaceholders["YANDEX_CLIENT_ID"] = properties.getProperty("YANDEX_CLIENT_ID")
    }
}

dependencies {
    // Dagger 2
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Work manager
    implementation(libs.androidx.work.runtime.ktx)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Compose navigation
    implementation(libs.androidx.navigation.compose)

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
