package com.xome.meetingminutes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.xome.meetingminutes.data.AuthRepository

class NotesViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel()  {

    private var saveNoteLiveData = repository.getSaveLiveData()
    private var editNoteLiveData = repository.getEditLiveData()

    fun getSavedStatus(): LiveData<Boolean> {
        return saveNoteLiveData
    }

    fun getEditedStatus(): LiveData<Boolean> {
        return editNoteLiveData
    }

    fun saveNote(title: String, date: String, desc: String, author: String) {
        repository.saveNote(title, date, desc, author)
    }

    fun editNote(title: String, date: String, desc: String,author: String, noteId: String){
        repository.editNote(title, date, desc, author, noteId)
    }

    fun deleteNote(noteId: String) {
        repository.deleteNote(noteId)
    }

}