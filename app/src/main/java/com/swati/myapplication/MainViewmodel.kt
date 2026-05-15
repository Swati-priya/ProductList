package com.swati.myapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swati.myapplication.model.ListData
import com.swati.myapplication.repository.DemoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val listRepository: DemoListRepository
): ViewModel() {
    private val _uiState: MutableState<UiSealedState> = mutableStateOf(UiSealedState.Loading)
    val uiState = _uiState

    private val allItems = mutableListOf<ListData>()
    private var currentSkip = 0
    private var isLoadingMore = false
    private var total = Int.MAX_VALUE
    private val pageSize = 20

    fun getList() {
        if (isLoadingMore || currentSkip >= total) return
        isLoadingMore = true

        viewModelScope.launch {
            if (allItems.isEmpty()) {
                _uiState.value = UiSealedState.Loading
            }
            val response = listRepository.getListData(limit = pageSize, skip = currentSkip)
            if (!response.isSuccessful || response.body() == null) {
                _uiState.value = UiSealedState.Error("Something went wrong")
                isLoadingMore = false
                return@launch
            }
            val body = response.body()!!
            total = body.total ?: 0
            val list = body.products
            list?.let { dataList ->
                if (dataList.isEmpty() && allItems.isEmpty()) {
                    _uiState.value = UiSealedState.Error("No products found")
                } else {
                    allItems.addAll(dataList)
                    currentSkip = (body.skip ?: 0) + (body.limit ?: pageSize)
                    _uiState.value = UiSealedState.Success(allItems.toList())
                }
            } ?: run {
                _uiState.value = UiSealedState.Error("Something went wrong")
            }
            isLoadingMore = false
        }
    }

    sealed class UiSealedState {
        object Loading : UiSealedState()
        data class Error(val msg: String) : UiSealedState()
        data class Success(val dataList: List<ListData>) : UiSealedState()
    }
}