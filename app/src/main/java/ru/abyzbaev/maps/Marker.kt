package ru.abyzbaev.maps

import android.graphics.Point

data class Marker(
    var point: Point,
    var name: String,
    var description: String
)