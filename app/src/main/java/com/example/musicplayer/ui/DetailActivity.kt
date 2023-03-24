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
import androidx.constraintlayout.motion.widget.MotionLayout
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
    private var playbackPositionBeforeTransition: Int = 0

    private val mainActivity = MainActivity()

    private lateinit var binding: ActivityDetailBinding

    private val handler = Handler(Looper.getMainLooper())

    /**
     * The 'updateSeekBar' runnable is responsible for periodically updating the progress of the SeekBar
     * using the 'handler', ensuring that the updates are performed on the main thread.
     */
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
        setupSeekBarChangeListener()
        setupButtonClickListeners()
        setupMotionLayoutTransitionListener()

    }

    private fun initViews() {
        songTitleTextView = binding.songTitleTextView
        albumArtImageView = binding.albumArtImageView
        seekBar = binding.seekBar
        playPauseButton = binding.playPauseButton
        previousButton = binding.previousButton
        nextButton = binding.nextButton
    }

    private fun setupButtonClickListeners() {
        playPauseButton.setOnClickListener { onPlayPauseButtonClick() }
        previousButton.setOnClickListener { onPreviousButtonClick() }
        nextButton.setOnClickListener { onNextButtonClick() }
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

            playPauseButton.setImageResource(android.R.drawable.ic_media_pause)

        }
    }

    private fun onPreviousButtonClick() {
        if (currentSongIndex > 0) {
            currentSongIndex -= 1
        } else {
            currentSongIndex = mainActivity.songs.size - 1
        }
        playSong()
        updateSongInfo()
    }

    private fun onNextButtonClick() {
        if (currentSongIndex < mainActivity.songs.size - 1) {
            currentSongIndex += 1
        } else {
            currentSongIndex = 0
        }
        playSong()
        updateSongInfo()
    }
    private fun onPlayPauseButtonClick() {
        MediaPlayerHolder.mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                handler.removeCallbacks(updateSeekBar)
            } else {
                mediaPlayer.start()
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                handler.postDelayed(updateSeekBar, 1000)
            }
        }
    }
    private fun updateSongInfo() {
        val songTitles = mainActivity.songs.map { it.title }
        val albumArts = mainActivity.songs.map { it.albumArt }

        songTitleTextView.text = songTitles[currentSongIndex]
        albumArtImageView.setImageResource(albumArts[currentSongIndex])
    }

    /**
     * Sets up the SeekBar change listener, which handles user interactions with the SeekBar.
     * When the user changes the SeekBar's progress, it updates the MediaPlayer's playback position accordingly.
     */
    private fun setupSeekBarChangeListener() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MediaPlayerHolder.mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupMotionLayoutTransitionListener() {
        val motionLayout = binding.motionLayout
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }
            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                MediaPlayerHolder.mediaPlayer?.let { mediaPlayer ->
                    // Store the current position before stopping the MediaPlayer
                    playbackPositionBeforeTransition = mediaPlayer.currentPosition
                    mediaPlayer.stop()
                    mediaPlayer.reset()

                    val songUri =
                        Uri.parse("${BASE_PATH}${packageName}/${mainActivity.songs[currentSongIndex].resource}")
                    mediaPlayer.setDataSource(this@DetailActivity, songUri)
                    mediaPlayer.prepare()
                    // Seek to the position before the transition
                    mediaPlayer.seekTo(playbackPositionBeforeTransition)
                    mediaPlayer.start()
                    seekBar.max = mediaPlayer.duration
                    handler.postDelayed(updateSeekBar, 1000)
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
    }

    companion object {
        const val SONG_TITLE_KEY_INTENT: String = "songTitle"
        const val BASE_PATH: String = "android.resource://"
    }

}