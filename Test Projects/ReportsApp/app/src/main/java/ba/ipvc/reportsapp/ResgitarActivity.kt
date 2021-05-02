package ba.ipvc.reportsapp

import android.content.Intent
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

class ResgitarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resgitar)
    }

    fun registar(view: View) {
        val intent = Intent(this, Login_Notes::class.java)
        val user = findViewById<EditText>(R.id.Email).text.toString()
        val pass = findViewById<EditText>(R.id.Password).text.toString()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.signup(user, pass)

        Log.d("Registar2", "ENTROU")
        when {
            TextUtils.isEmpty(Email.text) -> {
                Toast.makeText(this@ResgitarActivity, R.string.EmailEmpty, Toast.LENGTH_SHORT).show()

            }
            TextUtils.isEmpty(Password.text) -> {
                Toast.makeText(this@ResgitarActivity, R.string.PassEmpty, Toast.LENGTH_SHORT).show()
            }
            else -> {

                call.enqueue(object : Callback<Login> {
                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        Log.d("Registar", "FEZ")
                        if (response.isSuccessful) {
                            Log.d("Registar", "RESPONSE SUCESSFULL")
                            if (response.body()!!.status) {
                                Log.d("Registar", "MUDAR")
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@ResgitarActivity,
                                    R.string.BadResgister,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }

                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Toast.makeText(this@ResgitarActivity, "${t.message}", Toast.LENGTH_SHORT).show()

                    }
                })
            }
        }
    }
}