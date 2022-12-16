package com.xome.meetingminutes.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.xome.meetingminutes.databinding.ItemNotesBinding
import NotesModel

class NotesListAdapter(options: FirestoreRecyclerOptions<NotesModel>):
    FirestoreRecyclerAdapter<NotesModel, NotesListAdapter.NotesViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int, model: NotesModel) {
        holder.bindData(model)
    }

    inner class NotesViewHolder(binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root){
            private val title: TextView = binding.notesTitle
            private val date : TextView = binding.notesDate
            private val desc : TextView = binding.notesDescription
            fun bindData(model: NotesModel){
              //  view.setOnClickListener { listener(news) }
                title.text = model.title
                date.text = model.date
                desc.text = model.content
            }
        }

    }
