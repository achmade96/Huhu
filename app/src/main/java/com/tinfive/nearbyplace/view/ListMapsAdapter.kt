package com.tinfive.nearbyplace.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.response.Geometry
import com.tinfive.nearbyplace.model.response.GooglePlaceResult

class ListMapsAdapter(private val mapView: MutableList<GooglePlaceResult>) : RecyclerView.Adapter<ListMapsAdapter.MapViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mapfragment, parent, false)
        return MapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) = holder.let{
        it.clear()
        it.onBind(position)
        /*holder.setData(
            mapView[holder.adapterPosition],
            holder
        )*/
    }

    override fun getItemCount(): Int {
        return mapView.size
    }
    fun updateMap(mapApiUpdate: List<GooglePlaceResult>) {
        this.mapView.clear()
        this.mapView.addAll(mapApiUpdate)
        notifyDataSetChanged()

    }


    inner class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun clear() {

        }

        fun onBind(position: Int) {

            val (geometry, name) = mapView[position]

            inflateData(geometry, name)

        }

        private fun inflateData(geometry: Geometry,
                                name: String) {

        }

    }

}