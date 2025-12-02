@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.hw2.ui.gallery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hw2.R


@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier) {
    val viewModel: GalleryViewModel = viewModel()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarLoadedCount(viewModel.UiState)
        }
    ) { innerPadding ->
        DisplayGallery(
            state = viewModel.UiState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun TopBarLoadedCount(state: GalleryUiState) {
    val count = if (state is GalleryUiState.Success) state.photos.size else 0
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.TopBar, count)) }
    )
}