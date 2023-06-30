package com.example.finalprojectvegan;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.Objects;

public class MapButtonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationSource mLocationSource;
    private NaverMap mNaverMap;
    private DatabaseReference mDatabase;
    private View map_btn_fragment;
    private String name, addr, key;
    private double myLongitude, myLatitude;
    private ArrayList<String> mapLatList = new ArrayList<>();
    private ArrayList<String> mapLntList = new ArrayList<>();
    private ArrayList<String> mapItmeKeyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_button);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapBtnFragment = (MapFragment) fm.findFragmentById(R.id.map_button_fragment);
        if (mapBtnFragment == null) {
            mapBtnFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_button_fragment, mapBtnFragment).commit();
        }

        mapBtnFragment.getMapAsync(this);
        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady (@NonNull NaverMap naverMap){
        Log.d(TAG, "onMapReady");

        map_btn_fragment = findViewById(R.id.map_button_fragment);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        addr = intent.getStringExtra("addr");
        key = intent.getStringExtra("key");

        //
        mDatabase = FirebaseDatabase.getInstance().getReference("Maps");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mapLatList.add(Objects.requireNonNull(snapshot.child("storeLat").getValue()).toString());
                    mapLntList.add(Objects.requireNonNull(snapshot.child("storeLnt").getValue()).toString());
                    mapItmeKeyList.add(snapshot.getKey());

                }
                for(int i=0; i < mapItmeKeyList.size(); i++){
                    if(mapItmeKeyList.get(i).equals(key)) {
                        // 지도에 마커 표시
                        myLatitude = Double.parseDouble(mapLatList.get(i));
                        myLongitude = Double.parseDouble(mapLntList.get(i));
                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(myLatitude, myLongitude));
                        marker.setCaptionText(name);
                        marker.setMap(naverMap);

                        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(myLatitude, myLongitude));
                        naverMap.moveCamera(cameraUpdate);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // NaverMap 객체에 위치 소스 지정
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);

        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        // 권한 확인, onRequestPermissionsResult 콜백 메서드 호출
        ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 권한 획득 여부 확인
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
        }
    }
}