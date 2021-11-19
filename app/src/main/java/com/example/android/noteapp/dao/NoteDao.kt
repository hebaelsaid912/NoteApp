package com.example.android.noteapp.dao

import androidx.room.*
import com.example.android.noteapp.entities.Notes
import java.util.ArrayList

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER By id DESC")
    suspend fun getAllNotes():List<Notes>
    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun getClickedNote(id:Int):Notes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewNote(note: Notes)
    @Delete
    suspend fun deleteNote(note: Notes)
    @Update
    suspend fun updateNote(note: Notes)
    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)
}