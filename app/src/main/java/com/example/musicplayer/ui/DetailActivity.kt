package com.example.musicplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityDetailBinding
import com.example.musicplayer.models.media.MediaPlayerHolder
import com.example.musicplayer.ui.controllers.DetailActivityController
import com.example.musicplayer.ui.views.DetailActivityView


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailActivityView: DetailActivityView
    private lateinit var detailActivityController: DetailActivityController
    private val mainActivity = MainActivity()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_SONG_KEY, detailActivityController.currentSongIndex)
        outState.putInt(PLAYBACK_POSITION_KEY, MediaPlayerHolder.mediaPlayer?.currentPosition ?: 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)


        detailActivityView = DetailActivityView(binding)
        detailActivityController =
            DetailActivityController(detailActivityView, this, mainActivity.songs)

        detailActivityController.initSongInfo()
        detailActivityController.setupSeekBarChangeListener()
        detailActivityController.setupButtonClickListeners()
        detailActivityController.setupMotionLayoutTransitionListener()

        if (savedInstanceState != null) {
            detailActivityController.currentSongIndex =
                savedInstanceState.getInt(CURRENT_SONG_KEY)
            detailActivityController.playbackPositionBeforeTransition =
                savedInstanceState.getInt(PLAYBACK_POSITION_KEY)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerHolder.mediaPlayer?.release()
        MediaPlayerHolder.mediaPlayer = null
    }

    companion object{
        const val CURRENT_SONG_KEY: String = "currentSongIndex"
        const val  PLAYBACK_POSITION_KEY: String ="playbackPosition"
    }


}
