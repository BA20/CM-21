package ba.ipvc.reportsapp.adapter


import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.ipvc.reportsapp.EditNoteActivity
import ba.ipvc.reportsapp.R

import ba.ipvc.reportsapp.entities.Notes
import kotlinx.android.synthetic.main.recyclerview_notes.view.*

const val EXTRA_MESSAGE = "TITULO"
const val DESCRICAO = "DESCRICAO"
const val ID = "ID"
const val TITLE = "TITLE"
class NoteAdapter internal constructor(context: Context, private val callbackInterface:CallbackInterface ): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>()

    interface CallbackInterface {
        fun passResultCallback(uid: Int?)

    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noteTitle: TextView = itemView.findViewById(R.id.nome)
        val notedesc: TextView = itemView.findViewById(R.id.descricao)
        val noteDataMod: TextView = itemView.findViewById(R.id.data)
        val button: ImageButton = itemView.findViewById(R.id.delete)
        val buttonedit: ImageButton = itemView.findViewById(R.id.edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recyclerview_notes, parent, false);
        return NoteViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNota = notes[position]

        holder.noteTitle.text = currentNota.Title;
        holder.notedesc.text = currentNota.Description;
        holder.noteDataMod.text = currentNota.DateMod.toString();
        holder.button.setOnClickListener{
            callbackInterface.passResultCallback(currentNota.uid)
        }
        holder.buttonedit.setOnClickListener{
            val context=holder.noteTitle.context
            val tilte = holder.noteTitle.text.toString()
            val desc = holder.notedesc.text.toString()
            val id = currentNota.uid.toString()

            val intent = Intent (context, EditNoteActivity::class.java).apply{
                putExtra(EXTRA_MESSAGE, tilte)
                putExtra(TITLE, tilte)
                putExtra(DESCRICAO, desc)
                    putExtra(ID, id)
        }
            context.startActivity(intent)


        }
    }



    internal fun setNotes(notes: List<Notes>){
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size


}


