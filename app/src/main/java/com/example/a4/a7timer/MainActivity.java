package com.example.a4.a7timer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TODO ="Consider calling" ;
    private Button btnSetPos,btnWhut;
    private EditText edtLon, edtLat;
    private String strUrl, str1, str2, str3, strLon, strLat;
    private LocationManager locationManager;        //定义一个LocationManager和一个当前正在使用的位置提供器(GPS还是网络)
    private String locationProvider;    //
    private LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWhut=(Button)findViewById(R.id.btnWhut);
        btnWhut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUrl="http://www.7timer.info/bin/astro.php?lon=114.350&lat=30.511&ac=0&lang=zh-CN&unit=metric&output=internal&tzshift=0";
                Uri uri = Uri.parse(strUrl);//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        str1 = "http://www.7timer.info/bin/astro.php?lon=";
        str2 = "&lat=";
        str3 = "&ac=0&lang=zh-CN&unit=metric&output=internal&tzshift=0";

        edtLon = (EditText) findViewById(R.id.editTextLon);
        edtLat = (EditText) findViewById(R.id.editTextLat);
        btnSetPos = (Button) findViewById(R.id.SetPosition);
        btnSetPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //然后获取位置服务
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //然后获取所有可以用的位置提供器，并检测当前是否有可用的位置提供器
                List<String> providers = locationManager.getProviders(true);
                if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    //如果是GPS
                    Toast.makeText(MainActivity.this, "是GPS", Toast.LENGTH_SHORT);
                    locationProvider = LocationManager.GPS_PROVIDER;
                } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    //如果是Network
                    Toast.makeText(MainActivity.this, "是Network", Toast.LENGTH_SHORT);
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                } else {
                    Toast.makeText(MainActivity.this, "没有可用的位置提供器", Toast.LENGTH_SHORT);
                    return;
                }
                //如果有位置提供器，那么就获取Location
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "有可用的位置提供器", Toast.LENGTH_SHORT);
                    return;
                }
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    //不为空,显示地理位置经纬度
                    showLocation(location);
                }
                //监视地理位置变化
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                //LocationListern监听器：
                /**
                 * LocationListern监听器
                 * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
                 */
                locationListener = new LocationListener() {
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle arg2) {
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                    }
                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                    @Override
                    public void onLocationChanged(Location location) {
                        //如果位置发生变化,重新显示
                        showLocation(location);
                    }
                };


                strLon = edtLon.getText().toString();
                strLat = edtLat.getText().toString();
                strUrl = str1 + strLon + str2 + strLat + str3;
                Uri uri = Uri.parse(strUrl);//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


//                HttpClient client = new DefaultHttpClient();
//                HttpUriRequest request = new HttpGet("http://www.baidu.com");
//                HttpResponse response = client.execute(request);
//                InputStream ins = response.getEntity().getContent();

            }
        });




    }

    private void showLocation(Location location) {
        String strStr = "定位成功------->" + "location------>" + "经度为：" + location.getLatitude() + "\n纬度为" + location.getAltitude();
        edtLon.setText(strStr);
    }

}
