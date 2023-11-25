plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //Adding Plugin Hilt
    id("dagger.hilt.android.plugin")
    //Adding Plugin Kapt
    id("kotlin-kapt")
}

android {
    namespace = "fr.dev.majdi.publictoilets"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.dev.majdi.publictoilets"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {

    implementation (project(":data"))
    implementation (project(":domain"))
    implementation (project(":presentation"))

    //Hilt library
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("androidx.compose.runtime:runtime:1.5.4")

    //Activate multidex
    implementation("androidx.multidex:multidex:2.0.1")

}