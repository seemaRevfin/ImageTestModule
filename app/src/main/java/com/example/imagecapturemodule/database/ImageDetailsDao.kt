package com.example.imagecapturemodule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDetailsDao {

    @Query("SELECT * FROM image_details_table ORDER BY question_id ASC")
    fun getAlphabetizedWords(): Flow<List<ImageDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(imageDetails: ImageDetails)

    @Query("DELETE FROM image_details_table")
    suspend fun deleteAll()
}