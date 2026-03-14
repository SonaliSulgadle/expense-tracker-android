// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.ksp) apply false        // KSP: generates code for Room & Hilt
    alias(libs.plugins.hilt) apply false         // Hilt DI
    alias(libs.plugins.kotlin.serialization) apply false// for parsing Gemini JSON
}