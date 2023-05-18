package com.siheung_alba.alba.fragment

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityMainBinding
import com.siheung_alba.alba.databinding.FragmentMapBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    private lateinit var gMap: GoogleMap
    private var mapView: SupportMapFragment? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient //자동으로 gps값을 받아온다.
    private lateinit var locationCallback: LocationCallback //gps 응답 값을 가져온다.
    var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val permission_request = 99

    private val callback = OnMapReadyCallback {googleMap ->
        gMap = googleMap
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity()) //gps 자동으로 받아오기
        setUpdateLocationListner()

    }
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

        gMap.clear()
        gMap.addMarker(markerOptions)
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
    // 권한 허락 받기
    private fun isPermitted():Boolean {
        for(perm in permissions) {
            if(ContextCompat.checkSelfPermission(requireContext(), perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // 권한이 있으면 onMapReady 연결
    private fun startProcess() {
        val fm = childFragmentManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
            ?: SupportMapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            } //권한
        mapFragment.getMapAsync(callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

//        mapView = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
//        mapView?.onCreate(savedInstanceState)
//        mapView?.getMapAsync(this)

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isPermitted()) {
            startProcess()
        }else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, permission_request)
        }
    }

//    override fun onMapReady(map: GoogleMap) {
//        googleMap = map
//
//        val location = LatLng(37.5665, 126.9780)
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
//    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

}