package com.tinfive.nearbyplace.model

import com.google.gson.annotations.SerializedName

data class  DataMasjid (

    @SerializedName("id") var id : Int,
    @SerializedName("kabkota") var kabkota : String,
    @SerializedName("kecamatan") var kecamatan : String,
    @SerializedName("nama_masjid") var nama_masjid : String,
    @SerializedName("tipologi") var tipologi : String,
    @SerializedName("alamat") var alamat : String,
    @SerializedName("lat") var lat : Double,
    @SerializedName("long") var long : Double,
    @SerializedName("foto") var foto : String
)