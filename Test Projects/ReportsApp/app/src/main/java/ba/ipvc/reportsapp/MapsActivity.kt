package ba.ipvc.reportsapp


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
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
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var reports: List<Report>

    // add to implement last known location
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //added to implement location periodic updates
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    //added to implement distance between two locations
    private var continenteLat: Double = 0.0
    private var continenteLong: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        Log.d("MAPAS", "entrou");
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
                Log.d("LOC", "${loc}")
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
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
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
                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                mMap.addMarker(
                                    MarkerOptions().position(position)
                                        .title(report.titulo.toString() + " : " + report.descricao)
                                        .snippet(report.descricao).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_RED
                                            )
                                        )
                                )
                            } else {
                                position = LatLng(report.lat.toDouble(), report.lng.toDouble())
                                mMap.addMarker(
                                    MarkerOptions().position(position)
                                        .title(report.titulo.toString() + " : " + report.descricao)
                                        .snippet(report.descricao).icon(
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
    fun criarR(view: View) {
        val intent = Intent(this, CreateReportActivity::class.java)



        startActivity(intent)

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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
        locationRequest.interval = 10000
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
        Log.d("ONRESUME", "onResume - startLocationUpdates")
    }
    /*   private fun getAddress(lat: Double, lng: Double): String {
           val geocoder = Geocoder(this)
           val list = geocoder.getFromLocation(lat, lng, 1)
           return list[0].getAddressLine(0)
       }*/

}