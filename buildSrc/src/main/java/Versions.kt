object Versions {
    private const val VERSION_MAJOR = 1
    private const val VERSION_MINOR = 0
    private const val VERSION_PATCH = 0
    const val VERSION_CODE = VERSION_MAJOR * 10000 + VERSION_MINOR * 100 + VERSION_PATCH
    const val VERSION_NAME = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"

    const val NAMESPACE = "st.seno.autotrading"
    const val APPLICATION_Id = "st.seno.autotrading"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MIN_SDK = 30

}