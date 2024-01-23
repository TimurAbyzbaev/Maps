package ru.abyzbaev.maps

import com.yandex.mapkit.geometry.Point


data class Marker(
    var point: Point,
    var name: String,
    var description: String
)