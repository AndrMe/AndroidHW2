package com.example.hw2.data

import androidx.compose.ui.graphics.TransformOrigin
import com.google.gson.annotations.SerializedName

data class ResponseData(
    val data: List<GiphyImage>
)

data class GiphyImage(
    val id: String,
    val title: String,
    val images: Images
)
data class Images(
    @SerializedName("downsized")
    val downsized: ImageDetails,
)

data class ImageDetails(
    val url: String,
    val width: String,
    val height: String,
    val duration: String? = null
)