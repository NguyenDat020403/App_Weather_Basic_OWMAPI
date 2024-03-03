package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    ImageView imvBack;
    TextView txtName;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangThoithiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Anhxa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua","Du lieu truyen qua: " + city);
        if(city.equals("")){
            city = "Saigon";
            Get7DaysData(city);
        }else{
            Get7DaysData(city);
        }
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void Anhxa() {
        imvBack = (ImageView) findViewById(R.id.imvBack);
        txtName = (TextView) findViewById(R.id.txtCityName);
        lv = (ListView) findViewById(R.id.lvDays);
        mangThoithiet = new ArrayList<Thoitiet>();
        customAdapter = new CustomAdapter(MainActivity2.this,mangThoithiet);
        lv.setAdapter(customAdapter);
    }

    private void Get7DaysData(String data){
        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+data+"&cnt=16&appid=53fbf527d52d4d773e828243b90c1f8e";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                    String name = jsonObjectCity.getString("name");
                    txtName.setText(name);

                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                    for(int i = 0; i<jsonArrayList.length();i++){

                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        String ngay = jsonObjectList.getString("dt");
                        //day
                        Long l = Long.valueOf(ngay);
                        Date date = new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                        String Day = simpleDateFormat.format(date);
                        //temp
                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                        String max = jsonObjectTemp.getString("max");
                        String min = jsonObjectTemp.getString("min");

                        Double a = Double.valueOf(max)/10;
                        Double b = Double.valueOf(min)/10;
                        String NhietDoMax = String.valueOf(a.intValue());
                        String NhietDoMin = String.valueOf(b.intValue());


                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String status = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");

                        mangThoithiet.add(new Thoitiet(Day,status,icon,NhietDoMax,NhietDoMin));

                    }
                    customAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }
}