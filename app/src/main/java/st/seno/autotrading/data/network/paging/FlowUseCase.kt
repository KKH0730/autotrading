package st.seno.autotrading.data.network.paging

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import st.seno.autotrading.data.network.model.Result
import timber.log.Timber

abstract class FlowUseCase<P, R>(private val dispatcher: CoroutineDispatcher) {

    operator fun invoke(params: P): Flow<Result<R>> {
        return execute(params)
            .catch {
                Timber.e(it)
                emit(Result.Error(Exception(it)))
            }
            .flowOn(dispatcher)
    }

    abstract fun execute(params: P): Flow<Result<R>>
}