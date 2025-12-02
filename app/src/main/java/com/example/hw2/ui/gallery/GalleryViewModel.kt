package com.example.hw2.ui.gallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2.BuildConfig
import com.example.hw2.data.GiphyImage
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
class GalleryViewModel: ViewModel(){
    var UiState: GalleryUiState by mutableStateOf(GalleryUiState.Loading)
        private  set

    init {
        loadInitial()
    }

    fun retry(){
        UiState = GalleryUiState.Loading
        loadInitial()
    }
     fun loadInitial() {
        viewModelScope.launch {
            try {
                val listResults = GalleryApi.galleryService.getTrendingPhotos(
                    BuildConfig.API_KEY,
                    20,
                    0
                )

                UiState = GalleryUiState.Success(
                    listResults.data,
                    isLoadingMore = false
                )
            }catch (e: Exception)
            {
                UiState = GalleryUiState.Error(e.message ?: "Error")
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
                val data = GalleryApi.galleryService
                    .getTrendingPhotos(
                        BuildConfig.API_KEY,
                        limit = 20,
                        offset = state.photos.size
                    )

                UiState = state.copy(
                    photos = state.photos + data.data,
                    isLoadingMore = false,
                    loadingFailed = false
                )

            } catch (e: Exception) {
                UiState = state.copy(isLoadingMore = false)
                if (UiState is GalleryUiState.Success){
                    UiState = state.copy(loadingFailed = true, isLoadingMore = false)
                }

            }
        }
    }
}