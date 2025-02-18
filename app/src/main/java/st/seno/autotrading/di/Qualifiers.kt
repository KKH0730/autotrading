package st.seno.autotrading.di

import javax.inject.Qualifier

object Qualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrlRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IOCoroutineScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ApplicationScope
}