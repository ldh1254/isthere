package com.bigpicture.hje.scanner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.Network.NetworkTask_stock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-11.
 */

public class StockDialog extends Dialog {


    private Spinner spinner_item;
    private Button button_send;

    private String shop_id,item_code,stock_stock;
    private int type; //0 : in 1: out
    private Dialog dialog;
    private Context context;

    public StockDialog(Context context, int type, String shop_id, String item_code) {
        super(context);
        this.context = context;
        this.type = type;
        this.shop_id = shop_id;
        this.item_code = item_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_stock_info);
        dialog = this;

        spinner_item = (Spinner) findViewById(R.id.spinner_item);
        button_send = (Button) findViewById(R.id.button_send);


        spinner_item.setSelection(0);
        spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stock_stock =  (String)parent.getItemAtPosition(position);
                if(type==1) //출고일 경우 앞에 - 부호를 붙임
                    stock_stock = "-"+stock_stock;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //마지막 Shop 전송 버튼 눌렀을때
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask_stock networkTask = new NetworkTask_stock(context);

                Map<String, String> params = new HashMap<String, String>();
                params.put("item_code", item_code);
                params.put("shop_id", shop_id);
                params.put("stock_info", "test");
                params.put("stock_count", stock_stock);
                networkTask.execute(params);
                dialog.dismiss();
            }
        });
    }
}
