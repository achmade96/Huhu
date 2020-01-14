package com.tinfive.nearbyplace.view

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.response.GooglePlacesResponse
import com.tinfive.nearbyplace.networks.MasjidService
import com.tinfive.nearbyplace.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {
    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    private var mService = MasjidService()
    internal lateinit var currentPlaces: GooglePlacesResponse
    lateinit var context: Context

    lateinit var viewModel: ListViewModel
    private val masjidAdapter = ListMasjidAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        initializeComponent()
        observeViewModel()
    }

    private fun initializeComponent() {
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        listView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = masjidAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh()
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync { googleMap -> loadMap(googleMap) }

        //Requeest runtime Permission
        if (Build.VERSION.SDK_INT >= 24) {
            if (checkLocationPermisson()) {
                buildLocationRequest()
                buildLocationCallback()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations[p0.locations.size - 1]
                if (mMarker != null) {
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

//                val mLatLong = "$latitude,$longitude"

                val latLng = LatLng(latitude, longitude)

                //BINGUNG DISINI<<<-------------
                mService.getMosque("$mLastLocation")

//                mService.getNearbyPlaces("$mLastLocation", "500", "mosque", "AIzaSyBHkbWKsDCZtTUPn-qW-Lzjzmkbj7_1LmY")

                    (object : Callback<GooglePlacesResponse> {
                    override fun onFailure(call: Call<GooglePlacesResponse>, t: Throwable?) {
                        Toast.makeText(baseContext, "" + t!!.message, Toast.LENGTH_SHORT).show()
                    }

                    val geoCoder = Geocoder(this@MainActivity, Locale.getDefault())

                    override fun onResponse(
                        call: Call<GooglePlacesResponse>,
                        response: Response<GooglePlacesResponse>
                    ) {
                        currentPlaces = response.body()!!
                        if (response.isSuccessful) for (i in 0 until response.body()!!.googlePlaceResult.size) {
                            val markerOptions = MarkerOptions()
                                .position(latLng)
                                .title("AHA")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            val googlePlaces = response.body()!!.googlePlaceResult[i]
                            val lat = googlePlaces.geometry!!.locationA!!.lat
                            val lng = googlePlaces.geometry!!.locationA!!.lng
                            val placeName = googlePlaces.name
                            val latLng = LatLng(lat, lng)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)

                            mMap.addMarker(markerOptions)
                            viewModel.loadMosque(mLastLocation)


                        }
                        try {
                            val listAddress: List<Address> =
                                geoCoder.getFromLocation(latitude, longitude, 1)
                            if (null != listAddress && listAddress.size > 0) {
                                val placeAddress = listAddress.get(0).getAddressLine(0)
                                val placeName = listAddress.get(0).featureName
                                Log.d(
                                    "location me",
                                    "${listAddress.get(0).featureName} ${listAddress.get(0).adminArea} ${listAddress.get(
                                        0
                                    ).subLocality} ${listAddress.get(0).locale}"
                                )
                                Toast.makeText(
                                    applicationContext,
                                    "$placeName $placeAddress",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }


                })
                //Move Camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                val cu = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                // Animate Camera
                mMap.animateCamera(cu)


            }
        }
    }

    /*private fun getUrl(latitude: Double, longitude: Double): String {
        val googlePlaceUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=500") //1000=1km
        googlePlaceUrl.append("&type=mosque")
        googlePlaceUrl.append("&key=AIzaSyBHkbWKsDCZtTUPn-qW-Lzjzmkbj7_1LmY")

        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()
    }*/

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun checkLocationPermisson(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermisson()) {
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )
                            mMap.isMyLocationEnabled = true
                        }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadMap(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isCompassEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = true

        if (Build.VERSION.SDK_INT >= 24) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
                mMap.isMyLocationEnabled = true
        } else
            mMap.isMyLocationEnabled = true

        //Enable Zoom Control
        mMap.uiSettings.isZoomControlsEnabled

    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    //VIEW MASJID TAMPILAN 2
    private fun observeViewModel() {
        viewModel.masjid.observe(this, Observer { masjid ->
            masjid?.let {
                listView.visibility = View.VISIBLE
                masjidAdapter.updateMasjid(it)

                masjidAdapter.setOnItemClickListener(object :
                    ListMasjidAdapter.OnItemClickListener {
                    override fun onItemSelected(countries: DataMasjid) {

//                        println("PANGGIL MAP ASYU $countries.lat, ${countries.long} ")
                    }

                })
            }
        })

        viewModel.masjidLoadError.observe(this, Observer { isError ->
            isError?.let { list_error.visibility = if (it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                progress_circular.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    listView.visibility = View.GONE
                }
            }
        })
    }

}

