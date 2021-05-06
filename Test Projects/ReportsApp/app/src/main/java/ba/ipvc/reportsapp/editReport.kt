package ba.ipvc.reportsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ba.ipvc.reportsapp.adapter.ID
import ba.ipvc.reportsapp.api.EndPoints
import ba.ipvc.reportsapp.api.Report
import ba.ipvc.reportsapp.api.ServiceBuilder
import ba.ipvc.reportsapp.api.outputReport
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_report.*
import kotlinx.android.synthetic.main.activity_edit_report.*

import retrofit2.Call
import retrofit2.Response

class editReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_report)
        val ID = intent.getStringExtra(ID)
        val markinfos = ID?.split("+")?.toTypedArray()
        if (markinfos?.get(3).equals(markinfos?.get(4))) {
            val etitle = findViewById<TextView>(R.id.Etitle)
            val edesc = findViewById<TextView>(R.id.Edescricao)
            val eimagem = findViewById<ImageView>(R.id.vimagem)
            Etitle.setText(markinfos?.get(0))
            Edescricao.setText(markinfos?.get(1))


            /*Picasso.get()
                .load(
                    "https://reportsappbernardo.000webhostapp.com/myslim/api/img/" + (markinfos?.get(
                        5
                    ) ?: String()) + ".png"
                )
                .into(eimagem)*/
        } else {
            Toast.makeText(this@editReport, "${R.string.noedit}", Toast.LENGTH_LONG).show()


        }

        btneditar.setOnClickListener() {
            val id = markinfos?.get(6);
            val vtitle = findViewById<EditText>(R.id.Etitle).text.toString()
            val vdesc = findViewById<EditText>(R.id.Edescricao).text.toString()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.updateReport(id, vtitle, vdesc)



            call.enqueue(object : retrofit2.Callback<outputReport> {
                override fun onResponse(
                    call: Call<outputReport>,
                    response: Response<outputReport>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            Toast.makeText(this@editReport, "${R.string.editado}", Toast.LENGTH_LONG).show()
                            finish()



                        } else {
                            Log.d("ERROUU", response.toString())
                            Toast.makeText(this@editReport, "${R.string.BadResgister}", Toast.LENGTH_LONG).show()

                        }
                    }

                }


                override fun onFailure(call: Call<outputReport>, t: Throwable) {
                    Toast.makeText(
                        this@editReport,
                        "${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Errou", "${t.message}")
                }

            })



        }
        buttonCancelE.setOnClickListener {
            finish()
        }

        btnDelete.setOnClickListener(){
            val id = markinfos?.get(6);
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.deleteReport(id)

            call.enqueue(object : retrofit2.Callback<outputReport> {
                override fun onResponse(
                    call: Call<outputReport>,
                    response: Response<outputReport>
                ) {Log.d("Tentei", "Entrar no editar")

                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            Toast.makeText(this@editReport, "${R.string.eliminado}", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this@editReport, "${R.string.baddelete}", Toast.LENGTH_LONG).show()

                        }
                    }

                }


                override fun onFailure(call: Call<outputReport>, t: Throwable) {

                    Log.d("Errou", "${t.message}")
                }

            })


        }
    }


}