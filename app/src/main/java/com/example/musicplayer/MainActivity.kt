package com.example.musicplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.models.Song

class MainActivity : AppCompatActivity() {
    private lateinit var listview: ListView

    private val songs = arrayOf(
        Song(SONG_NAME_ONE, R.raw.song1, R.drawable.album_art_1),
        Song(SONG_NAME_TWO, R.raw.song2, R.drawable.album_art_2),
        Song(SONG_NAME_THREE, R.raw.song3, R.drawable.album_art_3)
    )

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
    companion object{
        const val SONG_NAME_ONE : String = "Bar Liar"
        const val SONG_NAME_TWO : String = "Girls Like You"
        const val SONG_NAME_THREE : String = "See You Again"
    }
}