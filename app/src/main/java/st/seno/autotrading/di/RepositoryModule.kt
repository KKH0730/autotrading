package st.seno.autotrading.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import st.seno.autotrading.data.network.repository.MarketImpl
import st.seno.autotrading.data.network.repository.MarketRepository
import st.seno.autotrading.data.network.repository.MyAssetsImpl
import st.seno.autotrading.data.network.repository.MyAssetsRepository
import st.seno.autotrading.data.network.repository.OrderImpl
import st.seno.autotrading.data.network.repository.OrderRepository

/**
 * 구현체 형태의 repository 객체들을 viewModel scope에 interface 형태로 bind 해준다.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMyAssetsRepository(myAssetsImpl: MyAssetsImpl): MyAssetsRepository

    @Binds
    abstract fun bindMarketRepository(marketImpl: MarketImpl): MarketRepository

    @Binds
    abstract fun bindOrderRepository(orderImpl: OrderImpl): OrderRepository
}