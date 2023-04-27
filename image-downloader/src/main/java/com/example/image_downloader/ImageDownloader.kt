package com.example.image_downloader

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import coil.ImageLoader
import coil.request.ImageRequest

class ImageDownloader {
    companion object {
        suspend fun download(context: Context, url: String) {
            val loader = ImageLoader(context)
            val req = ImageRequest.Builder(context)
                .data(url)
                .target { result ->
                    val bitmap = (result as BitmapDrawable).bitmap
                    saveImageToGallery(context, bitmap = bitmap, title = "test")
                }
                .build()
            loader.execute(req)
        }

        private fun saveImageToGallery(context: Context, bitmap: Bitmap, title: String) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val resolver: ContentResolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            try {
                uri?.let {
                    resolver.openOutputStream(uri).use { out ->
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                            throw Exception("Failed to save bitmap.")
                        }
                        Toast.makeText(context, "이미지가 다운로드 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "이미지 다운로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}