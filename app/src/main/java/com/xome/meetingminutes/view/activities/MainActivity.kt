package com.xome.meetingminutes.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.xome.meetingminutes.R
import com.xome.meetingminutes.databinding.ActivityMainBinding
import com.xome.meetingminutes.viewmodel.LogInViewModel

class MainActivity : AppCompatActivity() {

  //  private lateinit var viewModel: MainViewModel
    private lateinit var logInViewModel: LogInViewModel

    private lateinit var binding: ActivityMainBinding
  //  private val adapter = NotesListAdapter(mutableListOf()) { note -> deleteNote(note) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

     //   binding.notesItems.adapter = adapter

        binding.fab.setOnClickListener{
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        logInViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id : Int = item.itemId
        when(id){
            R.id.action_sort -> {
              //  showFilterAndSortingDialog()
            }
            R.id.action_logout -> {
                logInViewModel.logOut()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }
        return  super.onOptionsItemSelected(item)
    }
}