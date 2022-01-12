package com.example.imagecapturemodule.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ImageRepository(private val imageDetailsDao: ImageDetailsDao) {

    val allimageDetailss: Flow<List<ImageDetails>> = imageDetailsDao.getAlphabetizedWords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(imageDetails: ImageDetails) {
        imageDetailsDao.insert(imageDetails)
    }
}
