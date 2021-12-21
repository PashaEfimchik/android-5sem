package com.example.lab_3_media.file_models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MusicFile(
    val path: String,
    val title: String,
    val artist: String,
    val duration: String,
    val art: Bitmap?
) : Parcelable {

}