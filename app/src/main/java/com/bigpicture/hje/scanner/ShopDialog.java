package com.bigpicture.hje.scanner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Network.NetworkTask;
import com.VO.Shop;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-11.
 */

public class ShopDialog extends Dialog {


    private EditText edittext_shop_name;
    private EditText edittext_shop_info;
    private Spinner spinner_type;
    private Spinner spinner_vendor;
    private Button button_send;

    private Shop shop;
    private String shop_type;
    private String shop_vendor;
    private double lat,lng;
    private Dialog dialog;
    private Context context;

    public ShopDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_shop_info);
        dialog = this;

        edittext_shop_name = (EditText) findViewById(R.id.edittext_shop_name);
        edittext_shop_info = (EditText) findViewById(R.id.edittext_shop_info);
        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        spinner_vendor = (Spinner) findViewById(R.id.spinner_vendor);
        button_send = (Button) findViewById(R.id.button_send);

        spinner_type.setSelection(0);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setVendorSpinner(position);
                shop_type = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //마지막 Shop 전송 버튼 눌렀을때
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edittext_shop_name.getText().toString();

                if (name == null || shop_type == null || shop_vendor == null)
                    Toast.makeText(getContext(), "전송 불가 : Shop 입력 정보 확인", Toast.LENGTH_LONG).show();
                else {
                    shop = new Shop(name, lat, lng, shop_type, edittext_shop_info.getText().toString(), shop_vendor);

                    NetworkTask networkTask = new NetworkTask(context);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("shop_name", name);
                    params.put("shop_lat", String.valueOf(shop.getShop_lat()));
                    params.put("shop_lng", String.valueOf(shop.getShop_lng()));
                    params.put("shop_type", shop.getShop_type());
                    params.put("shop_info", shop.getShop_info());
                    params.put("shop_vendor", shop.getShop_vendor());

                    networkTask.execute(params);

                    dialog.dismiss();
                }
            }
        });
    }

    private void setVendorSpinner(int position){
        Log.i("setVendorSpinner", "position : "+position);
        int array = 0;
        switch (position){
            case 0: array = R.array.convenience_vendor; break;
            case 1: array = R.array.pc_vendor; break;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_vendor.setAdapter(adapter);

        spinner_vendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shop_vendor = (String)parent.getItemAtPosition(position);
                edittext_shop_name.setText(shop_vendor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void setLatLng(Double lat, Double lng){
        this.lat = lat;
        this.lng = lng;
    }

}
