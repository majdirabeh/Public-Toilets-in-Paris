plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //Adding Plugin Hilt
    id("dagger.hilt.android.plugin")
    //Adding Plugin Kapt
    id("kotlin-kapt")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    //Just add this for skip lint verification
    lint {
        abortOnError = false
        checkAllWarnings = false
    }
}

dependencies {

    implementation (project(":data"))
    implementation (project(":domain"))
    implementation (project(":presentation"))

    //Hilt library
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("androidx.compose.runtime:runtime:1.5.4")

    //Activate multidex
    implementation("androidx.multidex:multidex:2.0.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

}