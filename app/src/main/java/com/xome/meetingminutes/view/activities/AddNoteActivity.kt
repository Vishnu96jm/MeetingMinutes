package com.xome.meetingminutes.view.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ContentInfoCompat
import androidx.lifecycle.ViewModelProvider
import com.xome.meetingminutes.databinding.ActivityAddNoteBinding
import com.xome.meetingminutes.R
import com.xome.meetingminutes.utils.toast
import com.xome.meetingminutes.viewmodel.NotesViewModel
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.xome.meetingminutes.App
import com.xome.meetingminutes.App.Companion.day
import com.xome.meetingminutes.App.Companion.month
import com.xome.meetingminutes.App.Companion.year
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_CONTENT
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_DATE
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_ID
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_TITLE
import java.text.SimpleDateFormat
import com.google.firebase.firestore.Query
import com.xome.meetingminutes.view.activities.MainActivity.Companion.flag
import java.util.*


class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesViewModel: NotesViewModel
    private var isFromTouchFlag = 0
    private var noteId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if( intent.extras != null)
        {
            val title = intent.getStringExtra(NOTE_TITLE)
            val date = intent.getStringExtra(NOTE_DATE)
            val content = intent.getStringExtra(NOTE_CONTENT)
            noteId = intent.getStringExtra(NOTE_ID).toString()
            isFromTouchFlag = intent.getIntExtra(flag, 0)

            binding.notesTitle.setText(title)
            binding.notesDate.setText(date)
            binding.notesDescription.setText(content)

            supportActionBar?.title = "Edit Note"

        }else{
            supportActionBar?.title = "Add Note"
        }

        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        notesViewModel.getSavedStatus().observe(this, Observer { saved ->
            saved?.let {
                if (saved) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        notesViewModel.getEditedStatus().observe(this, Observer { edited ->
            edited?.let {
                if (edited) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        binding.notesDate.setOnClickListener {
            val date = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val d1 = sdf.parse("$year-$month-$day")
                    val d2 = App.getDate()

                    if (d1!!.before(d2) || d1.equals(d2)){
                        binding.notesDate.setText("$day/${month+1}/$year")
                    }else{
                        toast("Future dates cannot be selected")
                    }

                },
                year,
                month,
                day
            )
            date.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_note, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_save -> {
                if(isFromTouchFlag==0){
                    val title = binding.notesTitle.text.toString()
                    val date = binding.notesDate.text.toString()
                    val desc = binding.notesDescription.text.toString()

                    if (title.isNotBlank() && date.isNotBlank() && desc.isNotBlank()) {
                        notesViewModel.saveNote(title, date, desc)
                    } else {
                        toast("Field(s) cannot be empty")
                    }
                }else{
                    val title = binding.notesTitle.text.toString()
                    val date = binding.notesDate.text.toString()
                    val desc = binding.notesDescription.text.toString()

                    if (title.isNotBlank() && date.isNotBlank() && desc.isNotBlank()) {
                        notesViewModel.editNote(title, date, desc, noteId)
                    } else {
                        toast("Field(s) cannot be empty")
                    }
                }

            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}