package com.example.reto1.model

import android.graphics.Bitmap
import android.net.Uri

data class UserRequest (
    var userName: String,
    var userPassword: String,
    var userPhotoURI: Uri?,
    var userPhotoBitmap: Bitmap?,
)