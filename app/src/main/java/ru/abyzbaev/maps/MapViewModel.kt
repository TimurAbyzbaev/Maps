package ru.abyzbaev.maps

import androidx.lifecycle.*

class MarkersViewModel: ViewModel() {
    private val markers: MutableList<Marker> = mutableListOf()

    private val _liveData = MutableLiveData<MutableList<Marker>>()
    private var liveData: LiveData<MutableList<Marker>> = _liveData

    fun getMarks(): LiveData<MutableList<Marker>> {
        _liveData.postValue(markers)
        return liveData
    }

    fun addMark(newMarker: Marker) {
        markers.add(newMarker)
    }

    fun removeMarker(markerToDelete: Marker) {
        markers.remove(markerToDelete)
    }

    fun editMarker(marker: Marker) {

    }
}
