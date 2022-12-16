package com.xome.meetingminutes.data

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.xome.meetingminutes.App
import com.xome.meetingminutes.view.activities.MainActivity
import java.lang.Exception

class AuthRepository {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userData = MutableLiveData<FirebaseUser>()
    private val logOutStatus = MutableLiveData<Boolean>()
    val firebaseUser = firebaseAuth.currentUser
    private val saveNoteLiveData = MutableLiveData<Boolean>()

    private var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getSaveLiveData(): LiveData<Boolean> = saveNoteLiveData

    init {
        if (firebaseAuth.currentUser != null) {
            userData.postValue(firebaseAuth.currentUser)
            logOutStatus.postValue(false)
        }
    }

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    userData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        App.instance, "Registration failed: ${task.exception!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        userData.postValue(firebaseAuth.currentUser)
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
        return userData
    }

    fun getLoggedOutData(): MutableLiveData<Boolean> {
        return logOutStatus
    }

    fun logOut() {
        firebaseAuth.signOut()
        logOutStatus.postValue(true)
    }

    fun saveNote(title: String, date: String, desc: String){
        val documentReference = firebaseFirestore.collection("notes")
            .document(firebaseUser!!.uid).collection("myNotes").document()
        val note = mutableMapOf<String, Any>()
        note.put("title", title)
        note.put("date", date)
        note.put("content", desc)

        documentReference.set(note).addOnSuccessListener(object : OnSuccessListener<Void> {
            override fun onSuccess(p0: Void?) {
                Toast.makeText(
                    App.instance, "Notes created", Toast.LENGTH_SHORT
                ).show()
                saveNoteLiveData.postValue(true)
            }
        }).addOnFailureListener(object : OnFailureListener{
            override fun onFailure(p0: Exception) {
                Toast.makeText(
                    App.instance, "Failed to create note", Toast.LENGTH_SHORT
                ).show()
                saveNoteLiveData.postValue(false)
            }
        })
    }

}