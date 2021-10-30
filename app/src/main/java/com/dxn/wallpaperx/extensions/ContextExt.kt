package com.dxn.wallpaperx.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

suspend fun Context.getBitmap(url: String): Bitmap {
    return withContext(Dispatchers.IO) {
        val loader = ImageLoader(this@getBitmap)
        val request = ImageRequest.Builder(this@getBitmap)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        (result as BitmapDrawable).bitmap
    }
}

suspend fun Context.downloadWallpaper(bitmap: Bitmap, displayName: String) {
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            var fos: OutputStream?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                this@downloadWallpaper.contentResolver.let { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, displayName)
                        put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg")
                        put(
                            MediaStore.Images.ImageColumns.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES+"/wallpaperx"
                        )
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { contentResolver.openOutputStream(it) }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/wallpaperx")
                val image = File(imagesDir, displayName)
                fos = FileOutputStream(image)
            }
            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
                it.close()
            }
        }.getOrThrow()
    }
}