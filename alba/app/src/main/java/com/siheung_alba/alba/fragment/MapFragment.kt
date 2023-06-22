package com.siheung_alba.alba.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.DetailActivity
import com.siheung_alba.alba.adapter.JobAdapter
import com.siheung_alba.alba.model.JobModel


class MapFragment : Fragment() {

    private val db = Firebase.firestore
    private val colStoreRef = db.collection("store")
    private val colJobRef = db.collection("job" )


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

    //초기 위치를 설정하는 코드 - 한국공학대학교로 설정
    private val setCallback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        val latLng = LatLng(37.340, 126.733)
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(14f)
            .build()

        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        var count = 0

        colStoreRef // 위도 경도 가져오기
            .get()
            .addOnSuccessListener { storeDocuments->
                for(storeDocument in storeDocuments) {

                    colJobRef
                        .get()
                        .addOnSuccessListener { jobDocuments ->
                            for (jobDocument in jobDocuments) {

                                if (storeDocument.data["email"].toString() == jobDocument.data["email"].toString()) {
                                    count++
                                    var location = LatLng(
                                        storeDocument.data["latitude"].toString().toDouble(),
                                        storeDocument.data["longitude"].toString().toDouble()
                                    )

                                    val markerOptions = MarkerOptions() // 핀
                                    markerOptions.title(jobDocument["title"].toString()) // 이름
                                    markerOptions.position(location) // 위치
                                    markerOptions.icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_ROSE
                                        )
                                    )

                                    val marker = gMap.addMarker(markerOptions) // 핀 추가 및 마커 생성

                                    marker.tag =
                                        jobDocument.data?.get("updated_at").toString() + "^" + //업데이트 날짜
                                                jobDocument.data?.get("add_text").toString() + "^" + // 설명
                                                jobDocument.data?.get("term").toString() + "^" + // 기간
                                                jobDocument.data?.get("money").toString() + "^" + // 시급
                                                jobDocument.data?.get("sex").toString() + "^" + // 성별
                                                jobDocument.data?.get("nation").toString() + "^" +  // 국적
                                                jobDocument.data?.get("email").toString() // 채용자 이메일
                                    Log.d(
                                        ContentValues.TAG,
                                        "${storeDocument.id} => ${storeDocument.data}"
                                    )

                                    if (storeDocuments.size() <= count) //마지막 핀으로 카메라 이동
                                        gMap.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                location,
                                                16F
                                            )
                                        )
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w("mapPoint", "Error getting jobDocuments", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("mapPoint", "Error getting storeDocuments.", exception)
            }

        val jobFrame = view?.findViewById<FrameLayout>(R.id.job_frame)
        val applyToBtn = view?.findViewById<Button>(R.id.applyToBtn)
        gMap!!.setOnMarkerClickListener ( object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                jobFrame?.visibility = View.VISIBLE
                var name = requireView().findViewById<TextView>(R.id.store_name) // 매장 이름
                var updateAt = requireView().findViewById<TextView>(R.id.update_at) // 생성 날짜
                var addDescription = requireView().findViewById<TextView>(R.id.store_text) // 설명
                var term = requireView().findViewById<TextView>(R.id.work_term) // 근무 기간
                var money = requireView().findViewById<TextView>(R.id.money) // 시급
                var sex = requireView().findViewById<TextView>(R.id.sexCondition) // 성별 조건
                var nation = requireView().findViewById<TextView>(R.id.nationCondition) // 국적 조건

                var arr = marker.tag.toString().split("^") //마커에 붙인 태그를 /로 나눔

                name.text = marker.title
                updateAt.text = arr[0]
                addDescription.text = arr[1]
                term.text = arr[2]
                money.text = arr[3]
                sex.text = arr[4]
                nation.text = arr[5]
                val ownerEmail : String = arr[6]

                applyToBtn?.setOnClickListener {

                    db.collection("job")
                        .whereEqualTo("email", ownerEmail)
                        .get()
                        .addOnSuccessListener { documents ->
                            for(document in documents) {
                                val context = requireContext()
                                val intent = Intent(context, DetailActivity::class.java)

                                intent.putExtra("title", document.data["title"].toString())
                                intent.putExtra("addtext", document.data["add_text"].toString())
                                intent.putExtra("jobTerm", document.data["term"].toString())
                                intent.putExtra("money", document.data["money"].toString())
                                intent.putExtra("sex", document.data["sex"].toString())
                                intent.putExtra("nation", document.data["nation"].toString())
                                intent.putExtra("update", document.data["updated_at"].toString())
                                intent.putExtra("age", document.data["age"].toString())
                                intent.putExtra("extratext", document.data["extra_text"].toString())
                                intent.putExtra("jobId", document.data["job_id"].toString())
                                intent.putExtra("workday", document.data["work_day"].toString())
                                intent.putExtra("worktime", document.data["work_time"].toString())
                                intent.putExtra("preference", document.data["preference"].toString())
                                intent.putExtra("education", document.data["education"].toString())
                                intent.putExtra("ownerName", document.data["owner_name"].toString())
                                intent.putExtra("ownerPhone", document.data["owner_phone"].toString())
                                intent.putExtra("ownerEmail", document.data["email"].toString())

                                context.startActivity(intent)
                            }

                        }
                }

                return false
            }
        })
        gMap!!.setOnMapClickListener { jobFrame?.visibility = View.GONE }
    }

    private fun initLocation() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
            ?: SupportMapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(setCallback)
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
            .title("내 위치") // 마커의 제목 설정

        val LATLNG = LatLng(myLocation.latitude, myLocation.longitude)
        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()

        gMap.clear()
        gMap.addMarker(markerOptions)
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val jobFrame = view.findViewById<FrameLayout>(R.id.job_frame)
        jobFrame.visibility = View.GONE

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 화면 설정 함수 - 한국공학대학교로 설정함
        initLocation()

        // 현재 위치 설정 버튼을 누르면 현채 위치로
        val curLocationBtn = view.findViewById<ImageButton>(R.id.curLocationBtn)
        curLocationBtn.setOnClickListener{
            if(isPermitted()) {
                startProcess()
            }else {
                ActivityCompat.requestPermissions(requireActivity(), permissions, permission_request)
            }
        }

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


