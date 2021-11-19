package com.example.android.noteapp.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import kotlin.coroutines.CoroutineContext

@Entity(tableName = "Notes")
class Notes: Serializable{


    @PrimaryKey(autoGenerate = true)
    var id: Int?=null
    @ColumnInfo(name = "title")
    var title: String?=null
    @ColumnInfo(name = "sub_title")
    var subTitle: String?=null
    @ColumnInfo(name = "note_date")
    var noteDate: String?=null
    @ColumnInfo(name = "note_data")
    var noteData: String?=null
    @ColumnInfo(name = "image_url")
    var imagePath: String?=null
    @ColumnInfo(name = "web_link")
    var webLink: String?=null
    @ColumnInfo(name = "color")
    var noteColor: String?=null

    override fun toString(): String {
        return "$title : $noteData"
    }
}