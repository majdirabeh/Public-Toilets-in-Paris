plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "fr.dev.majdi.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/AL2.0")
            excludes.add("/META-INF/LGPL2.1")
            excludes.add("META-INF/services/javax.annotation.processing.Processor")
        }
    }
    kapt {
        correctErrorTypes = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    hilt {
        enableAggregatingTask = true
    }
    //Just add this for skip lint verification
    lint {
        abortOnError = false
        checkAllWarnings = false
    }
}

dependencies {
    implementation(project(":domain"))
    //Hilt library
    implementation("com.google.dagger:hilt-android:2.48")

    //Added for navigation Activity
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    kapt("com.google.dagger:hilt-android-compiler:2.44")
    //Navigation compose
    implementation("androidx.navigation:navigation-compose:2.7.5")
    //Compose
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.animation:animation:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

    //RxJava Library
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    //Icons library
    implementation("androidx.compose.material:material-icons-extended-android:1.5.4")
    /*mapbox*/
    api ("com.mapbox.maps:android:10.16.2")
    implementation("com.mapbox.navigation:android:2.17.5")
    api ("com.mapbox.mapboxsdk:mapbox-sdk-turf:6.15.0")
    //Animation Splash
    implementation("com.airbnb.android:lottie-compose:6.1.0")
}