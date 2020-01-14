package com.tinfive.nearbyplace.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationA(
    @Expose
    @SerializedName("lat")
    var lat:Double=0.0,
    @Expose
    @SerializedName("lng")
    var lng:Double=0.0
)