package ru.abyzbaev.maps.markerslistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.maps.Marker
import ru.abyzbaev.maps.databinding.RecyclerItemViewBinding

class MarkersListAdapter() : RecyclerView.Adapter<MarkersListAdapter.MarkersListViewHolder>() {

    private var markers: List<Marker> = arrayListOf()

    inner class MarkersListViewHolder(
        private val binding: RecyclerItemViewBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: Marker) {
            binding.markerNameTextview.text = marker.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkersListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemViewBinding.inflate(inflater, parent, false)
        return MarkersListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindViewHolder(holder: MarkersListViewHolder, position: Int) {
        val marker = markers[position]
        holder.bind(marker)
    }

    fun setData(markers: MutableList<Marker>) {
        this.markers = arrayListOf()
        this.markers = markers
        notifyDataSetChanged()
    }

}