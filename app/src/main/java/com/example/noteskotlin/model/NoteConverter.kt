package com.example.noteskotlin.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class NoteConverter {

    companion object {
        private var notesStringSave = ""
        private var listToReturn = mutableListOf<Note>()
    }

    fun notesToString(listNotes: List<Note>): String {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        notesStringSave = gson.toJson(listNotes)
        return notesStringSave
    }

    fun stringToNotes(stringToList: String): MutableList<Note> {
        val type = Types.newParameterizedType(List::class.java, Note::class.java)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<List<Note>> = moshi.adapter(type)
        listToReturn = jsonAdapter.fromJson(stringToList) as MutableList<Note>
        return listToReturn
    }
}