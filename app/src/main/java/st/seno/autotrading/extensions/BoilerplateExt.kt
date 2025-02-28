package st.seno.autotrading.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withContext
import st.seno.autotrading.data.network.model.Result
import timber.log.Timber
import java.io.IOException

fun <T> Flow<Result<T>>.catchError(dispatcher: CoroutineDispatcher): Flow<Result<T>> =
    (this@catchError).catch {
        Timber.e(it)
        emit(Result.Error(Exception(it)))
    }.flowOn(dispatcher)

fun <T> Flow<Result<T>>.retry(tryCount: Int = 3, delay: Long = 1000): Flow<Result<T>> {
    return this.retryWhen { cause, attempt ->
        Timber.e("cause : $cause, attempt : $attempt")
        if (attempt < tryCount && cause is IOException) { // 3번까지만 재시도
            delay(delay) // 1초 대기 후 재시도
            true
        } else {
            false
        }
    }
}

suspend fun <T> T.catchError(dispatcher: CoroutineDispatcher): Result<T> {
    return try {
        withContext(dispatcher) {
            Result.Success(this@catchError)
        }
    } catch (e: Exception) {
        Timber.e(e)
        Result.Error(e)
    }
}