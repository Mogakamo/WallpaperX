package com.dxn.wallpaperx.domain.usecases

import javax.inject.Inject

data class WallpaperUseCase
@Inject
constructor
    (
    val getWallpapers: GetWallpapers,
    val getWallpaper: GetWallpaper,
    val downloadWallpaper: DownloadWallpaper,
    val setWallpaperUseCases: SetWallpaperUseCases,
    val getFavourites: GetFavourites,
    val addFavourite: AddFavourite,
    val removeFavourite: RemoveFavourite
)