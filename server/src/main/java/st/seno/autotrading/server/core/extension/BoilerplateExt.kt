package st.seno.autotrading.server.core.extension

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withContext
import java.io.IOException
import st.seno.autotrading.server.core.data.network.model.Result

fun <T> Flow<Result<T>>.catchError(dispatcher: CoroutineDispatcher): Flow<Result<T>> =
    (this@catchError).catch {
        emit(Result.Error(Exception(it)))
    }.flowOn(dispatcher)

fun <T> Flow<Result<T>>.retry(tryCount: Int = 3, delay: Long = 1000): Flow<Result<T>> {
    return this.retryWhen { cause, attempt ->
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
        Result.Error(e)
    }
}

inline fun <T> safeCall(block: () -> T): Result<T> {
    return runCatching { block() }
        .fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )
}