package ru.abyzbaev.maps.markerslistfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.maps.Marker
import ru.abyzbaev.maps.databinding.RecyclerItemViewBinding
import ru.abyzbaev.maps.itemtouchhelper.OnItemDismissListener
import ru.abyzbaev.maps.markerDetails.DetailsMarkerFragment
import ru.abyzbaev.taskmaster.ui.itemtouchhelper.ItemTouchHelperAdapter
import java.util.*

class MarkersListAdapter(
    private val clickListener: (Marker) -> Unit,
    private var itemDismissListener: OnItemDismissListener
) : RecyclerView.Adapter<MarkersListAdapter.MarkersListViewHolder>(),
    ItemTouchHelperAdapter {

    private var markers: MutableList<Marker> = arrayListOf()
    private lateinit var context: Context
    inner class MarkersListViewHolder(
        private val binding: RecyclerItemViewBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: Marker, clickListener: (Marker) -> Unit) {
            binding.markerNameTextview.text = marker.name
            binding.root.setOnClickListener { clickListener(marker) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkersListViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemViewBinding.inflate(inflater, parent, false)
        return MarkersListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindViewHolder(holder: MarkersListViewHolder, position: Int) {
        val marker = markers[position]
        holder.bind(marker, clickListener)
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