package com.xome.meetingminutes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.xome.meetingminutes.data.AuthRepository

class LogInViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private var userData = MutableLiveData<FirebaseUser>()
    private var logOutData = MutableLiveData<Boolean>()

    init {
        userData = repository.getUser()
        logOutData = repository.getLoggedOutData()
    }

    fun login(email: String, password: String) {
        repository.login(email, password)
    }

    fun logOut() {
        repository.logOut()
    }

    fun getUserData(): MutableLiveData<FirebaseUser> {
        return userData
    }

    fun getLoggedOutData(): MutableLiveData<Boolean> {
        return logOutData
    }
}