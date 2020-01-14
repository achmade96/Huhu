package com.tinfive.nearbyplace.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.response.GooglePlaceResult
import com.tinfive.nearbyplace.model.response.GooglePlacesResponse
import com.tinfive.nearbyplace.networks.MasjidService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {


    private val masjidSrv = MasjidService()
    private val googleSrv = MasjidService()

    private val disposable = CompositeDisposable()
    val masjid = MutableLiveData<List<DataMasjid>>()
    val place = MutableLiveData<List<GooglePlaceResult>>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchMasjid()
    }

    fun loadMosque(location: Location) {
        fetchMosque(location)
    }

    private fun fetchMosque(location: Location) {
        loading.value = true
        val mLocations = "${location.latitude},${location.longitude}"
        disposable.add(
            googleSrv.getMosque(mLocations)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<GooglePlaceResult>() {
                    override fun onComplete() {
                            loading.value = false
                    }

                    override fun onNext(t: GooglePlaceResult) {
                        place.value= listOf(t)
                        masjidLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        masjidLoadError.value = true
                        loading.value = false
                    }

                })

        )

    }

    private fun fetchMasjid() {
        loading.value = true
        disposable.add(
            masjidSrv.getMasjid()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<DataMasjid>>() {
                    override fun onComplete() {
                        loading.value = false
                        //todo : Load Map Place
                    }

                    override fun onNext(t: List<DataMasjid>) {
                        println("DATA ${t.size}")
                        masjid.value = t
                        masjidLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        masjidLoadError.value = true
                        loading.value = false
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}