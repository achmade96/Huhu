package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.response.GooglePlaceResult
import com.tinfive.nearbyplace.model.response.GooglePlacesResponse
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MAPS
import com.tinfive.nearbyplace.networks.EndPoint.Masjid
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MasjidApi {

    @GET("maps/api/place/nearbysearch/json?")
    fun getNearbyPlaces(
        @Query(value = "location") location: String?,
        @Query(value = "radius") radius: String?,
        @Query(value = "type") type: String,
        @Query(value = "key") key: String? ): Observable<GooglePlaceResult>

//      @GET
//    fun getNearbyPlaces(@Url url: String): Single<List<GooglePlaceResult>>

    @GET(Masjid)
    fun getMosque(): Observable<List<DataMasjid>>
}