package com.bigpicture.hje.scanner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Network.NetworkTask_stock;
import com.VO.Stock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-11.
 */

public class StockDialog extends Dialog {


    private EditText edittext_stock_info;
    private EditText edittext_stock_stock;
    private Spinner spinner_item_code;
    private Spinner spinner_shop_id;
    private Button button_send_stock;

    private Stock stock;
    private String item_code;
    private String shop_id;
    private int stock_stock;
    private Dialog dialog;
    private Context context;

    public StockDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_stock_info);
        dialog = this;

        edittext_stock_info = (EditText) findViewById(R.id.edittext_stock_info);
        edittext_stock_stock = (EditText) findViewById(R.id.edittext_stock_stock);
        spinner_item_code = (Spinner) findViewById(R.id.spinner_item_code);
        spinner_shop_id = (Spinner) findViewById(R.id.spinner_shop_id);
        button_send_stock = (Button) findViewById(R.id.button_send_stock);


        spinner_item_code.setSelection(0);
        spinner_item_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_code = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_shop_id.setSelection(0);
        spinner_shop_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shop_id = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //마지막 Shop 전송 버튼 눌렀을때
        button_send_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stock_info = edittext_stock_info.getText().toString();
                int stock_stock = Integer.parseInt(edittext_stock_stock.getText().toString());

                if (spinner_item_code == null || spinner_shop_id == null)
                    Toast.makeText(getContext(), "전송 불가 : Stock 입력 정보 확인", Toast.LENGTH_LONG).show();
                else {
                    stock = new Stock(item_code, shop_id, stock_info, stock_stock);

                    NetworkTask_stock networkTask = new NetworkTask_stock(context);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("item_code", stock.getItem_code());
                    params.put("shop_id", String.valueOf(stock.getShop_id()));
                    params.put("stock_info", stock.getStock_info());
                    params.put("stock_count", String.valueOf(stock.getStock_stock()));
                    networkTask.execute(params);

                    dialog.dismiss();
                }
            }
        });
    }
}
