package com.xome.meetingminutes.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xome.meetingminutes.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}