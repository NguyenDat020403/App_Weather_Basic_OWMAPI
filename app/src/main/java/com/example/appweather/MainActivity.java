package com.example.appweather;

import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnSearch, btnChangeActivity;
    TextView txtName, txtCountry, txtTemp, txtStatus, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon;
    String City ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        getCurrentWeatherData("Saigon");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                if(city.equals("")) {
                    City = "Saigon";
                    getCurrentWeatherData(City);
                }else{
                    City = city;
                    getCurrentWeatherData(City);
                }
            }
        });
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("name",city);
                startActivity(intent);
            }
        });
    }
    public void getCurrentWeatherData(String Data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+Data+"&appid=6acca46f300076e39c828702d611353a";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String day = jsonObject.getString("dt");
                    String name = jsonObject.getString("name");
                    txtName.setText("Tên thành phố: "+name);
                    Long l = Long.valueOf(day);
                    Date date = new Date(l*1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                    String Day = simpleDateFormat.format(date);
                    txtDay.setText(Day);
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");

                    Picasso.get().load("https://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);
                    txtStatus.setText(status);

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String nhietdo = jsonObjectMain.getString("temp");
                    String doam = jsonObjectMain.getString("humidity");

                    Double a = Double.valueOf(nhietdo)/10;

                    String NhietDo = String.valueOf(a.intValue());

                    txtTemp.setText(NhietDo + "°C");
                    txtHumidity.setText(doam +"%");
                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String gio = jsonObjectWind.getString("speed");
                    txtWind.setText(gio +"m/s");
                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                    String may = jsonObjectCloud.getString("all");
                    txtCloud.setText(may+"%");
                    JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                    String country = jsonObjectSys.getString("country");
                    txtCountry.setText("Tên quốc gia: " + country);
                }catch (JSONException e)
                {
                        e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    private void Anhxa(){
        editSearch = (EditText) findViewById(R.id.edittextSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnChangeActivity  = (Button) findViewById(R.id.btnChangeActivity);
        txtName = (TextView) findViewById(R.id.txtName);
        txtCountry = (TextView) findViewById(R.id.txtCountry);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtStatus = (TextView) findViewById(R.id.txtTrangThai);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtCloud = (TextView) findViewById(R.id.txtCloud);
        txtWind = (TextView) findViewById(R.id.txtWind);
        txtDay = (TextView) findViewById(R.id.txtDay);
        imgIcon = (ImageView) findViewById(R.id.imvIcon);
    }
}