package ba.ipvc.reportsapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import ba.ipvc.reportsapp.api.EndPoints
import ba.ipvc.reportsapp.api.Report
import ba.ipvc.reportsapp.api.outputReport
import ba.ipvc.reportsapp.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_create_report.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class CreateReportActivity : AppCompatActivity() {
    private val REQUEST_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_report)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipos,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter




        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                try {
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                } catch (e: ActivityNotFoundException) {
                    Log.d("ERRO CAMERA", e.toString())
                }
            } else {
                Toast.makeText(this, R.string.CameraErro, Toast.LENGTH_SHORT).show()
            }

        }


    }


    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@CreateReportActivity.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun createR(view: View) {
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.sharedPref), Context.MODE_PRIVATE
        )
        val intent = Intent(this, MapsActivity::class.java)
        val imgBitmap: Bitmap = findViewById<ImageView>(R.id.imageView).drawable.toBitmap()
        val imgFile: File = convertBitmapToFile("file", imgBitmap)
        val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), imgFile)
        val imagem: MultipartBody.Part =
            MultipartBody.Part.createFormData("imagem", imgFile.name, imgFileRequest)

        val rtitle = findViewById<EditText>(R.id.Rtitle).text.toString()
        val titulo: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), rtitle)
        val rdesc = findViewById<EditText>(R.id.rdescricao).text.toString()
        val descricao: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), rdesc)

        //shared pref

        val latitude: String? = sharedPref.getString(R.string.userloclat.toString(), "ERRO");
        val longitude: String? = sharedPref.getString(R.string.userloclng.toString(), "ERRO");


        val lat: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), latitude)
        val lng: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), longitude)

        val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)
        val userid = RequestBody.create(MediaType.parse("multipart/form-data"), user.toString())
        val tipo: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            spinner.selectedItem.toString()
        )

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.createR(titulo, descricao, lat, lng, imagem, userid, tipo)

        when {
            TextUtils.isEmpty(Rtitle.text) -> {
                Toast.makeText(this@CreateReportActivity, R.string.TitleEmpty, Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(rdescricao.text) -> {
                Toast.makeText(this@CreateReportActivity, R.string.descEmpty, Toast.LENGTH_SHORT).show()
            }
            else -> {
                call.enqueue(object : Callback<Report> {
                    override fun onResponse(call: Call<Report>, response: Response<Report>) {

                        if (response.isSuccessful) {
                            if (response.body()!!.status) {
                                startActivity(intent)
                                finish()
                            } else {

                                Toast.makeText(
                                    this@CreateReportActivity,
                                    R.string.BadCreate,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }



                    override fun onFailure(call: Call<Report>, t: Throwable) {
                        Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("Errou", "${t.message}")
                    }

                })
            }
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }

}


