

object Versions {
    const val junit = "4.13.2"
    const val androidx_junit = "1.1.3"
    const val espresso_core = "3.4.0"
    const val core_ktx = "1.7.0"
    const val appcompat = "1.5.1"
    const val material = "1.6.1"
    const val constraint_layout = "2.1.4"
    const val navigation = "2.5.2"
    const val retrofit = "2.9.0"
    const val logging_interceptor = "4.9.1"
    const val coroutines = "1.6.4"
    const val dagger_hilt = "2.42"
    const val glide = "4.14.1"
    const val room = "2.3.0"
}

object Deps {

    //Testing
    const val junit = "junit:junit:${Versions.junit}"
    const val androidx_junit = "androidx.test.ext:junit:${Versions.androidx_junit}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"

    //AndroidX
    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    //Design
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

    //Navigation Component - Kotlin
    const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    //Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.logging_interceptor}"

    //Coroutines
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    //Dagger-Hilt
    const val dagger_hilt = "com.google.dagger:hilt-android:${Versions.dagger_hilt}"
    const val dagger_hilt_kapt = "com.google.dagger:hilt-compiler:${Versions.dagger_hilt}"

    //Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    //Room
    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"
}