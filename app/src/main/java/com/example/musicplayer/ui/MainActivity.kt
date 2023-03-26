package com.example.musicplayer.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.models.Song
import com.example.musicplayer.models.media.MediaPlayerHolder

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentSongIndex", currentSongIndex)
    }

    val songs = arrayOf(
        Song(SONG_NAME_ONE, R.raw.song1, R.drawable.album_art_1),
        Song(SONG_NAME_TWO, R.raw.song2, R.drawable.album_art_2),
        Song(SONG_NAME_THREE, R.raw.song3, R.drawable.album_art_3)
    )
    private var currentSongIndex: Int = 0

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()
        setupListView()

        if (savedInstanceState != null) {
            currentSongIndex = savedInstanceState.getInt("currentSongIndex")
        }
    }

    private fun initViews() {
        listView = binding.listView
    }

    private fun setupListView() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            songs.map { it.title }.toTypedArray()
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            playSelectedSong(position)
            navigateToDetailActivity(position)
        }
    }

    private fun playSelectedSong(position: Int) {
        MediaPlayerHolder.mediaPlayer?.release()
        currentSongIndex = position
        MediaPlayerHolder.mediaPlayer =
            MediaPlayer.create(this@MainActivity, songs[position].resource)
        MediaPlayerHolder.mediaPlayer?.start()
    }

    private fun navigateToDetailActivity(position: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(SONG_TITLE_KEY, songs[position].title)
        startActivity(intent)
    }

    companion object {
        const val SONG_NAME_ONE: String = "Bar Liar"
        const val SONG_NAME_TWO: String = "Girls Like You"
        const val SONG_NAME_THREE: String = "See You Again"
        const val SONG_TITLE_KEY: String = "songTitle"
    }
}