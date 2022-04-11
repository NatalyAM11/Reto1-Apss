package com.example.reto1

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri

class User {

    var nombre: String
    var contraseña: String
    var drawablePhoto: Int?
    var photoUri: Uri?
    var photoBitmap: Bitmap?

    constructor(nombre: String, contraseña:String, drawablePhoto: Int?, photoUri: Uri?, photoBitmap: Bitmap?){
        this.nombre=nombre
        this.contraseña=contraseña
        this.drawablePhoto= drawablePhoto
        this.photoUri= photoUri
        this.photoBitmap= photoBitmap
    }
}