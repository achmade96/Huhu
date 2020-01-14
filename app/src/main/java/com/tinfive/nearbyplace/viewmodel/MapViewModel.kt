/*

package com.tinfive.nearbyplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.gson.JsonObject
import com.tinfive.nearbyplace.model.response.GooglePlaceResult
import com.tinfive.nearbyplace.model.response.GooglePlacesResponse
import javax.security.auth.callback.Callback

class MapViewModel : ViewModel() {

    private lateinit var mMap: GoogleMap

    val markers = MutableLiveData<List<Marker>>()
    val place = MutableLiveData<List<GooglePlaceResult>>()
    val tempMarkerList = ArrayList<Marker>()

    fun getMarkers(zoom: Double?): MutableLiveData<List<Marker>> {

        setMarkers(zoom)
        return this.markers
    }

    fun setMarkers(zoom: Double?) {
        for (i in tempMarkerList) {
            i.title = null
        }
        tempMarkerList.clear()

        fetchList(tempMarkerList, markers, zoom)
    }

    private fun fetchList(place: MutableLiveData<List<GooglePlaceResult>>): Any {
        return this.place
    }


}
*/
