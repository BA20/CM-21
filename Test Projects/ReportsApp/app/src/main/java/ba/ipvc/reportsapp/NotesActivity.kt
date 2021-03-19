package ba.ipvc.reportsapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.ipvc.reportsapp.adapter.NoteAdapter
import ba.ipvc.reportsapp.entities.Notes
import ba.ipvc.reportsapp.viewModel.NotasViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class NotesActivity : AppCompatActivity(), NoteAdapter.CallbackInterface {
    private lateinit var notesViewModel: NotasViewModel
    private val newWordActivityRequestCode = 1;


    private var mydate: Date = Date();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NoteAdapter(this,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)



        // view model
        notesViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notesViewModel.allNotes.observe(this, Observer { notes ->
            // Update the cached copy of the words in the adapter.
            notes?.let { adapter.setNotes(it) }
        })


    }
    override fun passResultCallback(uid: Int?) {
        notesViewModel.deletenotebyid(uid)
    }




            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
                    val ptitulo = data?.getStringExtra(AddNoteActivity.EXTRA_REPLY_TITULO)
                    val pdesc = data?.getStringExtra(AddNoteActivity.EXTRA_REPLY_DESC)



                    if (ptitulo != null && pdesc != null) {
                        val not = Notes(
                            Title = ptitulo.toString(),
                            Description = pdesc.toString(),
                            DateMod = mydate.toString())
                        notesViewModel.insertNote(not)

                        Toast.makeText(applicationContext, "Nota inserida", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Erro ao inserir nota!", Toast.LENGTH_LONG).show()
                }
            }




    fun ban_addnote(view: View) {
        val intent = Intent(this, AddNoteActivity::class.java).apply {

        }
        startActivityForResult(intent, newWordActivityRequestCode)

    }






}

