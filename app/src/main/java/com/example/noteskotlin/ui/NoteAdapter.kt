package com.example.noteskotlin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskotlin.R
import com.example.noteskotlin.databinding.NoteCardViewBinding
import com.example.noteskotlin.model.Note


class NoteAdapter(val listener: Listener) : RecyclerView.Adapter<NoteAdapter.NotesHolder>() {
    private var noteList = emptyList<Note>()


    class NotesHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = NoteCardViewBinding.bind(item)

        fun bind(notes: Note, listener: Listener) = with(binding) {
            noteTittleCard.text = notes.tittle
            noteDescriptionCard.text = notes.desc

            cardView.setOnClickListener {
                listener.onClick(notes)
            }
            cardView.setOnLongClickListener(View.OnLongClickListener {
                listener.onLongClick(cardView,notes)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_card_view, parent, false)
        return NotesHolder(view)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.bind(noteList[position], listener)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    interface Listener {
        fun onClick(notes: Note)
        fun onLongClick(view: View ,notes: Note):Boolean
    }
    fun refresh(){
        notifyDataSetChanged()
    }
    fun submitList(notesList:List<Note>){
        noteList = notesList
        refresh()
    }
}