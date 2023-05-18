package com.siheung_alba.alba.activity

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityMapsBinding

@Suppress("deprecation")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val permission_request = 99
    private lateinit var mMap: GoogleMap
    var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //권한 확인
        if(isPermitted()) {
            startProcess()
        } else {
            ActivityCompat.requestPermissions(this, permissions, permission_request)
        }
    }

    // 권한 허락 받기
    private fun isPermitted():Boolean {
        for(perm in permissions) {
            if(ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // 권한이 있으면 onMapReady 연결
    private fun startProcess() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment?
            ?: SupportMapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            } //권한
        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val marker = LatLng(37.340, 126.733)
        mMap.addMarker(MarkerOptions().position(marker).title("한국공학대학교"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this) //gps 자동으로 받아오기
        setUpdateLocationListner()
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient //자동으로 gps값을 받아온다.
    lateinit var locationCallback: LocationCallback //gps 응답 값을 가져온다.


    @Suppress("MissingPermission")
    fun setUpdateLocationListner() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY //높은 정확도
            interval = 1000 //1초에 한번씩 GPS 요청
        }

        //location 요청 함수 호출
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for ((i, location) in locationResult.locations.withIndex()) {
                    android.util.Log.d("location: ", "${location.latitude}, ${location.longitude}")
                    setLastLocation(location)
                }
            }
        }

        //좌표계를 주기적으로 갱신
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }
    fun setLastLocation(location: Location) {
        val myLocation = LatLng(location.latitude, location.longitude)
        val markerOptions  = MarkerOptions()
            .position(myLocation)
            .title("Marker title") // 마커의 제목 설정
            .snippet("Marker Snippet") // 마커의 설명 설정

        val LATLNG = LatLng(myLocation.latitude, myLocation.longitude)
        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()

        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}