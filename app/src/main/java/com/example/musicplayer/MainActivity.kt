package com.example.musicplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var listview: ListView

    private val songs = arrayOf("Bar Liar", "Girls Like You", "See You Again")
    private val songResources = intArrayOf(R.raw.song1, R.raw.song2, R.raw.song3)
    private val albumArts = intArrayOf(R.drawable.album_art_1, R.drawable.album_art_2, R.drawable.album_art_3)

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()
        setupListView()

    }
    private fun initViews() {
        listview = binding.listView
    }
    private fun setupListView() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, songs)
        listview.adapter = adapter
    }
}