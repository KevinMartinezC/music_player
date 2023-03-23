package com.example.musicplayer.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var songTitleTextView: TextView
    private lateinit var albumArtImageView: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var playPauseButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private var currentSongIndex: Int = 0


    private val mainActivity = MainActivity()

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        initViews()
        initSongInfo()
    }

    private fun initViews() {
        songTitleTextView = binding.songTitleTextView
        albumArtImageView = binding.albumArtImageView
        seekBar = binding.seekBar
        playPauseButton = binding.playPauseButton
        previousButton = binding.previousButton
        nextButton = binding.nextButton
    }

    private fun initSongInfo() {
        val extras = intent.extras
        val songTitle = extras?.getString(SONG_TITLE_KEY_INTENT) ?: ""

        songTitleTextView.text = songTitle
        currentSongIndex = mainActivity.songs.indexOfFirst { it.title == songTitle }
        albumArtImageView.setImageResource(
            mainActivity.songs.getOrNull(currentSongIndex)?.albumArt ?: 0
        )
    }

    companion object {
        const val SONG_TITLE_KEY_INTENT: String = "songTitle"
    }

}