package com.xome.meetingminutes.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.xome.meetingminutes.R
import com.xome.meetingminutes.databinding.ActivityMainBinding
import com.xome.meetingminutes.viewmodel.LogInViewModel
import NotesModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xome.meetingminutes.databinding.ItemNotesBinding
import com.xome.meetingminutes.utils.createAndShowDialog
import com.xome.meetingminutes.viewmodel.NotesViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var logInViewModel: LogInViewModel
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesQuery: Query

    private lateinit var binding: ActivityMainBinding
    private lateinit var options : FirestoreRecyclerOptions<NotesModel>
    private lateinit var adapter : FirestoreRecyclerAdapter<NotesModel, MainActivity.NotesViewHolder>

    companion object {
        const val NOTE_TITLE = "NOTE TITLE"
        const val NOTE_DATE = "NOTE DATE"
        const val NOTE_CONTENT = "NOTE CONTENT"
        const val NOTE_ID = "NOTE ID"
        const val flag = "FLAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener{
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        logInViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        notesQuery = logInViewModel.fetchNotesQuery()


        binding.notesItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)

//        binding.notesItems.layoutManager = GridLayoutManager(this,  2)


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
                val noteId: String = adapter.snapshots.getSnapshot(position).id
                holder.bindData(model, noteId)
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
             //   showFilterAndSortingDialog()
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
        private val view = binding.root

        fun bindData(note: NotesModel, id: String){
            view.setOnClickListener { openNote(note, id) }
            view.setOnLongClickListener {
                onItemLongTap(note, id)
                true
            }
            title.text = note.title
            date.text = note.date
            desc.text = note.content
        }
    }

    private fun onItemLongTap(note: NotesModel, id: String) {
        createAndShowDialog(this,
            getString(R.string.delete_title),
            getString(R.string.delete_message, note.title),
            onPositiveAction = {
                notesViewModel.deleteNote(id)
            }
        )
    }

    private fun openNote(note: NotesModel, id: String) {
        val title = note.title
        val date = note.date
        val content = note.content

        val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
        intent.putExtra(NOTE_TITLE, title)
        intent.putExtra(NOTE_DATE, date)
        intent.putExtra(NOTE_CONTENT, content)
        intent.putExtra(NOTE_ID, id)
        intent.putExtra(flag, 1)
        startActivity(intent)
    }
}