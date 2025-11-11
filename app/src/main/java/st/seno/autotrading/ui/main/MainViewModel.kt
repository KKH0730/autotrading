package st.seno.autotrading.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import st.seno.autotrading.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _tabIndex: MutableStateFlow<Int> = MutableStateFlow(savedStateHandle["tabIndex"] ?: 0)
    val tabIndex: StateFlow<Int> get() = _tabIndex.asStateFlow()

    fun updateTabIndex(index: Int) {
        _tabIndex.value = index
    }
}