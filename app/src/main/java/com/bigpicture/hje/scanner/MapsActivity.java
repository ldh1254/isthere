package com.bigpicture.hje.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.Network.NetworkTask;
import com.VO.Shop;
import com.common.Scan;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.common.Scan.BR_ShopList;
import static com.common.Scan.HTTP_ACTION_SHOPLIST;
import static com.common.Scan.KEY_ShopList;
import static com.common.Scan.Pref_ScanShop;
import static com.common.Scan.Pref_ShopID;
import static com.common.Scan.Pref_ShopName;
import static com.common.Scan.lat;
import static com.common.Scan.lng;
import static com.common.Scan.scanDist;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; //지도 자체에 대한 객체
    private LatLng latlng; //위도, 경도 값을 가진 구조체 정도로 보면됨
    private ArrayList<Shop> shops;
    private ArrayList<MarkerOptions> markers;
    private Context context;
    private String scanCode;
    private String selectedShopID, selectedShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context = this;
        latlng = new LatLng( lat,lng);
    }

    //서버에서 Shop 리스트 목록을 리시버로 받음 (NetworkTask)
    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BR_ShopList);
        registerReceiver(mShopListBR, filter);
    }
    public void onPause(){
        super.onPause();
        unregisterReceiver(mShopListBR);
    }
    //리시버는 항상 해제 해줘야함

    BroadcastReceiver mShopListBR = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent){
            try {
                mMap.clear();
                shops = new ArrayList<Shop>();
                markers = new ArrayList<MarkerOptions>();
                JSONArray jArray = new JSONArray(intent.getStringExtra(KEY_ShopList));
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject oneObject = jArray.getJSONObject(i); // Pulling items from the array
                    String shop_id = oneObject.getString("shop_id");
                    String shop_name = oneObject.getString("shop_name");
                    Double marker_lat = oneObject.getDouble("shop_lat");
                    Double marker_lng = oneObject.getDouble("shop_lng");
                    int shop_distance = (int) (oneObject.getDouble("distance") * 1000.0d);
                    Shop shop = new Shop(shop_id, shop_name, marker_lat, marker_lng, oneObject.getString("shop_type"),
                            new Date(oneObject.getInt("shop_time")), oneObject.getString("shop_info"), oneObject.getString("shop_vendor"),shop_distance);
                    shops.add(shop);
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(marker_lat, marker_lng)).title(shop_id).snippet(shop_name);
                    markers.add(marker);
                    Marker location = mMap.addMarker(marker);
                }
            }catch(JSONException e){
                Log.e("JSON Parsing error", e.toString());
            }
        }
    };

    private void getShopList(){
        NetworkTask networkTask = new NetworkTask(context, HTTP_ACTION_SHOPLIST, Scan.shopScan);
        Map<String, String> params = new HashMap<String, String>();
        params.put("dist", String.valueOf(scanDist));
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        networkTask.execute(params);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getShopList();
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

                getShopList();
            }
        });


        //마커 눌렀을때 점포등록을 위한 추가 정보 입력 다이얼로그를 띄워야함
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            public boolean onMarkerClick(Marker marker) {

                selectedShopID = marker.getTitle();
                selectedShopName = marker.getSnippet();
                SharedPreferences pref =getSharedPreferences(Pref_ScanShop, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Pref_ShopID,selectedShopID ); //키값, 저장값
                editor.putString(Pref_ShopName,selectedShopName ); //키값, 저장값
                editor.commit();
                Log.d("Shop 스캐너 등록", "ID : "+selectedShopID + " Name : "+ selectedShopName);
                Toast.makeText(getApplicationContext(), selectedShopName + " 상점으로 스캐너를 사용합니다.", Toast.LENGTH_LONG).show();
                finish();
                return true;
            }
        });

    }
}
