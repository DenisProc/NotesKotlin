package com.example.noteskotlin.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteskotlin.R
import com.example.noteskotlin.databinding.FragmentNotesBinding
import com.example.noteskotlin.model.NoteViewModel
import com.example.noteskotlin.model.Note
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NoteFragment : Fragment(), NoteAdapter.Listener {
    private val notesViewModel: NoteViewModel by inject()
    private lateinit var binding: FragmentNotesBinding
    private val adapter = NoteAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch {
                notesViewModel.notesList.collect { list ->
                    adapter.submitList(list)
                }
            }
            launch { notesViewModel.selected.collect {string ->
            initRecyclerView(string)}
            } }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentNotesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val delItem = menu.findItem(R.id.delete_note_toolbar)
        val backItem = menu.findItem(R.id.back_note_toolbar)
        if (delItem != null) {
            delItem.isVisible = false
        }
        if (backItem != null) {
            backItem.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        initGridOrListRecycle(item)
        return super.onOptionsItemSelected(item)
    }


    companion object {
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    private fun initRecyclerView(string: String) {
        if (string == "LIST") {
            binding.apply {
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = adapter
            }
        } else {
            binding.apply { val gridLayoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                recyclerView.layoutManager = gridLayoutManager
                recyclerView.adapter = adapter
            }
        }
        adapter.refresh()
    }

    override fun onClick(notes: Note) {
        notesViewModel.saveSelectedNote(notes)
        startFragment2()
    }

    override fun onLongClick(view: View, notes: Note): Boolean {
        initPopupMenu(view, notes)
        return true
    }

    private fun initPopupMenu(view: View, notes: Note) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_popup_delete) {
                deleteDialog(notes)
                true
            } else false
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
            startFragment1()
        }
        builder.show()
    }

    private fun startFragment1() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, NoteFragment.newInstance())
            .commit()
    }

    private fun startFragment2() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack("")
            .replace(R.id.fragment_container, NoteFragment2.newInstance())
            .commit()
    }

    private fun startFragmentCreateNewNote() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack("")
            .replace(R.id.fragment_container, CreateNewNoteFragment.newInstance())
            .commit()
    }

    private fun initGridOrListRecycle(item: MenuItem) {
        when (item.getItemId()) {
            R.id.create_note_toolbar -> startFragmentCreateNewNote()
            R.id.recycle_grid -> {
                notesViewModel.saveSettings("GRID")
            }
            R.id.recycle_list -> {
                notesViewModel.saveSettings("LIST")
            }
        }
    }
}

