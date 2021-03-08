package ba.ipvc.reportsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Login_Notes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login__notes)
    }
    fun btnnotes(view: View) {
        val intent = Intent(this, Notes::class.java).apply {

        }
        startActivity(intent)
    }

}