package com.cchaegog.chaegog;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationSource mLocationSource;
    private NaverMap mNaverMap;
    double lat, lnt;
    String mapInfoName, mapInfoAddr, mapInfoTime, mapInfoDayoff,
            mapInfoImage, mapInfoCategory, mapInfoMenu, mapInfoPhonenum, mapInfoKey;
    TextView getMapInfoName, getMapInfoAddr, getMapInfoTime, getMapInfoDayoff, mapInfoDayoffTv;
    ImageView getMapInfoImage, mapSearchImage;
    ImageButton mapInfoButton;
    FloatingActionButton mapSearchBtn;
    LinearLayout mapInfoLayout, mapSearchLayout;
    View map_fragment;
    private DatabaseReference mDatabase;
    private ArrayList<String> mapNameList = new ArrayList<>();
    private ArrayList<String> mapAddrList = new ArrayList<>();
    private ArrayList<String> mapTimeList = new ArrayList<>();
    private ArrayList<String> mapDayoffList = new ArrayList<>();
    private ArrayList<String> mapImageList = new ArrayList<>();
    private ArrayList<String> mapLatList = new ArrayList<>();
    private ArrayList<String> mapLntList = new ArrayList<>();
    private ArrayList<String> mapMenuList = new ArrayList<>();
    private ArrayList<String> mapCategoryList = new ArrayList<>();
    private ArrayList<String> mapPhonenumList = new ArrayList<>();
    private ArrayList<String> mapItmeKeyList = new ArrayList<>();
    ArrayList<Integer> distance;

    double myLongitude, myLatitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getMapInfoName = findViewById(R.id.map_info_name);
        getMapInfoAddr = findViewById(R.id.map_info_addr);
        getMapInfoTime = findViewById(R.id.map_info_time);
        getMapInfoDayoff = findViewById(R.id.map_info_day_off);
        getMapInfoImage = findViewById(R.id.map_info_image);
        mapInfoButton = findViewById(R.id.map_info_button);
        mapInfoDayoffTv = findViewById(R.id.map_info_day_off_tv);

//        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(location != null) {
//            myLatitude = location.getLatitude();
//            myLongitude = location.getLongitude();
//        }
//
//        FragmentManager fm = getSupportFragmentManager();
//        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
//        if (mapFragment == null) {
//            mapFragment = MapFragment.newInstance();
//            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
//        }
//
//        mapFragment.getMapAsync(this);
//
//        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            myLongitude = location.getLongitude();
            myLatitude = location.getLatitude();
        }

        }

        @Override
        public void onMapReady (@NonNull NaverMap naverMap){
            Log.d(TAG, "onMapReady");

//            mDatabase = FirebaseDatabase.getInstance().getReference("Maps");
//            String targetCategory = "한식";
//            Query query = mDatabase.orderByChild("storeCategory").equalTo(targetCategory);
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
//                        // 조회한 각 항목의 image 필드 수정
//                        itemSnapshot.getRef().child("storeImage").setValue("new_image_url");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    System.out.println("데이터 조회 실패: " + databaseError.getMessage());
//                }
//            });

            mapSearchBtn = findViewById(R.id.map_search_btn);
            mapSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MapActivity.this, MapSearchActivity.class);
                    startActivity(intent);
                }
            });

            mapInfoLayout = findViewById(R.id.map_info_layout);
            map_fragment = findViewById(R.id.map_fragment);

            mDatabase = FirebaseDatabase.getInstance().getReference("Maps");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        mapNameList.add(snapshot.child("storeName").getValue().toString());
                        mapAddrList.add(snapshot.child("storeAddr").getValue().toString());
                        mapTimeList.add(snapshot.child("storeTime").getValue().toString());
                        mapDayoffList.add(snapshot.child("storeDayOff").getValue().toString());
                        mapImageList.add(snapshot.child("storeImage").getValue().toString());
                        mapLatList.add(snapshot.child("storeLat").getValue().toString());
                        mapLntList.add(snapshot.child("storeLnt").getValue().toString());
                        mapMenuList.add(snapshot.child("storeMenu").getValue().toString());
                        mapCategoryList.add(snapshot.child("storeCategory").getValue().toString());
                        mapPhonenumList.add(snapshot.child("storePhonenum").getValue().toString());
                        mapItmeKeyList.add(snapshot.getKey());

                    }
                    for(int i=0; i < mapNameList.size(); i++){
                        Log.d("map_list", mapNameList.toString());
                        Marker[] markers = new Marker[mapNameList.size()];

                        markers[i] = new Marker();
                        lat = Double.parseDouble(mapLatList.get(i));
                        lnt = Double.parseDouble(mapLntList.get(i));
//                        markers[i].setPosition(new LatLng(lat, lnt));
//                        markers[i].setCaptionText(mapNameList.get(i));
//                        markers[i].setMap(naverMap);
                        Log.d("my_location", myLatitude + " , " + myLongitude);

                        distance = new ArrayList<Integer>();
                        distance.add(getDistance(myLatitude ,myLongitude, lat, lnt));
                        for(int k=0; k<distance.size(); k++) {
                            if(distance.get(k) <= 1000) {
                                Log.d("map_distance", distance.get(k) + "m - " + mapNameList.get(i));
                                markers[i].setPosition(new LatLng(lat, lnt));
                                markers[i].setCaptionText(mapNameList.get(i));
                                markers[i].setMap(naverMap);
                            }
                        }

                        int k = i;
                        markers[i].setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay)
                            {
                                // DB에서 차례대로 정보 받아오기
                                mapInfoName = mapNameList.get(k);
                                mapInfoAddr = mapAddrList.get(k);
                                mapInfoTime = mapTimeList.get(k);
                                mapInfoDayoff = mapDayoffList.get(k);
                                mapInfoImage = mapImageList.get(k);
                                mapInfoCategory = mapCategoryList.get(k);
                                mapInfoMenu = mapMenuList.get(k);
                                mapInfoPhonenum = mapPhonenumList.get(k);
                                mapInfoKey = mapItmeKeyList.get(k);

                                mapInfoButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(MapActivity.this, MapInfoActivity.class);
                                        intent.putExtra("name", mapInfoName);
                                        intent.putExtra("addr", mapInfoAddr);
                                        intent.putExtra("time", mapInfoTime);
                                        intent.putExtra("dayOff", mapInfoDayoff);
                                        intent.putExtra("image", mapInfoImage);
                                        intent.putExtra("category", mapInfoCategory);
                                        intent.putExtra("menu", mapInfoMenu);
                                        intent.putExtra("phone", mapInfoPhonenum);
                                        intent.putExtra("key", mapInfoKey);
                                        startActivity(intent);

                                    }
                                });

                                // 받아온 데이터로 TextView 내용 변경
                                getMapInfoName.setText(mapInfoName);
                                getMapInfoAddr.setText(mapInfoAddr);
                                getMapInfoTime.setText(mapInfoTime);

                                if(mapInfoDayoff.trim().isEmpty()) {
                                    mapInfoDayoffTv.setVisibility(View.GONE);
                                } else {
                                    mapInfoDayoffTv.setVisibility(View.VISIBLE);
                                }
                                getMapInfoDayoff.setText(mapInfoDayoff);

                                startLoadingImage();

                                // visibility가 gone으로 되어있던 정보창 레이아웃을 visible로 변경
                                mapInfoLayout.setVisibility(View.VISIBLE);

                                map_fragment.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        mapInfoLayout.setVisibility(View.GONE);
                                        return false;
                                    }
                                });

                                return false;

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


            // NaverMap 객체를 받아 NaverMap 객체에 위치 소스 지정
            mNaverMap = naverMap;
            mNaverMap.setLocationSource(mLocationSource);

            UiSettings uiSettings = mNaverMap.getUiSettings();
            uiSettings.setLocationButtonEnabled(true);

            // 권한 확인, onRequestPermissionsResult 콜백 메서드 호출
            ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUEST_CODE);
        }

        // 이미지 가져오기
        private void startLoadingImage() {
            Glide.with(this)
                    .load(mapInfoImage)
                    .override(400,400)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(20)))
                    .into(getMapInfoImage);
        }

        private int getDistance(double lat1, double lnt1, double lat2, double lnt2) {
            double R = 6372.8 * 1000;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLnt = Math.toRadians(lnt2 - lnt1);
            double a = sin(dLat/2) * sin(dLat/2)+ cos(Math.toRadians(lat1))* cos(Math.toRadians(lat2))* sin(dLnt/2)* sin(dLnt/2);
            double c = 2 * Math.asin(Math.sqrt(a));
            return (int) (R * c);
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//            if (mLocationSource.onRequestPermissionsResult(
//                    requestCode, permissions, grantResults)) {
//                if (!mLocationSource.isActivated()) { // 권한 거부됨
//                    mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//                }
//                return;
//            }
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                }
            }
        }
    }