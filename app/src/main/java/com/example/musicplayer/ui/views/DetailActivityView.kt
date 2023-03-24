package com.example.musicplayer.ui.views

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.musicplayer.databinding.ActivityDetailBinding

class DetailActivityView(binding: ActivityDetailBinding) {
    val songTitleTextView: TextView = binding.songTitleTextView
    val albumArtImageView: ImageView = binding.albumArtImageView
    val seekBar: SeekBar = binding.seekBar
    val playPauseButton: ImageButton = binding.playPauseButton
    val previousButton: ImageButton = binding.previousButton
    val nextButton: ImageButton = binding.nextButton
    val motionLayout: MotionLayout = binding.motionLayout
}
