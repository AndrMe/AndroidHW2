@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.hw2.ui.gallery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.hw2.ui.theme.HW2Theme
import com.example.hw2.ui.gallery.GalleryViewModel



@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val viewModel: GalleryViewModel = viewModel()
        Greeting(viewModel.UiState, modifier)
    }
}
