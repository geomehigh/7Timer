package com.example.a4.a7timer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class MainActivity extends AppCompatActivity {

    private static final String TODO ="Consider calling" ;
    private Button btnSetPos,btnWhut,btnOK;
    private EditText edtLon, edtLat;
    private String strUrl, str1, str2, str3, strLon, strLat;
    private LocationManager locationManager;        //定义一个LocationManager和一个当前正在使用的位置提供器(GPS还是网络)
    private String locationProvider;    //
    private LocationListener locationListener;
    private LinearLayout linearLayoutShow;
    private ImageView imageView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        str1 = "http://167.99.8.254/bin/astro.php?lon=";
        str2 = "&lat=";
        str3 = "&ac=0&lang=zh-CN&unit=metric&output=internal&tzshift=0";
        edtLon = (EditText) findViewById(R.id.editTextLon);
        edtLat = (EditText) findViewById(R.id.editTextLat);


        imageView=(ImageView)findViewById(R.id.imageView);
        linearLayoutShow=(LinearLayout)findViewById(R.id.show7Timer);
        btnWhut=(Button)findViewById(R.id.btnWhut);
        btnWhut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                strUrl="http://167.99.8.254/bin/astro.php?lon=114.350&lat=30.511&ac=0&lang=zh-CN&unit=metric&output=internal&tzshift=0";

                edtLon.setText("114.350");
                edtLat.setText("30.511");

//                strUrl="http://167.99.8.254/bin/astro.php?lon=114.350&lat=30.511&ac=0&lang=zh-CN&unit=metric&output=internal&tzshift=0";
//                Uri uri = Uri.parse(strUrl);//要跳转的网址
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);


//                WebView TimerView = new WebView(MainActivity.this);
//                setContentView(TimerView);
//                String mUrl = "http://167.99.8.254/bin/astro.php?lon=114.350&lat=30.511&ac=0&lang=zh-CN&unit=metric&output=internal&tzshift=0";
//
//                TimerView.loadUrl(mUrl);
            }
        });


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
                Toast.makeText(MainActivity.this, "为空，不显示地理位置", Toast.LENGTH_SHORT);
                if (location != null) {
                    Toast.makeText(MainActivity.this, "不为空，显示地理位置", Toast.LENGTH_SHORT);
                    //不为空,显示地理位置经纬度
                    showLocation(location);
                }

                Toast.makeText(MainActivity.this, "监视", Toast.LENGTH_SHORT);

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

                //监视地理位置变化
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

//                showLocation(location);
//                strLon = edtLon.getText().toString();
//                strLat = edtLat.getText().toString();

                if(location==null){
                    Toast.makeText(MainActivity.this, "未获取到经纬度信息", Toast.LENGTH_SHORT);
                    return;
                }

                strLon=Double.toString(location.getLongitude());
                strLat=Double.toString(location.getLatitude());

                edtLon.setText(strLon);
                edtLat.setText(strLat);


//                Uri uri = Uri.parse(strUrl);//要跳转的网址
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);

//                HttpClient client = new DefaultHttpClient();
//                HttpUriRequest request = new HttpGet("http://www.baidu.com");
//                HttpResponse response = client.execute(request);
//                InputStream ins = response.getEntity().getContent();




                }

        });

        btnOK=(Button)findViewById(R.id.btnOK);         //查询天气
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strLon=edtLon.getText().toString();
                strLat=edtLat.getText().toString();
                strUrl = str1 + strLon + str2 + strLat + str3;
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            loadRmoteImage(strUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }

    private void showLocation(Location location) {
        String strStr = "定位成功------->" + "location------>" + "经度为：" + location.getLongitude() + "\n纬度为" + location.getLatitude();
        strLon=Double.toString(location.getLongitude());
        strLat=Double.toString(location.getLatitude());
        edtLon.setText(strLon);
        edtLat.setText(strLat);
    }

    /**
     * @param imgUrl
     *   远程图片文件的URL
     *
     *   下载远程图片
     */
    private void loadRmoteImage(String imgUrl) throws IOException {
        BaseHttpConnection connection = new BaseHttpConnection();
        connection.setInstanceCookieStorage(true);
        connection.setInstanceFollowRedirects(false);
        byte[] imgData = new byte[1];
        try{
            imgData = connection.getRaw(imgUrl);
            Log.d("666666666666","大小："+imgData.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(imgData.length == 1){
            Log.d("666666666666","请求失败");
            return;
        }
        final Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0,
                imgData.length);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });

//
//        final String sss = imgUrl;
//        Log.d("66666666",sss);
//        URL fileURL = null;
//        InputStream is=null;
//        try {
//            fileURL = new URL(imgUrl);
//        } catch (MalformedURLException err) {
//            err.printStackTrace();
//        }
//        try {
//            HttpURLConnection conn = (HttpURLConnection) fileURL.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            Log.d("666666666666","ready");
//            is = conn.getInputStream();
//            int length = (int) conn.getContentLength();
//            Log.d("666666666666","length:"+length);
//            if (length != -1) {
//                byte[] imgData = new byte[length];
//                byte[] buffer = new byte[512];
//                int readLen = 0;
//                int destPos = 0;
//                while ((readLen = is.read(buffer)) > 0) {
//                    System.arraycopy(buffer, 0, imgData, destPos, readLen);
//                    destPos += readLen;
//                }
//                Log.d("666666666666","大小："+imgData.length+"\n"+new String(imgData));
//                final Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0,
//                        imgData.length);
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("66666666",sss);
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

}
