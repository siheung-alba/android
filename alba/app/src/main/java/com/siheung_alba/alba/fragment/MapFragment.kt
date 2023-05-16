package com.siheung_alba.alba.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityMainBinding
import com.siheung_alba.alba.databinding.FragmentMapBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var mapView: SupportMapFragment? = null
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mapView = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val location = LatLng(37.5665, 126.9780)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }

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