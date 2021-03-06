package ba.ipvc.reportsapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ba.ipvc.reportsapp.adapter.LineAdapter
import ba.ipvc.reportsapp.dataclass.Notas
import kotlinx.android.synthetic.main.activity_notes.*
import java.util.*
import kotlin.collections.ArrayList

class Notes : AppCompatActivity() {
    private lateinit var myList: ArrayList<Notas>


    private var  mydate:Date =  Date();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        myList = ArrayList<Notas>()

        // ws

        for (i in 0 until 500 ) {
            myList.add(Notas("Nota nº$i","NOTA 123 Nº $i", mydate))
        }
        recycler_view.adapter = LineAdapter(myList)
        recycler_view.layoutManager = LinearLayoutManager(this)
    }

        fun insert(view: View) {

            myList.add(0, Notas( "Nota 000", "aasd", mydate))
            recycler_view.adapter?.notifyDataSetChanged()
        }



}