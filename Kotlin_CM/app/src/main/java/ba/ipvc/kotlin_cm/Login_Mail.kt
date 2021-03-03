package ba.ipvc.kotlin_cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Login_Mail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login__mail)
    }

    fun btn_topw_or_resgister(view: View) {
        val intent = Intent(this, Login_Mail::class.java).apply {

        }
        startActivity(intent)


    }
}