package com.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.common.Scan;

import java.util.Map;

/**
 * Created by Administrator on 2017-04-15.
 */

public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
    private Context context;
    private int statusCode;

    public NetworkTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder("POST", Scan.localUrl+Scan.shopAdd);

// Parameter 를 전송한다.
        http.addAllParameters(maps[0]);


//Http 요청 전송
        HttpClient post = http.create();
        post.request();

// 응답 상태코드 가져오기
        statusCode = post.getHttpStatusCode();
        Log.d("HTTP", "Result from server : " + statusCode);
// 응답 본문 가져오기
        String body = "";
        if(statusCode == 200) {
            body = post.getBody();
            return body;
        }else if(statusCode == -10){
            Toast.makeText(context, "서버 응답이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null) {
            Toast.makeText(context, "Shop 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            Log.d("HTTP", "Response from server : " + s);
        }
        else {
            Toast.makeText(context, "Shop 등록에 문제가있습니다.", Toast.LENGTH_SHORT).show();
            Log.e("HTTP", "Response error");
        }

    }
}
