package ba.ipvc.reportsapp

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ba.ipvc.reportsapp.api.EndPoints
import ba.ipvc.reportsapp.api.Report
import ba.ipvc.reportsapp.api.ServiceBuilder
import ba.ipvc.reportsapp.api.outputReport
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class MarkerWindow(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.window_mark, null)

    private fun rendowWindowText(marker: Marker, view: View) {

        val vtitle = view.findViewById<TextView>(R.id.vtitle)
        val vdesc = view.findViewById<TextView>(R.id.vdesc)
        val vtipo = view.findViewById<TextView>(R.id.vtipo)
        val vuser = view.findViewById<TextView>(R.id.vuser)
        val vimagem = view.findViewById<ImageView>(R.id.vimagem)


        val markinfos = marker.snippet.split("+").toTypedArray()

        vtitle.text = markinfos[0]
        vdesc.text = markinfos[1]
        vtipo.text = markinfos[2]
        vuser.text = markinfos[3]


        Picasso.get()
            .load("https://reportsappbernardo.000webhostapp.com/myslim/api/img/" + markinfos[5] + ".png")
            .into(
                vimagem
                  , object : Callback {
                        override fun onSuccess() {
                            if (marker.isInfoWindowShown) {
                                marker.hideInfoWindow()
                                marker.showInfoWindow()
                            }
                        }

                        override fun onError(e: Exception?) {
                            Toast.makeText(
                                mContext,
                                mContext.getString(R.string.falhaimg),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })




    }


    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
