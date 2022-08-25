package com.example.noteskotlin.model
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope


class NoteViewModel(val repository: NoteRepository) : ViewModel() {
    val selectedNote: MutableLiveData<Note> by lazy { MutableLiveData<Note>() }
    val selectedNoteIndex: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    val selected:StateFlow<String> =
        repository.getSettings().stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val notesList: StateFlow<List<Note>> =
        repository.getFlowList().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addNote(tittle: String, desc: String) =  viewModelScope.launch {
        repository.addNote(tittle,desc)
    }

    fun deleteNote(notes: Note) = viewModelScope.launch {
        repository.deleteNote(notes)
    }

    fun editNote(notes: Note) = viewModelScope.launch{
        repository.editNote(notes)
    }
    fun saveSelectedNote(notes: Note){
        repository.saveSelectedNote(notes)
    }

    fun getSelectedNote():Note{
        return repository.getSelectedNote()
    }
    fun saveSettings(string: String)= viewModelScope.launch{
        repository.saveSettings(string)
    }
}