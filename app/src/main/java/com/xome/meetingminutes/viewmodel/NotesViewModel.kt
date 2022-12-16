package com.xome.meetingminutes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.xome.meetingminutes.data.AuthRepository

class NotesViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel()  {

    private var saveNoteLiveData = repository.getSaveLiveData()

    fun getSavedStatus(): LiveData<Boolean> {
        return saveNoteLiveData
    }

    fun saveNote(title: String, date: String, desc: String) {
        repository.saveNote(title, date, desc)
    }

}