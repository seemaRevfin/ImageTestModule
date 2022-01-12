package com.example.imagecapturemodule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ImageDetails::class], version = 1)
abstract class ImageCaptureRoomDatabase : RoomDatabase() {
    abstract fun imageDetailsDao(): ImageDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: ImageCaptureRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ImageCaptureRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageCaptureRoomDatabase::class.java,
                    "image_database"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(imageDetailsDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class imageDetailsDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.imageDetailsDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(imageDetailsDao: ImageDetailsDao) {
            imageDetailsDao.deleteAll()
/*
            var imageDetails = ImageDetails("1","ques1","2","image1")
            imageDetailsDao.insert(imageDetails)
            imageDetails =  ImageDetails("2","ques2","4","image2")
            imageDetailsDao.insert(imageDetails)*/
        }
    }
}
