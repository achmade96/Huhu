package com.tinfive.nearbyplace.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Geometry(
    @Expose
    @SerializedName("location")
    var locationA: LocationA
)
