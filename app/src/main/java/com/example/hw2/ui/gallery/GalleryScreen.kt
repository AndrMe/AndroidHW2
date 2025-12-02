package com.example.hw2.ui.gallery

import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.hw2.R
import com.example.hw2.data.GiphyImage
import com.example.hw2.ui.theme.HW2Theme


@Composable
fun DisplayGallery(state: GalleryUiState,
                   modifier: Modifier = Modifier,
                   viewModel: GalleryViewModel
) {
    when (state){
        is GalleryUiState.Success ->
           GalleryScreen(state)
        is GalleryUiState.Error ->
            ErrorScreen(modifier,viewModel)
        is GalleryUiState.Loading ->
            LoadingScreen()
    }

}

@Composable
fun GalleryScreen(state: GalleryUiState.Success, modifier: Modifier = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(state.photos){
            photo ->
            MyCell(photo)
        }
    }
}

@Composable
fun MyCell(
    item: GiphyImage
)
{
    AsyncImage(
        model = item.images.downsized.url,
        contentDescription = item.title,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
    )
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier ){
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
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_error), contentDescription = ""
        )
        Text(text = "Loading Failed", modifier = Modifier.padding(16.dp))
        Button(
            modifier = Modifier.size(256.dp, 64.dp),
            onClick = {
                viewModel.getPhotos()
            }
        ) {
            Text(text = "Retry?", modifier = Modifier.padding(2.dp), textAlign = TextAlign.Center)
        }

    }
}
