package com.dxn.wallpaperx.data.remote

import com.dxn.wallpaperx.data.remote.models.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api")
    suspend fun getWallpapers(
        @Query("key") apikey:String,
        @Query ("q") query : String,
        @Query("page") page :Int = 1,
        @Query("orientation") orientation : String = "vertical",
        @Query("safesearch") safe: Boolean = false,
        @Query("image_type") imageType : String = "photo"
    ) : Response

    @GET("api")
    suspend fun getWallpaper(
        @Query("key") apikey:String = "",
        @Query("id") imageId:Int,
    ) : Response
}