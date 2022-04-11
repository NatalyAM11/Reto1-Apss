package com.example.reto1

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatusView (itemView: View) :RecyclerView.ViewHolder(itemView) {

    //componentes iu
    var usuarioRow: TextView= itemView.findViewById(R.id.usuarioRow)
    var usuarioImageRow: ImageView= itemView.findViewById(R.id.usuarioImageProfile)
    var ciudadRow: TextView = itemView.findViewById(R.id.ciudadRow)
    var fechaRow: TextView = itemView.findViewById(R.id.fechaRow)
    var fotoRow: ImageView = itemView.findViewById(R.id.fotoStatusRow)
    var estadoTextRow: TextView = itemView.findViewById(R.id.textStatusRow)

    init {

    }

}