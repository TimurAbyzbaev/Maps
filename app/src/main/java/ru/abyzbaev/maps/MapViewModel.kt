package ru.abyzbaev.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private val markers: MutableList<Marker> = mutableListOf()

    private val _liveData = MutableLiveData<MutableList<Marker>>()
    private var liveData: LiveData<MutableList<Marker>> = _liveData

    fun subscribeToLiveData(): LiveData<MutableList<Marker>> {
        _liveData.postValue(markers)
        return liveData
    }

    fun getMarks(): LiveData<MutableList<Marker>> {
        _liveData.postValue(markers)
        return liveData
    }

    fun addMark(newMarker: Marker) {
        markers.add(newMarker)
        Log.d("MARKS", "Current markers list: $markers")
        _liveData.postValue(markers)
    }

    fun removeMarker(markerToDelete: Marker) {
        markers.remove(markerToDelete)
        _liveData.postValue(markers)
    }

    fun editMarker(marker: Marker) {
        (markers.find { it.point == marker.point }?.apply {
            this.name = marker.name
            this.description = marker.description
        })
    }
    fun getSize() = markers.size
}