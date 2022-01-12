package com.example.imagecapturemodule

import android.app.Application
import com.example.imagecapturemodule.database.ImageCaptureRoomDatabase
import com.example.imagecapturemodule.database.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ImageDetailsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ImageCaptureRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ImageRepository(database.imageDetailsDao()) }
}
