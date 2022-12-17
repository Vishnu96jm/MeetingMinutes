package com.xome.meetingminutes.view.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xome.meetingminutes.App
import com.xome.meetingminutes.App.Companion.day
import com.xome.meetingminutes.App.Companion.month
import com.xome.meetingminutes.App.Companion.year
import com.xome.meetingminutes.R
import com.xome.meetingminutes.databinding.ActivityAddNoteBinding
import com.xome.meetingminutes.utils.toast
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_AUTHOR
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_CONTENT
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_DATE
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_ID
import com.xome.meetingminutes.view.activities.MainActivity.Companion.NOTE_TITLE
import com.xome.meetingminutes.view.activities.MainActivity.Companion.flag
import com.xome.meetingminutes.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
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
            val author = intent.getStringExtra(NOTE_AUTHOR)
            noteId = intent.getStringExtra(NOTE_ID).toString()
            isFromTouchFlag = intent.getIntExtra(flag, 0)

            binding.notesTitle.setText(title)
            binding.notesDate.setText(date)
            binding.notesDescription.setText(content)
            binding.notesAuthor.setText(author)

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

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)

                    val format = SimpleDateFormat("dd MMM yyy")
                    val strDate = format.format(calendar.time)

                    if (d1!!.before(d2) || d1.equals(d2)){
                        binding.notesDate.setText(strDate)
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
                    val author = binding.notesAuthor.text.toString()

                    if (title.isNotBlank() && date.isNotBlank() && desc.isNotBlank()
                        && author.isNotBlank()) {
                        notesViewModel.saveNote(title, date, desc, author)
                    } else {
                        toast("Field(s) cannot be empty")
                    }
                }else{
                    val title = binding.notesTitle.text.toString()
                    val date = binding.notesDate.text.toString()
                    val desc = binding.notesDescription.text.toString()
                    val author = binding.notesAuthor.text.toString()

                    if (title.isNotBlank() && date.isNotBlank() && desc.isNotBlank()
                        && author.isNotBlank()) {
                        notesViewModel.editNote(title, date, desc, author, noteId)
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