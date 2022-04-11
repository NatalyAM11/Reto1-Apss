package com.example.reto1

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.reto1.databinding.FragmentProfileBinding
import com.example.reto1.model.UserRequest
import com.google.gson.Gson
import java.io.File

class ProfileFragment : Fragment(){


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    //Observers del usuario
    var listener: OnUserDataChangedListener? = null


    private var file: File? = null


    var uriImage: Uri? = null
    var bitmap: Bitmap? = null


    lateinit var userLogin: User
    private var userUpdated: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPre = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonUser= sharedPre.getString("usuarioActual", "NO_DATA")

        /*if(jsonUser != "NO_DATA"){
            val currentUser = Gson().fromJson(jsonUser, User:: class.java)
            userLogin=currentUser
        }*/

        var user = requireActivity().intent.extras?.getString("user")

        if(userUpdated==null){
            userLogin= Gson().fromJson(user, User:: class.java)
        } else {
            userLogin= userUpdated as User
        }


        _binding= FragmentProfileBinding.inflate(inflater, container, false)
        val view= binding.root


        val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult(), :: onResult)


        val galLauncher= registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        val camLauncher= registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onCameraResult)



        //set data user
        binding.usernameProfile.setText(userLogin.nombre)

        if(userLogin.drawablePhoto != null){
            binding.usuarioImageProfile.setImageResource(userLogin.drawablePhoto!!)
        }

        if(userLogin.photoUri!=null){
            binding.usuarioImageProfile.setImageURI(userLogin.photoUri)
        } else if (userLogin.photoBitmap!=null){
            binding.usuarioImageProfile.setImageBitmap(userLogin.photoBitmap)
        }


        //editar perfil
        binding.editProfile.setOnClickListener{
            //los botones de edición aparecen
            binding.camBtnProfile.setVisibility(View.VISIBLE)
            binding.galBtnProfile.setVisibility(View.VISIBLE)
            binding.newNameInput.setVisibility(View.VISIBLE)
            binding.listoBtn.setVisibility(View.VISIBLE)
        }


        //Opciones imagen
        //galeria
        binding.galBtnProfile.setOnClickListener{
            val i= Intent(Intent.ACTION_GET_CONTENT)
            i.type= "image/*"
            galLauncher.launch(i)
        }

        //Camara
        binding.camBtnProfile.setOnClickListener{
            val i= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file= File("${requireActivity().getExternalFilesDir(null)}/photo.png")
            val uri = FileProvider.getUriForFile(requireActivity(), "com.example.reto1", file!!)
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            camLauncher.launch(i)
        }


        //edición completa
        binding.listoBtn.setOnClickListener {

            userLogin.nombre=binding.newNameInput.text.toString()
            binding.usernameProfile.setText(userLogin.nombre)

            if(uriImage!= null || bitmap!= null){
                userLogin.drawablePhoto = null
            }

            userUpdated= User (binding.newNameInput.text.toString(), "n", userLogin.drawablePhoto,uriImage,bitmap)
            userLogin = userUpdated!!

            listener?.let {
                it.onUserDataChanged(userLogin)
            }


            //Guardo el estado del usuario nuevo
            val sharedPre = requireActivity().getSharedPreferences("alfa",Context.MODE_PRIVATE)
            val json= Gson().toJson(userLogin)
            sharedPre.edit().putString("usuarioActual", json).apply()

            //Desaparecen los botones de edición
            binding.camBtnProfile.setVisibility(View.INVISIBLE)
            binding.galBtnProfile.setVisibility(View.INVISIBLE)
            binding.newNameInput.setVisibility(View.INVISIBLE)
            binding.listoBtn.setVisibility(View.INVISIBLE)
        }

        //Logout
        binding.LogOutBtn.setOnClickListener {
            val logout= true

            //mando un boolean para indicar que cerre
            val intent= Intent (requireActivity(), MainActivity::class.java).apply {

                putExtra("userLogout", logout)

                val jsonu= Gson().toJson(userLogin)

                putExtra("newUSer", jsonu)
            }

            //Guardo el estado del usuario
            val sharedPre = requireActivity().getSharedPreferences("alfa",Context.MODE_PRIVATE)
            sharedPre.edit().putString("usuarioActual", "NO_DATA").apply()

            launcher.launch(intent)

        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding= null
    }

    companion object {
            fun newInstance ()= ProfileFragment()
    }


    override fun onResume() {
        super.onResume()
    }

    private fun onResult( result: ActivityResult){}


    //Camara y galeria
    private fun onCameraResult(activityResult: ActivityResult){

        bitmap= BitmapFactory.decodeFile(file?.path)

        binding.usuarioImageProfile.setImageBitmap(bitmap)
        // Log.e(">", scaledBitmap.toString())
    }

    private fun onGalleryResult(activityResult: ActivityResult){

        if(activityResult.resultCode == AppCompatActivity.RESULT_OK){
            uriImage = activityResult.data?.data
            uriImage?.let {
                binding.usuarioImageProfile.setImageURI(uriImage)
            }

        }
    }

    interface OnUserDataChangedListener {
        fun onUserDataChanged(user: User)
    }


}