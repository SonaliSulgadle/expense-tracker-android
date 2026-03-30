import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)                  // KSP: generates code for Room & Hilt
    alias(libs.plugins.hilt)                 // Hilt DI
    alias(libs.plugins.kotlin.serialization) // for parsing Gemini JSON
}

android {
    namespace = "com.sonalisulgadle.expensetracker"
    compileSdk {
        version = release(36)
    }
    val geminiKey =
        gradleLocalProperties(rootDir, providers).getProperty("GEMINI_API_KEY") ?: ""

    defaultConfig {
        applicationId = "com.sonalisulgadle.expensetracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    // --- existing ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- Hilt (Dependency Injection) ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)                  // KSP generates Hilt boilerplate at compile time
    implementation(libs.hilt.navigation.compose)

    // --- Room (Local Database) ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)            // adds coroutine + Flow support to Room
    ksp(libs.room.compiler)                  // KSP generates the DAO implementation

    // --- Navigation ---
    implementation(libs.navigation.compose)

    // --- ViewModel ---
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    implementation(libs.datastore.preferences)

    // --- Kotlinx Serialization ---
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.timber)

    implementation(libs.mockito)
    implementation(libs.coroutine.test)
    implementation(libs.core.testing)
    implementation(libs.turbine)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
