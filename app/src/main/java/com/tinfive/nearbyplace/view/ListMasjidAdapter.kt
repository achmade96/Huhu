package com.tinfive.nearbyplace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.utils.getProgressDrawable
import com.tinfive.nearbyplace.utils.loadImage
import kotlinx.android.synthetic.main.row.view.*

class ListMasjidAdapter(val masjid: MutableList<DataMasjid>) : RecyclerView.Adapter<ListMasjidAdapter.MasjidViewHolder>() {

    private var context: Context? = null

    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemSelected(countries: DataMasjid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasjidViewHolder {
        context = parent.context
        val layoutInflate = LayoutInflater.from(context).inflate(R.layout.row, parent, false)
        return MasjidViewHolder(layoutInflate)

    }

    override fun getItemCount(): Int = masjid.size

    override fun onBindViewHolder(holder: MasjidViewHolder, position: Int) {
        holder.clear()
        holder.bind(position)
    }

    fun updateMasjid(masjidApiUpdate: List<DataMasjid>) {
        this.masjid.clear()
        this.masjid.addAll(masjidApiUpdate)
        notifyDataSetChanged()

    }


    internal fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    inner class MasjidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(v: View?) {
            mOnItemClickListener?.onItemSelected(masjid[adapterPosition])
        }


        fun clear() {
            itemView.titleTv.text = ""
            itemView.descTv.text = ""
            itemView.iconIv.setImageDrawable(null)

        }

        fun bind(position: Int) {
            val (id,
                kabkota,
                kecamatan,
                nama_masjid,
                tipologi,
                alamat,
                lat,
                long,
                foto) = masjid[position]
            inflateData(
                id,
                kabkota,
                kecamatan,
                nama_masjid,
                tipologi,
                alamat,
                lat,
                long,
                foto
            )
        }

        private fun inflateData(
            id: Int,
            kabkota: String,
            kecamatan: String,
            nama_masjid: String,
            tipologi: String,
            alamat: String,
            lat: Double,
            long: Double,
            foto: String
        ) {
            val progressDrawable: CircularProgressDrawable = getProgressDrawable(itemView.context)


            nama_masjid.let {
                itemView.titleTv.text = it
            }

            println(alamat)

            alamat.let {
                itemView.descTv.text = String.format("%s, %s, %s", it, kecamatan, kabkota)
            }

            foto.let {
                itemView.iconIv.loadImage(it, progressDrawable)
            }
            itemView.setOnClickListener(this)
        }




    }
}
