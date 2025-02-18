package st.seno.autotrading.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import st.seno.autotrading.data.network.repository.CandleImpl
import st.seno.autotrading.data.network.repository.CandleRepository
import st.seno.autotrading.data.network.repository.MarketImpl
import st.seno.autotrading.data.network.repository.MarketRepository
import st.seno.autotrading.data.network.repository.MyAssetsImpl
import st.seno.autotrading.data.network.repository.MyAssetsRepository
import st.seno.autotrading.data.network.repository.OrderImpl
import st.seno.autotrading.data.network.repository.OrderRepository
import st.seno.autotrading.data.network.repository.TradingDataImpl
import st.seno.autotrading.data.network.repository.TradingDataRepository

/**
 * 구현체 형태의 repository 객체들을 viewModel scope에 interface 형태로 bind 해준다.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMarketRepository(marketImpl: MarketImpl): MarketRepository

    @Binds
    abstract fun bindTradingDataRepository(tradingDataImpl: TradingDataImpl): TradingDataRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonRepositoryModule {

    @Binds
    abstract fun bindMyAssetsRepository(myAssetsImpl: MyAssetsImpl): MyAssetsRepository

    @Binds
    abstract fun bindOrderRepository(orderImpl: OrderImpl): OrderRepository

    @Binds
    abstract fun bindCandleRepository(candleImpl: CandleImpl) : CandleRepository
}