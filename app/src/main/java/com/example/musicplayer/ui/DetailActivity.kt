package com.example.musicplayer.ui

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityDetailBinding
import com.example.musicplayer.models.media.MediaPlayerHolder


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

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBar = object : Runnable {
        override fun run() {
            MediaPlayerHolder.mediaPlayer?.let { mediaPlayer ->
                seekBar.progress = mediaPlayer.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }
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
        playSong()
    }
    private fun playSong() {
        MediaPlayerHolder.mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            // Set the data source using a Uri
            val songUri =
                Uri.parse("${BASE_PATH}${packageName}/${mainActivity.songs[currentSongIndex].resource}")
            mediaPlayer.setDataSource(this, songUri)
            mediaPlayer.prepare()
            mediaPlayer.start()
            seekBar.max = mediaPlayer.duration
            handler.postDelayed(updateSeekBar, 1000)
        }
    }
    companion object {
        const val SONG_TITLE_KEY_INTENT: String = "songTitle"
        const val BASE_PATH: String = "android.resource://"
    }

}