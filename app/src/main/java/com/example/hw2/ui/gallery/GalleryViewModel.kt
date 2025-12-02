package com.example.hw2.ui.gallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2.BuildConfig
import com.example.hw2.network.GalleryApi
import kotlinx.coroutines.launch


sealed interface GalleryUiState{
    data class Success(val photos: String): GalleryUiState
    object Loading: GalleryUiState
    data class Error(val error: String): GalleryUiState
}

class GalleryViewModel: ViewModel(){
    var UiState: GalleryUiState by mutableStateOf(GalleryUiState.Loading)
        private  set

    init {
        getPhotos()
    }

     fun getPhotos() {
        viewModelScope.launch {
            try {
                val listResults = GalleryApi.galleryService.getTrendingPhotos(
                    BuildConfig.API_KEY,
                    10,
                    5
                )
                UiState = GalleryUiState.Success("Success: ${listResults.data.size} Images retrieved")
            }catch (e: Exception)
            {
                UiState = GalleryUiState.Error(e.message ?: "Error")
            }
        }
    }

}