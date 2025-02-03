package st.seno.autotrading.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message get() = _message.asSharedFlow()



    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        hideLoading()
        Timber.e("$javaClass exceptionHandler -> ${throwable.message}")
    }

    open fun showLoading() {
        _isLoading.value = true
    }

    open fun hideLoading() {
        _isLoading.value = false
    }

    open fun showMessage(message: String) {
        vmScopeJob { _message.emit(message) }
    }

    fun vmScopeJob(
        coroutineExceptionHandler: CoroutineExceptionHandler = exceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = SupervisorJob() + coroutineExceptionHandler, block = block)
}