package com.example.hw2.ui.gallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2.BuildConfig
import com.example.hw2.network.GalleryApi
import kotlinx.coroutines.launch



class GalleryViewModel: ViewModel(){
    var UiState: String by mutableStateOf("")
        private  set

    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch {
            try {
                val listResults = GalleryApi.galleryService.getTrendingPhotos(
                    BuildConfig.API_KEY,
                    10,
                    5
                )
                UiState = listResults
            }catch (e: Exception)
            {
                UiState = e.message ?: "Error"
            }
        }
    }

}