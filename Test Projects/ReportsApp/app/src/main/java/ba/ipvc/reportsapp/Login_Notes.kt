package ba.ipvc.reportsapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import ba.ipvc.reportsapp.api.EndPoints
import ba.ipvc.reportsapp.api.Login
import ba.ipvc.reportsapp.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login__notes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.microedition.khronos.egl.EGLDisplay

class Login_Notes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login__notes)

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.sharedPref), Context.MODE_PRIVATE
        )

        val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

        if (user != 0) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun login(view: View) {
        Log.d("asd", "asd")
        val intent = Intent(this, MapsActivity::class.java)
        val user = findViewById<EditText>(R.id.Email).text.toString()
        val pass = findViewById<EditText>(R.id.Password).text.toString()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(user, pass)
        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.sharedPref), Context.MODE_PRIVATE
        )
        Log.d("Entrou", "NETRO")
        when {
            TextUtils.isEmpty(Email.text) -> {
                Toast.makeText(this@Login_Notes, R.string.EmailEmpty, Toast.LENGTH_SHORT).show()

            }
            TextUtils.isEmpty(Password.text) -> {
                Toast.makeText(this@Login_Notes, R.string.PassEmpty, Toast.LENGTH_SHORT).show()
            }
            else -> {

                call.enqueue(object : Callback<Login> {
                    override fun onResponse(call: Call<Login>, response: Response<Login>) {

                        if (response.isSuccessful) {
                            if (response.body()!!.status) {
                                with(sharedPref.edit()) {
                                    putInt(R.string.userlogged.toString(), response.body()!!.id)
                                    commit()
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                        this@Login_Notes,
                                        R.string.BadLogin,
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }

                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Toast.makeText(this@Login_Notes, "${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ERROU", t.message.toString())
                    }
                })
            }
        }


    }
    fun btnMAps(view: View){
        val intent = Intent(this, MapsActivity::class.java).apply {

        }
        startActivity(intent)
    }

    fun btnnotes(view: View) {
        val intent = Intent(this, NotesActivity::class.java).apply {

        }
        startActivity(intent)
    }


}