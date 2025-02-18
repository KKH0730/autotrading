package st.seno.autotrading.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import st.seno.autotrading.data.network.service.CandleService
import st.seno.autotrading.data.network.service.MarketService
import st.seno.autotrading.data.network.service.MyAssetsService
import st.seno.autotrading.data.network.service.OrderService

/**
 * REST API serivce 객체들을 singleton scope 에 injection 시켜준다.
 */
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    /**
     * BaseUrlRetrofit
     */
    @Provides
    fun provideMyAssetsService(@Qualifiers.BaseUrlRetrofit retrofit: Retrofit): MyAssetsService =
        retrofit.create(MyAssetsService::class.java)

    @Provides
    fun provideMarketService(@Qualifiers.BaseUrlRetrofit retrofit: Retrofit): MarketService =
        retrofit.create(MarketService::class.java)

    @Provides
    fun provideOrderService(@Qualifiers.BaseUrlRetrofit retrofit: Retrofit): OrderService =
        retrofit.create(OrderService::class.java)

    @Provides
    fun provideCandleService(@Qualifiers.BaseUrlRetrofit retrofit: Retrofit): CandleService =
        retrofit.create(CandleService::class.java)
}