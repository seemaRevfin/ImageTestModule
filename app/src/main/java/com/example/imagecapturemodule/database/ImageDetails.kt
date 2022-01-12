package com.example.imagecapturemodule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob


/*@Entity(tableName = "image_details_table")
data class ImageDetails(
    @PrimaryKey @ColumnInfo(name = "id")
    var userId: String,
    var question: String,
    var answer_marked: String,
    var image: String
)*/

@Entity(tableName = "image_details_table")
data class ImageDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "question_id")
    var question: String = "",
    var userId: String = "101",
    var answer_marked: String = "",
    var image: String = "",
)