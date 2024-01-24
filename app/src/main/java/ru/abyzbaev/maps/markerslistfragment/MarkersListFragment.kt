package ru.abyzbaev.maps.markerslistfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import ru.abyzbaev.maps.MapViewModel
import ru.abyzbaev.maps.Marker
import ru.abyzbaev.maps.R
import ru.abyzbaev.maps.databinding.FragmentMarkersListBinding
import ru.abyzbaev.maps.itemtouchhelper.OnItemDismissListener
import ru.abyzbaev.maps.itemtouchhelper.SimpleItemTouchHelperCallback
import ru.abyzbaev.maps.markerDetails.DetailsMarkerFragment

class MarkersListFragment: Fragment(), OnItemDismissListener {

    private var _binding: FragmentMarkersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapViewModel

    private val adapter: MarkersListAdapter by lazy {
        MarkersListAdapter({
            marker -> navigateToDetails(marker)
        }, this)
    }

    private fun navigateToDetails(marker: Marker) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack("")
            //.add(DetailsMarkerFragment.newInstance(marker), "FRAGMENT_TAG")
            .replace(R.id.fragment_container, DetailsMarkerFragment.newInstance(marker))
            .commit()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarkersListBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        binding.recyclerViewMarkers.adapter = adapter

        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper: ItemTouchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerViewMarkers)
    }

    private fun initViewModel() {
        if (binding.recyclerViewMarkers.adapter != null) {
            throw IllegalStateException("The viewModel should initialised first")
        }

        viewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]
        Log.d("VIEW_MODEL", "$viewModel")
        viewModel.subscribeToLiveData().observe(this.viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }

    override fun onItemDismiss(marker: Marker) {
        viewModel.removeMarker(marker)
    }
}