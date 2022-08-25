package com.example.noteskotlin.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.noteskotlin.R
import com.example.noteskotlin.databinding.FragmentNotes2Binding
import com.example.noteskotlin.model.Note
import com.example.noteskotlin.model.NoteViewModel
import org.koin.android.ext.android.inject

class NoteFragment2 : Fragment() {
    private val notesViewModel: NoteViewModel by inject()
    lateinit var binding: FragmentNotes2Binding
    private var selectNote= initSelectNote()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentNotes2Binding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        initToolbar(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.delete_note_toolbar) {
            deleteDialog(selectNote)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSelectNote()
        initText()
        binding.save.setOnClickListener {
            sendData()
            backToFragment1()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NoteFragment2()
    }

    private fun backToFragment1() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .remove(this)
            .replace(R.id.fragment_container, NoteFragment.newInstance())
            .commit()
    }

    private fun sendData() {
        selectNote.tittle = binding.noteTittleFrag2.text.toString()
        selectNote.desc = binding.noteDescriptionFrag2.text.toString()
        notesViewModel.selectedNoteIndex.observe(activity as LifecycleOwner) {
            selectNote.id = it
        }
        notesViewModel.editNote(selectNote)
    }

    private fun initSelectNote():Note {
        return notesViewModel.getSelectedNote()
    }

    private fun initText() {
        binding.noteTittleFrag2.text = Editable.Factory.getInstance().newEditable(selectNote.tittle)
        binding.noteDescriptionFrag2.text = Editable.Factory.getInstance().newEditable(selectNote.desc)
    }
    private fun initToolbar(menu: Menu){
        val createItem = menu.findItem(R.id.create_note_toolbar)
        val gridItem = menu.findItem(R.id.recycle_grid)
        val listItem = menu.findItem(R.id.recycle_list)
        val backItem = menu.findItem(R.id.back_note_toolbar)

        if (createItem != null) {
            createItem.isVisible = false
        }
        if (gridItem != null) {
            gridItem.isVisible = false
        }
        if (listItem != null) {
            listItem.isVisible = false
        }
        if (backItem != null) {
            backItem.isVisible = false
        }
    }
    private fun deleteDialog(notes: Note) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Удаление заметки")
        builder.setMessage("Вы действительно хотите удалить заметку?")
        builder.setPositiveButton("Нет") { dialog, d ->
        }
        builder.setNegativeButton("Да") { dialog, d ->
            notesViewModel.deleteNote(notes)
            backToFragment1()
        }
        builder.show()
    }
}