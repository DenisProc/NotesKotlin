package com.example.noteskotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.noteskotlin.R
import com.example.noteskotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onBackPressed() {
        super.onBackPressed()
        startFragment1()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Мои заметки"
        startFragment1()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    fun startFragment1(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, NoteFragment.newInstance())
            .commit()
    }
}