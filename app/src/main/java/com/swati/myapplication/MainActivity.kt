package com.swati.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.swati.myapplication.compose.ProductItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.getList()
            }
            ListScreen()
        }
    }

    @Composable
    fun ListScreen() {
        val uiState by viewModel.uiState
        when (uiState) {
            MainViewmodel.UiSealedState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is MainViewmodel.UiSealedState.Error -> {
                val error = uiState as MainViewmodel.UiSealedState.Error
                Box {
                    Text(text = error.msg)
                }
            }
            is MainViewmodel.UiSealedState.Success -> {
                val success = uiState as MainViewmodel.UiSealedState.Success
                val listState = rememberLazyListState()

                val shouldLoadMore = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.let {
                    it.index >= success.dataList.size - 5
                } ?: false

                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore) {
                        viewModel.getList()
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(success.dataList) { product ->
                        ProductItem(product)
                    }
                }
            }
        }
    }
}
