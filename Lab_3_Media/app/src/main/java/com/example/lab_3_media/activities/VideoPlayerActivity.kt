package com.example.lab_3_media.activities

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.RequiresApi
import com.example.lab_3_media.R
import com.example.lab_3_media.file_models.VideoFile
import java.io.File

class VideoPlayerActivity : Activity() {
    private var videoFile: VideoFile? = null

    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoView = findViewById(R.id.videoView)
        mediaController = MediaController(this)

        mediaController.setAnchorView(videoView)

        videoView.setMediaController(mediaController)
        videoView.requestFocus()
        videoView.start()
        videoView.setOnCompletionListener {
            it.seekTo(0)
            it.start()
        }

        val arguments: Bundle? = intent.extras
        if (arguments != null) {
            videoFile = arguments.getParcelable("videoFile")

            if (videoFile != null) {
                videoView.setVideoURI(Uri.fromFile(File(videoFile!!.path)))
                videoView.start()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        videoView.stopPlayback()
        videoView.releasePointerCapture()
        super.onDestroy()
    }
}