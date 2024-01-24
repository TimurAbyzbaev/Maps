package ru.abyzbaev.maps.markerDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import ru.abyzbaev.maps.MapViewModel
import ru.abyzbaev.maps.Marker
import ru.abyzbaev.maps.R
import ru.abyzbaev.maps.databinding.FragmentDetailsMarkerBinding
import ru.abyzbaev.maps.databinding.FragmentMapBinding

class DetailsMarkerFragment(private val marker: Marker) : Fragment() {

    private var _binding: FragmentDetailsMarkerBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(marker: Marker) = DetailsMarkerFragment(marker)
    }

    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsMarkerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        binding.markerName.setText(marker.name)
        binding.markerDescription.setText(marker.description)
        binding.markerPoint.setText(marker.point.toString())

        binding.markerName.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                marker.name = p0.toString()
                viewModel.editMarker(marker)
            }

        })

        binding.markerDescription.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                marker.description = p0.toString()
                viewModel.editMarker(marker)
            }

        })
    }

}