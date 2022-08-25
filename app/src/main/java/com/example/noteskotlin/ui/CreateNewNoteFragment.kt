package com.example.noteskotlin.ui

import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.noteskotlin.R
import com.example.noteskotlin.databinding.FragmentCreateNewNoteBinding
import com.example.noteskotlin.model.Note
import com.example.noteskotlin.model.NoteViewModel
import org.koin.android.ext.android.inject

class CreateNewNoteFragment : Fragment() {
    private val notesViewModel: NoteViewModel by inject()
    lateinit var binding: FragmentCreateNewNoteBinding
    var selectNote: Note = Note(10000)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentCreateNewNoteBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNewNoteFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        initToolbar(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.back_note_toolbar) {
            backToFragment1()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSelectNote()
        initText()

        binding.saveCreateFragment.setOnClickListener {
            sendData()
            notesViewModel.addNote(selectNote.tittle,selectNote.desc)
            backToFragment1()
        }
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
        selectNote.tittle = binding.noteTittleCreateFragment.text.toString()
        selectNote.desc = binding.noteDescriptionCreateFragment.text.toString()
        notesViewModel.selectedNoteIndex.observe(activity as LifecycleOwner) {
            selectNote.id = it
        }
        notesViewModel.editNote(selectNote)
    }

    private fun initSelectNote(): Note {
        notesViewModel.selectedNote.observe(activity as LifecycleOwner) {
            selectNote = it
        }
        return selectNote
    }

    private fun initText() {
        binding.noteTittleCreateFragment.text = Editable.Factory.getInstance().newEditable(selectNote.tittle)
        binding.noteDescriptionCreateFragment.text =
            Editable.Factory.getInstance().newEditable(selectNote.desc)
    }
    private fun initToolbar(menu: Menu){
        val createItemF = menu.findItem(R.id.create_note_toolbar)
        val gridItemF = menu.findItem(R.id.recycle_grid)
        val listItemF = menu.findItem(R.id.recycle_list)
        val delItemF = menu.findItem(R.id.delete_note_toolbar)

        if (createItemF != null) {
            createItemF.isVisible = false
        }
        if (gridItemF != null) {
            gridItemF.isVisible = false
        }
        if (listItemF != null) {
            listItemF.isVisible = false
        }
        if (delItemF != null) {
            delItemF.isVisible = false
        }
    }
}