package st.seno.autotrading.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import st.seno.autotrading.data.network.DefaultParamsInterceptor
import java.util.concurrent.TimeUnit

/**
 * REST API 사용을 위한 Network 관련 객체들을 singleton scope에 injection 시켜준다
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val BASE_URL = "https://api.upbit.com/v1/"

    private val httpLoggingInterceptor: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun provideOkHttpClient(
        defaultParamsInterceptor: DefaultParamsInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(defaultParamsInterceptor)
            .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    @Qualifiers.BaseUrlRetrofit
    @Provides
    fun provideBaseUrlRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = createRetrofit(
        BASE_URL, gson, okHttpClient)

    private fun createRetrofit(url: String, gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
}