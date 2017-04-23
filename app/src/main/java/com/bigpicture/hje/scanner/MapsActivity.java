package com.bigpicture.hje.scanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; //지도 자체에 대한 객체
    private MarkerOptions marker; //마커라고 화면에 동그란거 뜨는거
    private LatLng latlng; //위도, 경도 값을 가진 구조체 정도로 보면됨
    private Double lat= 37.491,lng=127.020;
    private ShopDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //마커 정의
        marker = new MarkerOptions();
        //위도, 경도 (해커톤 장소)
        latlng = new LatLng( lat,lng);

        dialog = new ShopDialog(this);
    }

    //맵이 활성화 되면 자동 호출
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        marker.position(latlng).title("Select shop position");
        mMap.addMarker(marker);
        //해커톤 행사장으로 이동, 지도 확대를 15로
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(latlng, 15) );

        //맵 터치했을때 위,경도 토스트로 띄워주고 마커 위치 바꿔줌
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            public void onMapClick(LatLng point) {
                lat = point.latitude;
                lng = point.longitude;
                String text = "위도 =" + point.latitude + ", 경도 ="+ point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                //기존 마커 삭제
                mMap.clear();
                marker.position(point);
                mMap.addMarker(marker);
            }
        });

        //마커 눌렀을때 점포등록을 위한 추가 정보 입력 다이얼로그를 띄워야함
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            public boolean onMarkerClick(Marker marker) {
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dia) {
                        //dialog가 보여질때 초기값 셋팅
                        dialog.setLatLng(lat,lng);
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dia) {
                        //dialog가 사라질때 액션
                    }
                });
                dialog.show();
                return false;
            }
        });
    }
}
