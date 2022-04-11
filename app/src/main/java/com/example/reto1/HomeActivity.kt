package com.example.reto1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.reto1.databinding.ActivityHomeBinding
import com.example.reto1.model.UserRequest
import com.google.gson.Gson

class HomeActivity : AppCompatActivity() {


    private lateinit var homeFragment: HomeFragment
    private lateinit var publicarFragment: PublicarFragment
    private lateinit var profileFragment: ProfileFragment

    private lateinit var binding: ActivityHomeBinding

    private lateinit var userDes: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult(), :: onResult)

        //Saber si ya estaba loggeado
        val sharedPre = getSharedPreferences("alfa",Context.MODE_PRIVATE)
        val jsonUser= sharedPre.getString("usuarioActual", "NO_DATA")

        if((jsonUser == "NO_DATA" || jsonUser == null)){
            val intent= Intent (this, MainActivity::class.java).apply {}
            launcher.launch(intent)
        }

        var user = intent.extras?.getString("user")
        userDes= Gson().fromJson(user, User:: class.java)
        Log.e("el usuario actual", userDes.nombre)

        //binding
        binding= ActivityHomeBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        homeFragment= HomeFragment.newInstance()
        publicarFragment= PublicarFragment.newInstance()
        profileFragment= ProfileFragment.newInstance()


        //suscripcion
        publicarFragment.listener = homeFragment
        profileFragment.listener = publicarFragment


        //para que aparezca el home de primero
        showFragment(homeFragment)

        //Barra de navegacion
        binding.navigator.setOnItemSelectedListener { option: MenuItem ->

            when (option.itemId) {
                R.id.homeNavBar -> showFragment(homeFragment)
                R.id.plusNavBar -> showFragment(publicarFragment)
                R.id.profileNavBar -> showFragment(profileFragment)
            }
            true
        }
    }


    fun showFragment(fragment: Fragment) {
        val transaction= supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }


    private fun onResult( result: ActivityResult){}


}