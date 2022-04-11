package com.example.reto1

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.reto1.databinding.FragmentStatusBinding
import com.example.reto1.model.UserRequest
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PublicarFragment : Fragment(), ProfileFragment.OnUserDataChangedListener{

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    //Listener
    var listener: onNewStatusListener?= null

    private lateinit var homeFragment: HomeFragment
    var uriImage: Uri? = null
    var bitmap: Bitmap? = null

    //Fecha
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var date = Date()
    var fecha: String = dateFormat.format(date)

    private var file: File? = null

    //Menu desplegable
    var adapter: ArrayAdapter<CharSequence>? = null
    private lateinit var ciudad: String


    private lateinit var userDes: User
    private var userUpdated: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPre = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonUser= sharedPre.getString("usuarioActual", "NO_DATA")

        //Toma los valores que le mande desde el main al principio
        if(jsonUser != "NO_DATA"){
            val currentUser = Gson().fromJson(jsonUser, User:: class.java)
            userDes=currentUser
        }

        //Si al usuario no le han modificado ningun dato en el perfil, toma el valor inicial que le mande en el main
        //Pero si ya lo modificaron entonces toma el valor de userUpdated
        var user = requireActivity().intent.extras?.getString("user")

        if(userUpdated==null){
            userDes= Gson().fromJson(user, User:: class.java)
        } else {
            userDes= userUpdated as User
        }
        Log.e ("Este es el user actual", userDes.nombre)


        //binding
        _binding= FragmentStatusBinding.inflate(inflater, container, false)
        val view= binding.root


        val galLauncher= registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        val camLauncher= registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onCameraResult)



        //Menu desplegable, ingreso sus valores
        val ciudades = resources.getStringArray(R.array.ciudades)

        adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.ciudades, android.R.layout.simple_spinner_item);
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCiudades.setAdapter(adapter);

       binding.spinnerCiudades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
           override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               ciudad = ciudades[p2] }
           override fun onNothingSelected(p0: AdapterView<*>?) {
               TODO("Not yet implemented")
           }
       }


        //Opciones de la imagen

        //Galeria
        binding.galBtn.setOnClickListener{
            val i= Intent(Intent.ACTION_GET_CONTENT)
            i.type= "image/*"
            galLauncher.launch(i)
        }

        //Camara
        binding.camBtn.setOnClickListener{
            val i= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file= File("${requireActivity().getExternalFilesDir(null)}/photo.png")
            val uri = FileProvider.getUriForFile(requireActivity(), "com.example.reto1", file!!)
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            if(file?.path.toString()!=null){
                Log.e(">>", file?.path.toString())
            }
            camLauncher.launch(i)
        }


        //Se crea una publicación
        binding.publicarBtn.setOnClickListener{

            if((uriImage!=null || bitmap!=null) && userDes!= null) {
                val status= Status(UUID.randomUUID().toString(), userDes.nombre, userDes.drawablePhoto, userDes.photoUri,userDes.photoBitmap, ciudad, fecha, uriImage,bitmap, binding.statusText.text.toString())
                listener?.let {
                    it.onNewStatus(status)
                }
                uriImage = null
                bitmap = null

                Toast.makeText(requireActivity(),"Se publico exitosamente tu foto", Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(requireActivity(),"Complete toda la información necesaria", Toast.LENGTH_LONG).show()
            }

        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

    override fun onUserDataChanged(user: User) {
        this.userUpdated = user
    }

    interface onNewStatusListener{
        fun onNewStatus(status: Status)
    }

    //camara y galeria
    private fun onCameraResult(activityResult: ActivityResult){

        bitmap= BitmapFactory.decodeFile(file?.path)

        if(bitmap!=null){
            binding.fotoStatus.setImageBitmap(bitmap)
        }

    }

    private fun onGalleryResult(activityResult: ActivityResult){

        if(activityResult.resultCode == AppCompatActivity.RESULT_OK){
            uriImage = activityResult.data?.data
            uriImage?.let {
                binding.fotoStatus.setImageURI(uriImage)
            }
        }

        /*val pathBitmap= BitmapFactory.decodeFile(file?.path)
        binding.fotoStatus.setImageBitmap(pathBitmap)*/
    }


    companion object {
        fun newInstance ()= PublicarFragment()
    }


}