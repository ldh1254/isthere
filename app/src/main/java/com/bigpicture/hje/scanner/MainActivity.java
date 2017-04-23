package com.bigpicture.hje.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //context 액티비티랑 같은 거라고 보면됨
    private Context context;
    private StockDialog dialog;

    //Override는 extends AppCompatActivity 상속했을때 꼭 구현되어야 하는 함수로 자동 추가됨
    //모든 activity 코드는 onCreate함수에서 시작 C의 main함수라고 보면됨
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new StockDialog(this);


        //R은 Resource 의미 (각종 xml파일부터 이미지 파일등 R안에 관리됨), 메인화면 배포자?(activity_main.xml파일)를 여기 코드에서 관리하겠다
        setContentView(R.layout.activity_main);
        context = this;

        //activity_main.xml 의 button_shop이라는 id의 버튼을 가져옴
        Button button_shop = (Button) findViewById(R.id.button_shop);

        //버튼 눌렀을때 이벤트 처리
        button_shop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent는 현재 화면에서 특정 지시를 Broadcasting,  여기서는 화면 전환
                Intent intent = new Intent(context, MapsActivity.class);
                //지도 액티비티로 이동
                startActivity(intent);
            }
        });


        Button button_stock = (Button) findViewById(R.id.button_stock);
        button_stock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dia) {
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dia) {
                    }
                });
                dialog.show();
            }
        });

        Button button_item = (Button) findViewById(R.id.button_item);
        button_item.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //바코드 화면에서 카메라 써야되는데 그전에 써도되는지 허락 받는거
                //사용자가 이 앱에서 카메라 사용을 허락했는지 확인
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //쓴다고 했으면 zxing의 바코드 촬영 화면으로 전환
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    //위 지도맵 이동과 다르게 명령을 날릴때 아래와 같이 key-value 형식으로 값을 싸서 보낼수있음
                    intent.putExtra("SCAN_MODE", "ALL");
                    startActivityForResult(intent, 0);
                } else {
                    //사용자한테 카메라 쓸거냐고 물어보는거임
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
            //정상적으로 바코드 값을 가져왔으면
            if(resultCode == Activity.RESULT_OK)
            {
                //되돌아온 Intent에서 key를 통해 value를 가져옴(바코드 숫자)
                String contents = data.getStringExtra("SCAN_RESULT");
                //토스트라고 화면하단에 잠깐 보여주고 없어지는거. 바코드 숫자 확인. 간단히 무슨 값 확인할때 좋음
                Toast.makeText (context, contents, Toast.LENGTH_LONG).show();

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
