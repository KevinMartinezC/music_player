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
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerHolder.mediaPlayer?.release()
        MediaPlayerHolder.mediaPlayer = null
    }


}
