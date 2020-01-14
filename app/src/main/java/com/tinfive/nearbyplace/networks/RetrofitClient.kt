package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MAPS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit?=null

    fun getClient(baseUrll:String) : Retrofit {
        if(retrofit==null)
        {
            retrofit= Retrofit.Builder()
                .baseUrl(baseUrll)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

}