package com.xome.meetingminutes.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xome.meetingminutes.R
import com.xome.meetingminutes.databinding.ActivityMainBinding
import com.xome.meetingminutes.view.adapters.NotesListAdapter
import com.xome.meetingminutes.viewmodel.LogInViewModel
import NotesModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xome.meetingminutes.databinding.ItemNotesBinding


class MainActivity : AppCompatActivity() {

    private lateinit var logInViewModel: LogInViewModel
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var notesQuery: Query

    private lateinit var binding: ActivityMainBinding
    private lateinit var options : FirestoreRecyclerOptions<NotesModel>
    private lateinit var adapter : FirestoreRecyclerAdapter<NotesModel, MainActivity.NotesViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener{
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        logInViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        firebaseUser = logInViewModel.getfirebaseUser()!!
        firebaseFirestore = logInViewModel.getFirebaseFirestore()
        notesQuery = logInViewModel.fetchNotesQuery()


   //     binding.notesItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)

        options = FirestoreRecyclerOptions.Builder<NotesModel>()
            .setQuery(notesQuery, NotesModel::class.java)
            .build()

        adapter = object :
            FirestoreRecyclerAdapter<NotesModel, MainActivity.NotesViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
                val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return NotesViewHolder(binding)
            }

            override fun onBindViewHolder(
                holder: NotesViewHolder,
                position: Int,
                model: NotesModel
            ) {
                holder.bindData(model)
            }
        }

        binding.notesItems.adapter = adapter


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

    override fun onStart() {
        super.onStart()
        adapter.startListening()
        binding.notesItems.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
        binding.notesItems.adapter = adapter
    }

    inner class NotesViewHolder(binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root){
        private val title: TextView = binding.notesTitle
        private val date : TextView = binding.notesDate
        private val desc : TextView = binding.notesDescription
        fun bindData(model: NotesModel){
            title.text = model.title
            date.text = model.date
            desc.text = model.content
        }
    }
}