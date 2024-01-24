package ru.abyzbaev.maps.markerslistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.maps.Marker
import ru.abyzbaev.maps.databinding.RecyclerItemViewBinding
import ru.abyzbaev.maps.itemtouchhelper.OnItemDismissListener
import ru.abyzbaev.taskmaster.ui.itemtouchhelper.ItemTouchHelperAdapter
import java.util.*

class MarkersListAdapter(
    private var itemDismissListener: OnItemDismissListener
) : RecyclerView.Adapter<MarkersListAdapter.MarkersListViewHolder>(),
    ItemTouchHelperAdapter {

    private var markers: MutableList<Marker> = arrayListOf()

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

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(markers, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(markers, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val marker = markers[position]
        itemDismissListener.onItemDismiss(marker)
        markers.remove(marker)
        notifyItemRemoved(position)
    }
}