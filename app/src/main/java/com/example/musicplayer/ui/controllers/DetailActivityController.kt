package com.example.musicplayer.ui.controllers

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.musicplayer.models.Song
import com.example.musicplayer.models.media.MediaPlayerHolder
import com.example.musicplayer.ui.views.DetailActivityView

class DetailActivityController(
    private val view: DetailActivityView,
    private val activity: AppCompatActivity,
    private val songs: Array<Song>

) {
    private var currentSongIndex: Int = 0
    private var playbackPositionBeforeTransition: Int = 0
    private val handler = Handler(Looper.getMainLooper())

    /**
     * The 'updateSeekBar' runnable is responsible for periodically updating the progress of the SeekBar
     * using the 'handler', ensuring that the updates are performed on the main thread.
     */
    private val updateSeekBar = object : Runnable {
        override fun run() {
            MediaPlayerHolder.mediaPlayer?.let { mediaPlayer ->
                view.seekBar.progress = mediaPlayer.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }

    fun setupButtonClickListeners() {
        view.playPauseButton.setOnClickListener { onPlayPauseButtonClick() }
        view.previousButton.setOnClickListener { onPreviousButtonClick() }
        view.nextButton.setOnClickListener { onNextButtonClick() }
    }

    fun initSongInfo() {
        val extras = activity.intent.extras
        val songTitle = extras?.getString(SONG_TITLE_KEY_INTENT) ?: ""

        view.songTitleTextView.text = songTitle
        currentSongIndex = songs.indexOfFirst { it.title == songTitle }
        view.albumArtImageView.setImageResource(
            songs.getOrNull(currentSongIndex)?.albumArt ?: 0
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
                Uri.parse("${BASE_PATH}${activity.packageName}/${songs[currentSongIndex].resource}")
            mediaPlayer.setDataSource(activity, songUri)
            mediaPlayer.prepare()
            mediaPlayer.start()
            view.seekBar.max = mediaPlayer.duration
            handler.postDelayed(updateSeekBar, 1000)

            view.playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
        }
    }

    private fun onPreviousButtonClick() {
        if (currentSongIndex > 0) {
            currentSongIndex -= 1
        } else {
            currentSongIndex = songs.size - 1
        }
        playSong()
        updateSongInfo()
    }

    private fun onNextButtonClick() {
        if (currentSongIndex < songs.size - 1) {
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
                view.playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                handler.removeCallbacks(updateSeekBar)
            } else {
                mediaPlayer.start()
                view.playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                handler.postDelayed(updateSeekBar, 1000)
            }
        }
    }

    private fun updateSongInfo() {
        val songTitles = songs.map { it.title }
        val albumArts = songs.map { it.albumArt }

        view.songTitleTextView.text = songTitles[currentSongIndex]
        view.albumArtImageView.setImageResource(albumArts[currentSongIndex])
    }

    /**
     * Sets up the SeekBar change listener, which handles user interactions with the SeekBar.
     * When the user changes the SeekBar's progress, it updates the MediaPlayer's playback position accordingly.
     */
    fun setupSeekBarChangeListener() {
        view.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MediaPlayerHolder.mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun setupMotionLayoutTransitionListener() {
        val motionLayout = view.motionLayout
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
                        Uri.parse("${BASE_PATH}${activity.packageName}/${songs[currentSongIndex].resource}")
                    mediaPlayer.setDataSource(activity, songUri)
                    mediaPlayer.prepare()
                    // Seek to the position before the transition
                    mediaPlayer.seekTo(playbackPositionBeforeTransition)
                    mediaPlayer.start()
                    view.seekBar.max = mediaPlayer.duration
                    handler.postDelayed(updateSeekBar, 1000)
                    view.playPauseButton.setImageResource(android.R.drawable.ic_media_pause)

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
