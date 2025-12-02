package com.example.hw2.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.hw2.R
import com.example.hw2.data.GiphyImage
import com.example.hw2.ui.theme.HW2Theme
import kotlinx.coroutines.flow.collect

@Composable
fun DisplayGallery(
    state: GalleryUiState,
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel
) {
    when (state) {
        is GalleryUiState.Success ->
            GalleryScreen(state,  modifier, viewModel)

        is GalleryUiState.Error ->
            ErrorScreen(modifier, viewModel)

        is GalleryUiState.Loading ->
            LoadingScreen( modifier)
    }
}

@Composable
fun GalleryScreen(
    state: GalleryUiState.Success,
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel
) {
    val gridState = rememberLazyGridState()

    var isLoadingTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(gridState, state.photos.size, state.isLoadingMore) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        }.collect { lastVisible ->
            if (lastVisible >= state.photos.size - 1 && !state.isLoadingMore && !isLoadingTriggered) {
                isLoadingTriggered = true
                viewModel.loadAdditional()
            }
            if (!state.isLoadingMore) {
                isLoadingTriggered = false
            }
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        items(items = state.photos, key = { it.id }) { photo ->
            MyCell(item  = photo)
        }

        if (state.isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun MyCell(
    item: GiphyImage
) {
    Card {
        AsyncImage(
            model = item.images.downsized.url,
            contentDescription = item.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, viewModel: GalleryViewModel) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = "Error"
        )
        Text(text = "Loading Failed", modifier = Modifier.padding(16.dp))
        Button(
            modifier = Modifier.size(256.dp, 64.dp),
            onClick = {
                viewModel.retry()
            }
        ) {
            Text(text = "Retry?", modifier = Modifier.padding(2.dp), textAlign = TextAlign.Center)
        }
    }
}
