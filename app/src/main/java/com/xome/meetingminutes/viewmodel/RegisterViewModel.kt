package com.xome.meetingminutes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.xome.meetingminutes.data.AuthRepository

class RegisterViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel()  {

    private var userData = MutableLiveData<FirebaseUser>()

    init {
        userData = repository.getUser()
    }

    fun getUser(): MutableLiveData<FirebaseUser> {
        return userData
    }

    fun register(email: String, password: String) {
        repository.register(email, password)
    }

}