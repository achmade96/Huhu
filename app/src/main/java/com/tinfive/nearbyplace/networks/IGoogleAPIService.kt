/*
package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.response.GooglePlaceResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IGoogleAPIService {
    */
/*@GET("maps/api/place/nearbysearch/json?")
    fun getNearbyPlaces(
        @Query(value = "location") location: String?,
        @Query(value = "radius") radius: String?,
        @Query(value = "type") type: String,
        @Query(value = "key") key: String? ): Observable<GooglePlaceResult>*//*

    @GET("maps/api/place/nearbysearch/json?")
    fun getNearbyPlaces(@Url url:String): Observable<GooglePlaceResult>
}*/
