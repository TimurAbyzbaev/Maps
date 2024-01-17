package ru.abyzbaev.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.graphics.scale
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.abyzbaev.maps.databinding.ActivityMainBinding
import java.security.Permission

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // 55.7522, 37.6156 Москва
    private var latitude = 55.7522
    private var longitude = 37.6156

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        MapKitFactory.initialize(this)

        setContentView(binding.root)

        requestLocationPermission()
        Log.d("####","lat = $latitude, lon = $longitude")
        moveMapToCurrentLocation()
        setMyPositionMark()
    }


    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешение на местоположение уже предоставлено, получаем координаты
            getLocation()
        } else {
            // Запрашиваем разрешение у пользователя
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getLocation() {
        // Проверяем, есть ли разрешение на использование местоположения
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешение предоставлено, получаем провайдер местоположения
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

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
                    getLocation()
                } else {
                    // Пользователь отказал в предоставлении разрешения
                    Toast.makeText(
                        this,
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
        val placemark = binding.mapview.map.mapObjects.addPlacemark().apply {
            val imageProvider = ImageProvider.fromResource(this@MainActivity,R.drawable.pos)
            imageProvider.image.scale(30, 30, false)
            geometry = Point(latitude, longitude)
            setIcon(imageProvider)
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE  = 123 // Вы можете выбрать любой уникальный код
    }
}