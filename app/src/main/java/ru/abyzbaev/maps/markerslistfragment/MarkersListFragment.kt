package ru.abyzbaev.maps.markerslistfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.abyzbaev.maps.MapViewModel
import ru.abyzbaev.maps.databinding.FragmentMarkersListBinding

class MarkersListFragment: Fragment() {

    private var _binding: FragmentMarkersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapViewModel

    private val adapter = MarkersListAdapter()

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
}