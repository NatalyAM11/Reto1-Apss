package com.example.reto1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.reto1.databinding.ActivityMainBinding
import com.example.reto1.model.UserRequest
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var user1: User
    lateinit var user2: User

    lateinit var userLogin: User

    var json: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var userLogout = intent.extras?.getBoolean("userLogout")

        //Permisos
        requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ), 11)

        //Usuarios predeterminados
        user1= User("alfa@gmail.com", "aplicacionesmoviles", R.drawable.rebeka, null, null)
        user2= User("beta@gmail.com", "aplicacionesmoviles", R.drawable.rebeka, null, null)


        val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult(), :: onResult)

        //binding
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)


        binding.loginBtn.setOnClickListener {

            if ((binding.userName.text.toString() == user1.nombre && binding.password.text.toString()==user1.contraseña) || (binding.userName.text.toString() == user2.nombre && binding.password.text.toString()==user2.contraseña) ) {

                //Defino cual de los dos usuarios entro
                if(binding.userName.text.toString() == user1.nombre && binding.password.text.toString()==user1.contraseña){
                    userLogin=user1
                }
                if(binding.userName.text.toString() == user2.nombre && binding.password.text.toString()==user2.contraseña){
                    userLogin=user2
                }

                //Envio el usuario que entro a la siguiente actividad
                json= Gson().toJson(userLogin)
                Log.e("usuario", json.toString())

                val intent= Intent (this, HomeActivity::class.java).apply {
                    putExtra("user", json)
                }

                //Guardo el estado
                val sharedPre = getSharedPreferences("alfa",Context.MODE_PRIVATE)
                sharedPre.edit().putString("usuarioActual", json).apply()

                launcher.launch(intent)


            } else{
                Toast.makeText(this,"Los datos que ingreso son incorrectos", Toast.LENGTH_LONG).show()
            }
        }


        //Saber si ya estaba loggeado
        val sharedPre = getSharedPreferences("alfa",Context.MODE_PRIVATE)
        val jsonUser= sharedPre.getString("usuarioActual", "NO_DATA")

        if((jsonUser != "NO_DATA" || jsonUser != null) && userLogout==false){

            val intent= Intent (this, HomeActivity::class.java).apply {
                putExtra("user", jsonUser) }

            launcher.launch(intent)
        }
        if(jsonUser!=null){
            Log.e("k", jsonUser.toString())
        }

    }

    override fun onPause(){
        super.onPause()

        if(json != null){
            Log.e ("este es el json del usuario", json!!)
        }
    }


    //Permisos
    private fun onResult( result: ActivityResult){}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            var allGrant = true
            for (result in grantResults){
                Log.e(">>>", " "+result)
                if( result == PackageManager.PERMISSION_DENIED) allGrant = false
            }

            if(allGrant){

            } else{
                Toast.makeText(this, "Tiene que aceptar todos los permisos para ingresar", Toast.LENGTH_SHORT).show()
            }
        }
    }

}