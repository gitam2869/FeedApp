package com.fan.feedapp.data.userstorage.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.fan.feedapp.data.userstorage.model.ImageDTO
import com.fan.feedapp.data.userstorage.model.ImageItem
import com.fan.feedapp.domain.repository.ImageListRepository
import javax.inject.Inject

class ImageListRepositoryImpl @Inject constructor(private val contentResolver: ContentResolver) :
    ImageListRepository {

    override suspend fun getImageList(start: Int, end: Int): ImageDTO {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val sortOrder = MediaStore.MediaColumns.DATE_ADDED + " DESC"// + " LIMIT 1"
        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ? ", arrayOf("Camera"),
            sortOrder
        )

        val imageList = ImageDTO()
        cursor?.let {
            val count = it.count
            for (i in start until count) {
                if (i == end) break
                it.moveToPosition(i)
                val dataColumnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                imageList.add(
                    ImageItem(
                        "",
                        it.getString(dataColumnIndex)
                    )
                )
            }
        }

        cursor?.close()
        return imageList
    }

}