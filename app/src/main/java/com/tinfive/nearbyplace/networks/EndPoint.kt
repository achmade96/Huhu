package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.response.GooglePlacesResponse

object EndPoint {
    const val BASE_URL_MAPS : String = "https://maps.googleapis.com/"
    const val BASE_URL_MASJID : String = "https://raw.githubusercontent.com"
    const val Masjid : String = "/achmade96/lokasimasjid/master/datamasjid.json"
    const val MAPS_GOOGLE : String = "maps/api/place/nearbysearch/json?"

    val googleApiService: GooglePlacesResponse
        get() = RetrofitClient.getClient(BASE_URL_MAPS).create(GooglePlacesResponse::class.java)
}