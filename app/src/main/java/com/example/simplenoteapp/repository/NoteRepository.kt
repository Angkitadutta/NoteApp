package com.example.simplenoteapp.repository

import androidx.lifecycle.LiveData
import com.example.simplenoteapp.data.model.Note
import com.example.simplenoteapp.data.local.NotesDao

class NoteRepository(private val notesDao: NotesDao) {

    val allNotes: LiveData<List<Note>> = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }


}