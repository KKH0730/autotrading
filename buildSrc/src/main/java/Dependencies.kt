object Dependencies {

    object AndroidX {
        const val ANDROID_CORE = "androidx.core:core-ktx:1.13.1"
        const val ANDROID_LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:2.8.7"
        const val ANDROID_ACTIVITY_COMPOSE = "androidx.activity:activity-compose:1.9.3"
        const val COMPOSE_BOM = "androidx.compose:compose-bom:2024.01.00"
        const val COMPOSE_UI = "androidx.compose.ui:ui"
        const val COMPOSE_UI_GRAPHICS = "androidx.compose.ui:ui-graphics"
        const val COMPOSE_UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview"
        const val COMPOSE_MATERIAL3 = "androidx.compose.material3:material3"
        const val SPLASHSCREEN = "androidx.core:core-splashscreen:1.0.1"
        const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:2.8.4"
        const val HILT_NAVIGATION_COMPOSE = "androidx.hilt:hilt-navigation-compose:1.2.0"
        const val APPCOMPAT = "androidx.appcompat:appcompat:1.7.0"
        const val COMPOSE_MATERIAL = "androidx.compose.material:material:1.7.8"
        const val ANDROID_MATERIAL = "com.google.android.material:material:1.9.0"

        object Paging {
            const val PAGING_RUNTIME = "androidx.paging:paging-runtime:3.3.6"
            const val PAGING_COMPOSE = "androidx.paging:paging-compose:3.3.6"
        }
    }

    object Firebase {
        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:33.9.0"
        const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics"
        const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore"
    }

    object Hilt {
        const val ANDROID_HILT = "com.google.dagger:hilt-android:2.50"
        const val ANDROID_HILT_COMPILER = "com.google.dagger:hilt-android-compiler:2.50"
        const val ANDROID_HILT_TESTING = "com.google.dagger:hilt-android-testing:2.48"
    }

    object OkHttp3 {
        const val OKHTTP3_BOM = "com.squareup.okhttp3:okhttp-bom:4.12.0"
        const val OKHTTP3 = "com.squareup.okhttp3:okhttp"
        const val OKHTTP3_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor"
    }

    object Retrofit {
        private const val VERSION = "2.11.0"
        const val RETROFIT = "com.squareup.retrofit2:retrofit:$VERSION"
        const val GSON_CONVERTER = "com.squareup.retrofit2:converter-gson:$VERSION"
    }

    object Gson {
        const val GSON = "com.google.code.gson:gson:2.10.1"
    }

    object Lottie {
        const val LOTTIE = "com.airbnb.android:lottie-compose:6.6.2"
    }

    object Etc {
        const val TEDPERMISSION = "io.github.ParkSangGwon:tedpermission-normal:3.3.0"
        const val EASYPREFS = "com.pixplicity.easyprefs:EasyPrefs:1.10.0"
        const val TIMBER = "com.jakewharton.timber:timber:5.0.1"
        const val JAVA_JWT = "com.auth0:java-jwt:4.4.0"
    }

    object Test {
        const val JUNIT = "junit:junit:4.13.2"
        const val TEST_EXT_JUNIT = "androidx.test.ext:junit:1.2.1"
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.6.1"
        const val COMPOSE_UI_TEST = "androidx.compose.ui:ui-test-junit4"
        const val COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling"
        const val COMPOSE_UI_TEST_MANIFEST = "androidx.compose.ui:ui-test-manifest"
    }
}

object Plugins {
    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:7.2.2"
    const val HILT_AGP = "com.google.dagger:hilt-android-gradle-plugin:2.42"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0"
}