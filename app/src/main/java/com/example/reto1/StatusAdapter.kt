package com.example.reto1

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.co.icesi.semana4kotlina.UtilDomi

class StatusAdapter: RecyclerView.Adapter<StatusView>() {

    private val status= ArrayList<Status>()

    //arreglo invertido para que la ultima publicación aparezca primero
    private val statusReversed= status.asReversed()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusView {
        var inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.statusrow, parent,false)
        val statusView= StatusView(view)
        return statusView
    }

    override fun onBindViewHolder(skeleton: StatusView, position: Int) {
        val statusn= statusReversed[position]

        //val path= UtilDomi.getPath(this, statusn.foto)
        skeleton.usuarioRow.text = statusn.usuario
        skeleton.ciudadRow.text= statusn.ciudad
        skeleton.fechaRow.text= statusn.fecha

       /* val bitmap= BitmapFactory.decodeFile(statusn.foto.toString())

        if(bitmap!=null){
            Log.e("elbitmap", bitmap.toString())
        }*/

        //skeleton.fotoRow.setImageBitmap(bitmap)

        if( statusn.fotoUri != null){
            skeleton.fotoRow.setImageURI(statusn.fotoUri)
        } else{
            skeleton.fotoRow.setImageBitmap(statusn.fotoBitmap)
        }

        if( statusn.usuarioImageUri != null){
            skeleton.usuarioImageRow.setImageURI(statusn.usuarioImageUri)
        } else if (statusn.usuarioImageBitmap != null){
            skeleton.usuarioImageRow.setImageBitmap(statusn.usuarioImageBitmap)
        } else{
            skeleton.usuarioImageRow.setImageResource(statusn.usuarioImageDrawable!!)
        }

        skeleton.estadoTextRow.text= statusn.estadoTexto
    }

    fun addStatus (statuss: Status){
        status.add(statuss)
        //notifyItemInserted(status.size-1)
    }

    fun haveArray (): ArrayList<Status> {
        return status
    }

    override fun getItemCount(): Int {
        return status.size
    }


}