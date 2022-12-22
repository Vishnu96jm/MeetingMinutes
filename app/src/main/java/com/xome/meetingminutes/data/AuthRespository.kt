package com.xome.meetingminutes.data

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xome.meetingminutes.App

class AuthRepository {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userData = MutableLiveData<FirebaseUser>()
    private val logOutStatus = MutableLiveData<Boolean>()
    private val firebaseUser = firebaseAuth.currentUser

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val saveNoteLiveData = MutableLiveData<Boolean>()
    private val editNoteLiveData = MutableLiveData<Boolean>()


    fun getSaveLiveData(): LiveData<Boolean> = saveNoteLiveData

    fun getEditLiveData(): LiveData<Boolean> = editNoteLiveData

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

    fun saveNote(title: String, date: String, desc: String, author: String){
        val note = hashMapOf(
            "title" to title,
            "date" to date,
            "content" to desc,
            "author" to author
            )

        db.collection("notes")
            .add(note)
            .addOnSuccessListener(object : OnSuccessListener<DocumentReference>{
                override fun onSuccess(p0: DocumentReference?) {
                    Toast.makeText(
                        App.instance, "Notes created", Toast.LENGTH_SHORT
                    ).show()
                    saveNoteLiveData.postValue(true)
                }
            })
            .addOnFailureListener { e -> Toast.makeText(
                App.instance, "Failed to create note", Toast.LENGTH_SHORT
            ).show()
                saveNoteLiveData.postValue(false) }

    }

    fun fetchNotesQuery(): Query {
        return db.collection("notes")
    }

    fun editNote(title: String, date: String, desc: String, author: String, noteId: String){
        val documentReference = db.collection("notes").document(noteId)
        val note = hashMapOf(
            "title" to title,
            "date" to date,
            "content" to desc,
            "author" to author
        )
        documentReference.set(note).addOnSuccessListener {
            Toast.makeText(
                App.instance, "Note edited", Toast.LENGTH_SHORT
            ).show()
            editNoteLiveData.postValue(true)

        }.addOnFailureListener {
            Toast.makeText(
                App.instance, "Edit failed", Toast.LENGTH_SHORT
            ).show()
            editNoteLiveData.postValue(false)

        }
    }

    fun deleteNote(noteId: String){
        val documentReference = db.collection("notes").document(noteId)
        documentReference.delete().addOnSuccessListener {
            Toast.makeText(
                App.instance, "Note deleted", Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                App.instance, "Delete failed", Toast.LENGTH_SHORT
            ).show()
        }

    }

}