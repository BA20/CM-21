package ba.ipvc.reportsapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ba.ipvc.reportsapp.R
import ba.ipvc.reportsapp.dataclass.Notas
import kotlinx.android.synthetic.main.recyclerview_notes.view.*

class LineAdapter (val list: ArrayList<Notas>): RecyclerView.Adapter<LineViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recyclerview_notes, parent, false);
        return LineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentNota = list[position]

        holder.nome.text = currentNota.nome
        holder.data.text = currentNota.data.toString();

    }




}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val nome = itemView.nome
    val data = itemView.data
}


