package com.example.hw2.ui.gallery

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hw2.BuildConfig
import com.example.hw2.data.DiskCache
import com.example.hw2.data.GiphyImage
import com.example.hw2.data.ResponseData
import com.example.hw2.network.GalleryApi
import kotlinx.coroutines.launch


sealed interface GalleryUiState {
    object Loading : GalleryUiState

    data class Success(
        val photos: List<GiphyImage>,
        val isLoadingMore: Boolean = false,
        val loadingFailed: Boolean = false
    ) : GalleryUiState

    data class Error(val message: String) : GalleryUiState
}
class GalleryViewModel(private val appContext: Context) : ViewModel() {

    var UiState: GalleryUiState by mutableStateOf(GalleryUiState.Loading)
        private set

    init {
        loadInitial()
    }

    fun retry() {
        UiState = GalleryUiState.Loading
        loadInitial()
    }

    fun loadInitial() {
        viewModelScope.launch {
            val cached = DiskCache.loadResponse(appContext)
            if (cached != null) {
                UiState = GalleryUiState.Success(
                    photos = cached.data,
                    isLoadingMore = false
                )
            } else {
                UiState = GalleryUiState.Loading
            }

            try {
                val response = GalleryApi.galleryService.getTrendingPhotos(
                    BuildConfig.API_KEY,
                    20,
                    0
                )

                UiState = GalleryUiState.Success(
                    response.data,
                    isLoadingMore = false
                )

                DiskCache.saveResponse(appContext, response)

            } catch (e: Exception) {
                if (cached == null) {
                    UiState = GalleryUiState.Error(e.message ?: "Error")
                }
            }
        }
    }

    fun loadAdditional() {
        val state = UiState
        if (state !is GalleryUiState.Success) return
        if (state.isLoadingMore) return

        UiState = state.copy(isLoadingMore = true)

        viewModelScope.launch {
            try {
                val response = GalleryApi.galleryService.getTrendingPhotos(
                    BuildConfig.API_KEY,
                    limit = 20,
                    offset = state.photos.size
                )

                val newPhotos = state.photos + response.data

                UiState = state.copy(
                    photos = newPhotos,
                    isLoadingMore = false,
                    loadingFailed = false
                )

                DiskCache.saveResponse(appContext, ResponseData(newPhotos))

            } catch (e: Exception) {
                UiState = state.copy(isLoadingMore = false, loadingFailed = true)
            }
        }
    }

    companion object {
        fun Factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GalleryViewModel(context.applicationContext) as T
            }
        }
    }
}
