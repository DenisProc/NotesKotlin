package com.example.noteskotlin.model
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppDI {
    val koinModule = module {
        single { NotesDAO(androidContext()) }
        single { NoteRepository(get()) }
        viewModel {
            NoteViewModel(get())
        }
    }
}