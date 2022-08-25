package com.example.noteskotlin.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val PREFERENCE_NAME = "NOTES_PREFERENCE"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

class NotesDAO(private val context: Context) {
    private val notesConverter = NoteConverter()

    companion object {
        val KEY_NOTES = stringPreferencesKey("NOTES")
        val KEY_SETTINGS = stringPreferencesKey("SETTINGS")
    }

    suspend fun saveNotesInDataStore(listForSave: List<Note>) {
        context.dataStore.edit { savedNotes ->
            savedNotes[KEY_NOTES] = notesConverter.notesToString(listForSave)
        }
    }

    fun getSavedNotesFromDataStore(): Flow<List<Note>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[KEY_NOTES] ?: "[]"
            notesConverter.stringToNotes(json)
        }
    suspend fun saveSettings(string: String) {
        context.dataStore.edit { savedSettings ->
            savedSettings[KEY_SETTINGS] = string
        }
    }
    fun getSettings(): Flow<String> = context.dataStore.data
        .map { preferences ->
                preferences[KEY_SETTINGS] ?: "[]"
        }

}
