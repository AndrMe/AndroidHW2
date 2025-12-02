package com.example.hw2.ui.gallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel



class GalleryViewModel: ViewModel(){
    var UiState: String by mutableStateOf("")
        private  set

    init {
        getText()
    }

    fun getText(){
        UiState = "Hello Text"
    }

}