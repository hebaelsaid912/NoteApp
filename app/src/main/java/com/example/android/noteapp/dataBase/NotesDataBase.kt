package com.example.android.noteapp.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.noteapp.dao.NoteDao
import com.example.android.noteapp.entities.Notes

@Database(entities = [Notes::class] , version = 1 , exportSchema = false )
abstract class NotesDataBase : RoomDatabase(){
    companion object{
         var notesDataBase:NotesDataBase?=null
        @Synchronized
        fun getDataBase(context: Context ): NotesDataBase{
            if(notesDataBase == null){
                notesDataBase = Room.databaseBuilder(
                    context,
                NotesDataBase::class.java,
                "notes.db"
                ).build()
            }
            return notesDataBase!!
        }
    }
    abstract fun noteDeo():NoteDao
}