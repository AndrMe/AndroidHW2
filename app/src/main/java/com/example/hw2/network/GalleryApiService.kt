package com.example.hw2.network
import com.example.hw2.data.ResponseData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.giphy.com/v1/gifs/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object GalleryApi {
    val galleryService by lazy{
        retrofit.create(GalleryApiService::class.java)
    }
}

interface GalleryApiService {
    @GET("trending")
    suspend fun getTrendingPhotos(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): ResponseData

}