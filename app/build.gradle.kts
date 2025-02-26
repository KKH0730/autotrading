import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreFileInputStream = FileInputStream(keystorePropertiesFile)
val keystoreProperties = Properties()
keystoreProperties.load(keystoreFileInputStream)

android {
    namespace = Versions.NAMESPACE
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = Versions.APPLICATION_Id
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME

        testInstrumentationRunner = Versions.TEST_INSTRUMENTATION_RUNNER
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = keystoreProperties["storeFile"]?.let { file(it) }
            storePassword = keystoreProperties["storePassword"] as? String
            keyAlias = keystoreProperties["keyAlias"] as? String
            keyPassword = keystoreProperties["keyPassword"] as? String
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = ".debug"

            // proguard 설정
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["enableCrashReporting"] = false
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                // If you don't need crash reporting for your debug build,
                // you can speed up your build by disabling mapping file uploading.
                mappingFileUploadEnabled = false
            }

            // crashlytics 플러그인을 사용하지 않음
            extra.set("enableCrashlytics", false)
            // crashlytics 빌드 ID 업데이트 막기
            extra.set("alwaysUpdateBuildId", false)

            buildConfigField("String", "SECRET_KEY", localProperties["SECRET_KEY"].toString())
            buildConfigField("String", "ACCESS_KEY", localProperties["ACCESS_KEY"].toString())
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            // proguard 설정
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["enableCrashReporting"] = true

            buildConfigField("String", "SECRET_KEY", localProperties["SECRET_KEY"].toString())
            buildConfigField("String", "ACCESS_KEY", localProperties["ACCESS_KEY"].toString())
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    // AndroidX
    implementation(Dependencies.AndroidX.ANDROID_CORE)
    implementation(Dependencies.AndroidX.ANDROID_LIFECYCLE)
    implementation(Dependencies.AndroidX.ANDROID_ACTIVITY_COMPOSE)
    implementation(platform(Dependencies.AndroidX.COMPOSE_BOM))
    implementation(Dependencies.AndroidX.COMPOSE_UI)
    implementation(Dependencies.AndroidX.COMPOSE_UI_GRAPHICS)
    implementation(Dependencies.AndroidX.COMPOSE_UI_TOOLING_PREVIEW)
    implementation(Dependencies.AndroidX.COMPOSE_MATERIAL3)
    implementation(Dependencies.AndroidX.SPLASHSCREEN)
    implementation(Dependencies.AndroidX.NAVIGATION_COMPOSE)
    implementation(Dependencies.AndroidX.HILT_NAVIGATION_COMPOSE)
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.AndroidX.COMPOSE_MATERIAL)
    implementation(Dependencies.AndroidX.ANDROID_MATERIAL)

    // Firebase
    implementation(platform(Dependencies.Firebase.FIREBASE_BOM))
    implementation(Dependencies.Firebase.FIREBASE_CRASHLYTICS)
    implementation(Dependencies.Firebase.FIREBASE_FIRESTORE)

    //Paging
    implementation(Dependencies.AndroidX.Paging.PAGING_RUNTIME)
    implementation(Dependencies.AndroidX.Paging.PAGING_COMPOSE)

    // Hilt
    implementation(Dependencies.Hilt.ANDROID_HILT)
    kapt(Dependencies.Hilt.ANDROID_HILT_COMPILER)
    testImplementation(Dependencies.Hilt.ANDROID_HILT_TESTING)

    // OkHttp3
    implementation(platform(Dependencies.OkHttp3.OKHTTP3_BOM))
    implementation(Dependencies.OkHttp3.OKHTTP3)
    implementation(Dependencies.OkHttp3.OKHTTP3_INTERCEPTOR)

    // Retrofit2
    implementation(Dependencies.Retrofit.RETROFIT)
    implementation(Dependencies.Retrofit.GSON_CONVERTER)

    // Lottie
    implementation(Dependencies.Lottie.LOTTIE)

    // Etc
    implementation(Dependencies.Gson.GSON)
    implementation(Dependencies.Etc.TEDPERMISSION)
    implementation(Dependencies.Etc.EASYPREFS)
    implementation(Dependencies.Etc.TIMBER)
    implementation(Dependencies.Etc.JAVA_JWT)

    // Test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.TEST_EXT_JUNIT)
    androidTestImplementation(Dependencies.Test.ESPRESSO_CORE)
    testImplementation(Dependencies.Test.COMPOSE_UI_TEST)
    debugImplementation(Dependencies.Test.COMPOSE_UI_TOOLING)
    debugImplementation(Dependencies.Test.COMPOSE_UI_TEST_MANIFEST)
}