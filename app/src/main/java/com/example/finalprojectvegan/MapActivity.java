package com.example.finalprojectvegan;

import static java.lang.Math.round;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
        private static final String TAG = "MapActivity";
        private static final int PERMISSION_REQUEST_CODE = 100;
        private static final String[] PERMISSION = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        private FusedLocationSource mLocationSource;
        private NaverMap mNaverMap;

        private NaverMapItem naverMapList;
        private List<NaverMapData> naverMapInfo;

        double lat;
        double lnt;
        String mapInfoName;
        String mapInfoAddr;

        TextView getMapInfoName;
        TextView getMapInfoAddr;
        LinearLayout mapInfoLayout;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);


            FragmentManager fm = getSupportFragmentManager();
            MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance();
                fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
            }

            mapFragment.getMapAsync(this);

            mLocationSource =
                    new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        }

        @Override
        public void onMapReady (@NonNull NaverMap naverMap){
            Log.d(TAG, "onMapReady");

            mapInfoLayout = findViewById(R.id.map_info_layout);

            NaverMapApiInterface naverMapApiInterface = NaverMapRequest.getClient().create(NaverMapApiInterface.class);
            Call<NaverMapItem> call = naverMapApiInterface.getMapData();
            call.enqueue(new Callback<NaverMapItem>() {
                             @Override
                             public void onResponse(Call<NaverMapItem> call, Response<NaverMapItem> response) {
                                 naverMapList = response.body();
                                 naverMapInfo = naverMapList.MAPSTOREINFO;

                                 // 식당 정보가 출력되는 곳
                                 getMapInfoName = findViewById(R.id.map_info_name);
                                 getMapInfoAddr = findViewById(R.id.map_info_addr);

                                 // 마커 여러개 찍기
                                 for(int i=0; i < naverMapInfo.size(); i++){
                                     Marker[] markers = new Marker[naverMapInfo.size()];

                                     markers[i] = new Marker();
                                     lat = naverMapInfo.get(i).getStoreLat();
                                     lnt = naverMapInfo.get(i).getStoreLnt();
                                     markers[i].setPosition(new LatLng(lat, lnt));
                                     markers[i].setCaptionText(naverMapInfo.get(i).getStoreName());
                                     markers[i].setMap(naverMap);

                                     int finalI = i;
                                     markers[i].setOnClickListener(new Overlay.OnClickListener() {
                                         @Override
                                         public boolean onClick(@NonNull Overlay overlay)
                                         {
                                             // DB에서 차례대로 정보 받아오기
                                             mapInfoName = naverMapInfo.get(finalI).getStoreName();
                                             mapInfoAddr = naverMapInfo.get(finalI).getStoreAddr();

                                             // 받아온 데이터로 TextView 내용 변경
                                             getMapInfoName.setText(mapInfoName);
                                            getMapInfoAddr.setText(mapInfoAddr);

                                            // visibility가 gone으로 되어있던 정보창 레이아웃을 visible로 변경
                                             mapInfoLayout.setVisibility(View.VISIBLE);
                                             return false;

                                         }
                                     });

                                 }
                             }

                             @Override
                             public void onFailure(Call<NaverMapItem> call, Throwable t) {

                             }
                         });


            // NaverMap 객체를 받아 NaverMap 객체에 위치 소스 지정
            mNaverMap = naverMap;
            mNaverMap.setLocationSource(mLocationSource);

            // 권한 확인, onRequestPermissionsResult 콜백 메서드 호출
            ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUEST_CODE);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            // request code와 권한획득 여부 확인
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                }
            }
        }
    }