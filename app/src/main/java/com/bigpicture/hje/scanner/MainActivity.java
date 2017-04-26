package com.bigpicture.hje.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.common.Scan.Pref_ScanShop;
import static com.common.Scan.Pref_ShopID;
import static com.common.Scan.Pref_ShopName;

public class MainActivity extends AppCompatActivity {
    //context 액티비티랑 같은 거라고 보면됨
    private Context context;
    private StockDialog stockDialog;
    private Button button_in, button_out, button_shop;
    private String shopID, itemCode;

    //Override는 extends AppCompatActivity 상속했을때 꼭 구현되어야 하는 함수로 자동 추가됨
    //모든 activity 코드는 onCreate함수에서 시작 C의 main함수라고 보면됨
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        button_shop = (Button) findViewById(R.id.button_shop);
        button_shop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent는 현재 화면에서 특정 지시를 Broadcasting,  여기서는 화면 전환
                Intent intent = new Intent(context, MapsActivity.class);
                //지도 액티비티로 이동
                startActivity(intent);
            }
        });

        button_in = (Button) findViewById(R.id.button_in);
        button_in.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if(shopID.length() != 0) {
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        intent.putExtra("SCAN_MODE", "ALL");
                        startActivityForResult(intent, 0);
                    }else
                        Toast.makeText (context, "스캐너의 상점 등록이 필요합니다.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 0);
                }
            }
        });

        button_out = (Button) findViewById(R.id.button_out);
        button_out.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if(shopID.length() != 0) {
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        intent.putExtra("SCAN_MODE", "ALL");
                        startActivityForResult(intent, 1);
                    }else
                        Toast.makeText (context, "스캐너의 상점 등록이 필요합니다.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 0);
                }
            }
        });
    }

    //바코드를 찍고 바코드 값 추출 후 다시 메인화면으로 돌아오면서 바코드 값을 돌려줌
    //메인화면으로 다시 돌아왔을때 아래 함수 자동으로 호출 됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0) {
            if(resultCode == Activity.RESULT_OK)
            {
                itemCode = data.getStringExtra("SCAN_RESULT");
                stockDialog = new StockDialog(context, 0, shopID, itemCode);
                stockDialog.show();
            }
        } else if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK)
            {
                itemCode = data.getStringExtra("SCAN_RESULT");
                stockDialog = new StockDialog(context, 1, shopID, itemCode);
                stockDialog.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences pref =getSharedPreferences(Pref_ScanShop, MODE_PRIVATE);
        String shopName = pref.getString(Pref_ShopName, "");
        shopID =  pref.getString(Pref_ShopID, "");
        setTitle("Scanner ("+ shopName +" "+shopID+ ")");
    }
}
