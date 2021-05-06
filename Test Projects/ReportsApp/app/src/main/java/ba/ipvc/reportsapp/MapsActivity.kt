package ba.ipvc.reportsapp


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import ba.ipvc.reportsapp.adapter.ID
import ba.ipvc.reportsapp.api.EndPoints
import ba.ipvc.reportsapp.api.Report
import ba.ipvc.reportsapp.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var reports: List<Report>

    // add to implement last known location
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //added to implement location periodic updates
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        //setSupportActionBar(findViewById(R.id.my_toolbar))
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //added to implement location periodic updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.sharedPref), Context.MODE_PRIVATE
                )


                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                Log.d("LOCa", "${loc}")
                with(sharedPref.edit()) {
                    putString(
                        ba.ipvc.reportsapp.R.string.userloclat.toString(),
                        lastLocation.latitude.toString()
                    )
                    putString(
                        ba.ipvc.reportsapp.R.string.userloclng.toString(),
                        lastLocation.longitude.toString()
                    )

                    commit()
                }

                FiltrosButton5km.setOnClickListener() {
                    mMap!!.clear()
                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng



                    call.enqueue(object : Callback<List<Report>> {

                        override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                            val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.sharedPref), Context.MODE_PRIVATE
                            )
                            if (response.isSuccessful) {

                                reports = response.body()!!
                                for (report in reports) {

                                    val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

                                    if (user != 0) {
                                        if (report.user_id != user) {
                                            if (calculateDistance(loc.latitude,loc.longitude,report.lat,report.lng)<5000) {

                                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                                mMap!!.addMarker(
                                                    MarkerOptions().position(position)
                                                        .title(report.titulo)
                                                        .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                                        .icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_RED
                                                            )
                                                        )
                                                )
                                            }
                                        } else {
                                            if (calculateDistance(loc.latitude,loc.longitude,report.lat,report.lng)<5000) {
                                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                                mMap!!.addMarker(
                                                    MarkerOptions().position(position)
                                                        .title(report.titulo)
                                                        .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                                        //0                   1                       2                       3              4                5                     6                   7                   8
                                                        .icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_BLUE
                                                            )
                                                        )

                                                )
                                            }

                                        }


                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()


                        }

                    })
                }
                FiltrosButton2km.setOnClickListener() {
                    mMap!!.clear()
                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng



                    call.enqueue(object : Callback<List<Report>> {

                        override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                            val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.sharedPref), Context.MODE_PRIVATE
                            )
                            if (response.isSuccessful) {

                                reports = response.body()!!
                                for (report in reports) {

                                    val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

                                    if (user != 0) {
                                        if (report.user_id != user) {
                                            if (calculateDistance(loc.latitude,loc.longitude,report.lat,report.lng)<2000) {

                                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                                mMap!!.addMarker(
                                                    MarkerOptions().position(position)
                                                        .title(report.titulo)
                                                        .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                                        .icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_RED
                                                            )
                                                        )
                                                )
                                            }
                                        } else {
                                            if (calculateDistance(loc.latitude,loc.longitude,report.lat,report.lng)<2000) {
                                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                                mMap!!.addMarker(
                                                    MarkerOptions().position(position)
                                                        .title(report.titulo)
                                                        .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                                        //0                   1                       2                       3              4                5                     6                   7                   8
                                                        .icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_BLUE
                                                            )
                                                        )

                                                )
                                            }

                                        }


                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()


                        }

                    })
                }


                //mMap.addMarker(MarkerOptions().position(loc).title("Marker"))
                if (ActivityCompat.checkSelfPermission(
                        this@MapsActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@MapsActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap!!.isMyLocationEnabled = true
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                // reverse geocoding
                // val address = getAddress(lastLocation.latitude, lastLocation.longitude)


                Log.d(
                    "LOCATION CALLBACK",
                    "new location received - " + loc.latitude + " -" + loc.longitude
                )

            }

        }


        // request creation
        createLocationRequest()

        addReportButton.setOnClickListener() {
            val intent = Intent(this, CreateReportActivity::class.java).apply {

            }
            startActivity(intent)
        }



        FiltrosButtonConts.setOnClickListener() {
            filtro_const()
        }
        FiltrosButtonHole.setOnClickListener() {
            filtro_hole()
        }

        FiltrosButtonwater.setOnClickListener() {
            filtro_water()
        }
        FiltrosButtonall.setOnClickListener() {
            pontos()
        }



    }


    fun pontos() {
        Log.d("Pnts", "load")
        mMap!!.clear()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng



        call.enqueue(object : Callback<List<Report>> {

            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.sharedPref), Context.MODE_PRIVATE
                )
                if (response.isSuccessful) {

                    reports = response.body()!!
                    for (report in reports) {
                        Log.d("IMA", report.imagem.toString())
                        val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

                        if (user != 0) {
                            if (report.user_id != user) {

                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                mMap!!.addMarker(
                                    MarkerOptions().position(position)
                                        .title(report.titulo)
                                        .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_RED
                                            )
                                        )
                                )
                            } else {
                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                mMap!!.addMarker(
                                    MarkerOptions().position(position)
                                        .title(report.titulo)
                                        .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                        //0                   1                       2                       3              4                5                     6                   7                   8
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_BLUE
                                            )
                                        )

                                )

                            }


                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()


            }

        })


    }

    fun filtro_const() {
        mMap!!.clear()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng



        call.enqueue(object : Callback<List<Report>> {

            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.sharedPref), Context.MODE_PRIVATE
                )
                if (response.isSuccessful) {

                    reports = response.body()!!
                    for (report in reports) {

                        val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

                        if (user != 0) {
                            if (report.user_id != user) {
                                if (report.tipo == "Contruction") {
                                    Log.d("Contruction", report.tipo.toString())
                                    position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                    mMap!!.addMarker(
                                        MarkerOptions().position(position)
                                            .title(report.titulo)
                                            .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_RED
                                                )
                                            )
                                    )
                                }
                            } else {
                                if (report.tipo == "Contruction") {
                                    position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                    mMap!!.addMarker(
                                        MarkerOptions().position(position)
                                            .title(report.titulo)
                                            .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                            //0                   1                       2                       3              4                5                     6                   7                   8
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_BLUE
                                                )
                                            )

                                    )
                                }

                            }


                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()


            }

        })
    }

    fun filtro_hole() {
        mMap!!.clear()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng



        call.enqueue(object : Callback<List<Report>> {

            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.sharedPref), Context.MODE_PRIVATE
                )
                if (response.isSuccessful) {

                    reports = response.body()!!
                    for (report in reports) {
                        Log.d("IMA", report.imagem.toString())
                        val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

                        if (user != 0) {
                            if (report.user_id != user) {
                                if (report.tipo == "Hole") {

                                    position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                    mMap!!.addMarker(
                                        MarkerOptions().position(position)
                                            .title(report.titulo)
                                            .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_RED
                                                )
                                            )
                                    )
                                }
                            } else {
                                if (report.tipo == "Hole") {
                                    position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                    mMap!!.addMarker(
                                        MarkerOptions().position(position)
                                            .title(report.titulo)
                                            .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                            //0                   1                       2                       3              4                5                     6                   7                   8
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_BLUE
                                                )
                                            )

                                    )
                                }

                            }


                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()


            }

        })
    }

    fun filtro_water() {
        mMap!!.clear()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng



        call.enqueue(object : Callback<List<Report>> {

            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.sharedPref), Context.MODE_PRIVATE
                )
                if (response.isSuccessful) {

                    reports = response.body()!!
                    for (report in reports) {
                        Log.d("IMA", report.imagem.toString())
                        val user: Int = sharedPref.getInt(R.string.userlogged.toString(), 0)

                        if (user != 0) {
                            if (report.user_id != user) {
                                if (report.tipo == "Water Leak") {
                                    Log.d("water", report.tipo)
                                    position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                    mMap!!.addMarker(
                                        MarkerOptions().position(position)
                                            .title(report.titulo)
                                            .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_RED
                                                )
                                            )
                                    )
                                }
                            } else {
                                if (report.tipo == "Water Leak") {
                                    position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                    mMap!!.addMarker(
                                        MarkerOptions().position(position)
                                            .title(report.id.toString())
                                            .snippet(report.titulo + " + " + report.descricao + "+" + report.tipo + "+" + report.user_id + "+" + user + "+" + report.imagem + "+" + report.id + "+" + report.lat + "+" + report.lng)
                                            //0                   1                       2                       3              4                5                     6                   7                   8
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_BLUE
                                                )
                                            )

                                    )
                                }

                            }


                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()


            }

        })
    }

    fun signout(view: View) {
        val intent = Intent(this, Login_Notes::class.java)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.sharedPref), Context.MODE_PRIVATE
        )

        sharedPref.edit() {
            putInt(R.string.userlogged.toString(), 0)
            commit()
        }

        startActivity(intent)
        finish()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        if (mMap != null) {
            pontos()
            mMap!!.setInfoWindowAdapter(MarkerWindow(this))

            mMap!!.setOnInfoWindowClickListener { marker ->
                val intent = Intent(this, editReport::class.java).apply {
                    putExtra(ID, marker.snippet.toString())


                }
                startActivity(intent)
            }
        }
        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }

    companion object {
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    //added to implement location periodic updates
    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000 // 5sec
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("ONPAUSE", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()

    }


    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }
    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        // distance in meter
        return results[0]
    }

}