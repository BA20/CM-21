package ba.ipvc.reportsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ba.ipvc.reportsapp.adapter.*
import ba.ipvc.reportsapp.entities.Notes
import ba.ipvc.reportsapp.viewModel.NotasViewModel
import java.util.*

class EditNoteActivity : AppCompatActivity() {
    private lateinit var notesViewModel: NotasViewModel
    private lateinit var editTitle: EditText
    private lateinit var editDesc: EditText
    private var mydate: Date = Date();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        val iniTitle:String? = intent.getStringExtra(TITLE)
        val iniDesc:String? = intent.getStringExtra(DESCRICAO)






        findViewById<EditText>(R.id.inputName).setText(iniTitle)
        findViewById<EditText>(R.id.inputDesc).setText(iniDesc)

        notesViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)


    }

    fun ban_editnota(view: View){
          editTitle = findViewById(R.id.inputName)
          editDesc = findViewById(R.id.inputDesc)
       val uid = intent.getStringExtra(ID)

            if(TextUtils.isEmpty(editTitle.text)){
                Toast.makeText(applicationContext, "Empty Title", Toast.LENGTH_LONG).show()
            }
            if(TextUtils.isEmpty(editDesc.text)) {
                Toast.makeText(applicationContext, "Empty Description", Toast.LENGTH_LONG).show()
            }
          else{

                notesViewModel.updateDescriptionFromUid (uid!!.toInt(),editTitle.text.toString(), editDesc.text.toString(), mydate.toString())

                Toast.makeText(applicationContext, "Done", Toast.LENGTH_LONG).show()

        }

        val intent = Intent(this, NotesActivity::class.java).apply {

        }
        startActivity(intent)
    }

}