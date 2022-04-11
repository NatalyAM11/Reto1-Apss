package com.example.reto1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView

class Status {

    var id: String
    var usuario: String
    var usuarioImageDrawable: Int?
    var usuarioImageUri: Uri?
    var usuarioImageBitmap: Bitmap?
    var ciudad: String
    var fecha: String
    var fotoUri: Uri?
    var fotoBitmap: Bitmap?
    var estadoTexto: String


    constructor(id: String, usuario:String, usuarioImageDrawable: Int?, usuarioImageUri: Uri?, usuarioImageBitmap: Bitmap?, ciudad: String, fecha:String, fotoUri: Uri?,fotoBitmap: Bitmap?, estadoTexto: String){
        this.id= id
        this.usuario= usuario
        this.usuarioImageDrawable=usuarioImageDrawable
        this.usuarioImageUri= usuarioImageUri
        this.usuarioImageBitmap=usuarioImageBitmap
        this.ciudad=ciudad
        this.fecha=fecha
        this.fotoUri=fotoUri
        this.fotoBitmap= fotoBitmap
        this.estadoTexto=estadoTexto


    }
}