package com.xome.meetingminutes.view.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.xome.meetingminutes.databinding.ActivityAddNoteBinding
import com.xome.meetingminutes.R
import com.xome.meetingminutes.utils.toast
import com.xome.meetingminutes.viewmodel.NotesViewModel
import androidx.lifecycle.Observer
import com.xome.meetingminutes.App
import com.xome.meetingminutes.App.Companion.day
import com.xome.meetingminutes.App.Companion.month
import com.xome.meetingminutes.App.Companion.year
import java.text.SimpleDateFormat
import java.util.*


class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesViewModel: NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.notesTitle.text
//        binding.notesDescription.text

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Note"

        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        notesViewModel.getSavedStatus().observe(this, Observer { saved ->
            saved?.let {
                if (saved) {
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
                        binding.notesDate.setText("$day/$month/$year")
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
                val title = binding.notesTitle.text.toString()
                val date = binding.notesDate.text.toString()
                val desc = binding.notesDescription.text.toString()

                if (title.isNotBlank() && date.isNotBlank() && desc.isNotBlank()) {
                    notesViewModel.saveNote(title, date, desc)
                } else {
                    toast("Fields cannot be empty")
                }
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}