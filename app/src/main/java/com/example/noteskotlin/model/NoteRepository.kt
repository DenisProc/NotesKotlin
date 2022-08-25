package com.example.noteskotlin.model
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NoteRepository(val notesDAO: NotesDAO) {

    companion object {
        private var selectedNote = Note(1000000)
    }

    suspend fun addNote(tittle: String, desc: String) {
        var id = 0
        val list = mutableListOf<Note>()
        val currentList = notesDAO.getSavedNotesFromDataStore().first()
        for (notes in currentList) {
            if (notes.id > id)
                id = notes.id
        }
        list.addAll(currentList)
        list.add(Note(id + 1, tittle, desc))
        notesDAO.saveNotesInDataStore(list)
    }

    suspend fun editNote(notes: Note) {
        val listForEdit = mutableListOf<Note>()
        val currentList = notesDAO.getSavedNotesFromDataStore().first()
        for (note in currentList) {
            if (note.id == notes.id) {
                note.tittle = notes.tittle
                note.desc = notes.desc
            }
        }
        listForEdit.addAll(currentList)
        notesDAO.saveNotesInDataStore(listForEdit)
    }

    suspend fun deleteNote(notes: Note) {

        val listForSave = mutableListOf<Note>()
        val currentList = notesDAO.getSavedNotesFromDataStore().first()
        listForSave.addAll(currentList)
        val listForDel = listForSave.filter { it.id != notes.id } as MutableList<Note>

        notesDAO.saveNotesInDataStore(listForDel)
    }

    fun getFlowList(): Flow<List<Note>> {
        return notesDAO.getSavedNotesFromDataStore()
    }

    fun saveSelectedNote(notes: Note): Note {
        selectedNote = notes
        return selectedNote
    }

    fun getSelectedNote(): Note {
        return selectedNote
    }
    suspend fun saveSettings(string: String) {
        notesDAO.saveSettings(string)
    }

    fun getSettings():Flow<String> {
        return notesDAO.getSettings()
    }
}

