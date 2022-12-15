package com.xome.meetingminutes.data

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.xome.meetingminutes.App

class AuthRepository {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = MutableLiveData<FirebaseUser>()
    private val logOutStatus = MutableLiveData<Boolean>()

   // private var firebaseFirestore: FirebaseFirestore = FirebaseAuth.getInstance()


    init {
        if (firebaseAuth.currentUser != null) {
            user.postValue(firebaseAuth.currentUser)
            logOutStatus.postValue(false)
        }
    }

    fun register(email: String?, password: String?) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    user.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        App.instance, "Registration failed: ${task.exception!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    fun login(email: String?, password: String?) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        user.postValue(firebaseAuth.currentUser)
                    } else {
                        Toast.makeText(
                            App.instance, "Login Failed: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        firebaseAuth.signOut()
                    }
                })
    }

    fun getUser(): MutableLiveData<FirebaseUser> {
        return user
    }

    fun getLoggedOutData(): MutableLiveData<Boolean> {
        return logOutStatus
    }

    fun logOut() {
        firebaseAuth.signOut()
        logOutStatus.postValue(true)
    }

}