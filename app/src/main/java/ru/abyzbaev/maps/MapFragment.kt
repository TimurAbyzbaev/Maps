package ru.abyzbaev.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.app.ActivityCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import ru.abyzbaev.maps.databinding.FragmentMapBinding

class MapFragment : Fragment(), InputListener, GeoObjectTapListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    // 55.7522, 37.6156 Москва
    private var latitude = 55.7522
    private var longitude = 37.6156

    private var markers: MutableList<Marker> = arrayListOf()

    companion object {
        fun newInstance() = MapFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE  = 123 // Вы можете выбрать любой уникальный код

    }

    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        // TODO: Use the ViewModel
        MapKitFactory.initialize(requireContext())
        initViewModel()
        requestLocationPermission()
        Log.d("####","lat = $latitude, lon = $longitude")
        moveMapToCurrentLocation()
        setMyPositionMark()
        binding.mapview.map.addTapListener(this)
        binding.mapview.map.addInputListener(this)

        for (marker in markers) {
            drawMarker(marker.point)
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]
        Log.d("VIEW_MODEL", "$viewModel")
        viewModel.subscribeToLiveData().observe(this.viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Subscribed to liveData", Toast.LENGTH_SHORT).show()
            Log.d("MARKS", "Map marks: $it")
            markers = it
        })
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешение на местоположение уже предоставлено, получаем координаты
            getLocation()
        } else {
            // Запрашиваем разрешение у пользователя
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MapFragment.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getLocation() {
        // Проверяем, есть ли разрешение на использование местоположения
        if (ActivityCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешение предоставлено, получаем провайдер местоположения
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Получаем последнее известное местоположение
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            // Обрабатываем полученные координаты
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.latitude
                longitude = lastKnownLocation.longitude
                // Теперь у вас есть широта и долгота
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
            } else {
                // Местоположение неизвестно или недоступно
                Log.e("Location", "Unable to retrieve location.")
            }
        } else {
            // Разрешение не предоставлено, обработайте этот случай
            Log.e("Location", "Location permission not granted.")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение предоставлено, получаем координаты
                    Debug.getLocation()
                } else {
                    // Пользователь отказал в предоставлении разрешения
                    Toast.makeText(
                        requireContext(),
                        "Разрешение на местоположение не предоставлено",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun setMyPositionMark() {
        val placeMark = binding.mapview.map.mapObjects.addPlacemark().apply {
            val imageProvider = ImageProvider.fromResource(requireActivity(),R.drawable.pos)
            imageProvider.image.scale(30, 30, false)
            geometry = Point(latitude, longitude)
            setIcon(imageProvider)
        }
    }

    private fun drawMarker(point: Point) {
        val placeMark = binding.mapview.map.mapObjects.addPlacemark().apply {
            val imageProvider = ImageProvider.fromResource(
                requireActivity(),
                R.drawable.placemark_icon
            )
            geometry = Point(point.latitude, point.longitude)
            setIcon(imageProvider)
        }
    }


    private fun setMark(point: Point) {
        val placeMark = binding.mapview.map.mapObjects.addPlacemark().apply {
            val imageProvider = ImageProvider.fromResource(requireActivity(),
                R.drawable.placemark_icon
            )
            geometry = Point(point.latitude, point.longitude)
            setIcon(imageProvider)
            viewModel.addMark(Marker(geometry, "Point ${viewModel.getSize()}", ""))
        }
    }

    private fun moveMapToCurrentLocation() {
        binding.mapview.map.move(
            CameraPosition(
                Point(latitude, longitude),
                /* zoom = */ 17.0f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 30.0f
            )
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onMapTap(p0: Map, p1: Point) {
        Toast.makeText(requireContext(), "TAP", Toast.LENGTH_SHORT).show()
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        setMark(p1)
        Toast.makeText(requireContext(), "LONG TAP", Toast.LENGTH_SHORT).show()
    }

    override fun onObjectTap(p0: GeoObjectTapEvent): Boolean {
        Toast.makeText(requireContext(), "GEO OBJECT TAP", Toast.LENGTH_SHORT).show()
        return true
    }
}