package com.tinfive.nearbyplace.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GooglePlaceResult  (
    @Expose
    @SerializedName("geometry")
    var geometry: Geometry,
    @Expose
    @SerializedName("name")
    var name:String
)