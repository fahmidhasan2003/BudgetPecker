package com.fahmicode.data.dao

import androidx.room.*
import com.fahmicode.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY dateMillis DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}
