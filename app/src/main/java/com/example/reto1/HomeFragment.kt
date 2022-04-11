package com.example.reto1

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reto1.databinding.FragmentHomeBinding
import com.example.reto1.model.UserRequest
import com.google.gson.Gson
import kotlin.reflect.typeOf

class HomeFragment : Fragment(), PublicarFragment.onNewStatusListener, ProfileFragment.OnUserDataChangedListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    //status
    private val adapter= StatusAdapter()

    lateinit var userLogin: User

    private var statuss= arrayListOf<Status>()


    var currentStatuss: String? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding= FragmentHomeBinding.inflate(inflater, container, false)
        val view= binding.root

        //recrear el estado
        val statusRecycler= binding.statusRecycler
        statusRecycler.setHasFixedSize(true)
        statusRecycler.layoutManager= LinearLayoutManager(activity)
        statusRecycler.adapter = adapter

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onUserDataChanged(user: User) {

        this.userLogin = user

        Log.e("mainuser", user.nombre)
    }


    companion object {
        fun newInstance ()= HomeFragment()
    }

    override fun onNewStatus(status: Status) {
        statuss.add(status)

        //if(jsonUserCopia.size != 0){
            if(currentStatuss!=null){
                /*jsonUserCopia.forEach { s->
                val newStatus = Status (s.id, s.usuario, s.usuarioImageDrawable, s.usuarioImageUri, s.usuarioImageBitmap, s.ciudad, s.fecha, s.fotoUri,s.fotoBitmap, s.estadoTexto)
                adapter.addStatus(newStatus)
            }*/
        } else {
            val newStatus = Status (status.id, status.usuario, status.usuarioImageDrawable, status.usuarioImageUri, status.usuarioImageBitmap, status.ciudad, status.fecha, status.fotoUri,status.fotoBitmap, status.estadoTexto)
            adapter.addStatus(newStatus)

            Log.e("estadossssTodos", statuss.toString())
        }


    }

    override fun onPause(){
        super.onPause()

        //Guardo el estado
        val sharedPre = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonStatus= Gson().toJson(statuss)
        sharedPre.edit().putString("statusActual", jsonStatus).apply()


        //Log.e("kdkdk", jsonStatus)
        //sharedPre.edit().putString("statusActual", status.toString()).apply()
    }

    override fun onResume() {
        super.onResume()

        //Recupero el estado
        val sharedPre = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonStatus = sharedPre.getString("statusActual", "NO_DATA")

        if (jsonStatus!="NO_DATA") {
            //val currentStatusss = Gson().fromJson(jsonStatus, Array<Status>:: class.java)

            /*for(status in currentStatusss){
                Log.e(">>>",status.estadoTexto)
            }*/

        }

    }

    }
