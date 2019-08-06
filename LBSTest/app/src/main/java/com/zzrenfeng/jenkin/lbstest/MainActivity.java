package com.zzrenfeng.jenkin.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());  //注册一个定位监听器，当获取到位置信息的时候，回调自定义的定位监听器
        SDKInitializer.initialize(getApplicationContext());  //百度地图SDK初始化
        setContentView(R.layout.activity_main);

        positionText = (TextView) findViewById(R.id.position_text_view);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);  //开启在地图上设置构建的“我”的位置信息数据功能

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    /**
     * 开始地理位置定位
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    /**
     * 初始化定位设置
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);  //设置位置更新间隔
        /*
         * 定位模式：
         * Hight_Accuracy:高精度模式，默认模式；会在GPS信号正常的情况下优先使用GPS定位，在无法接收GPS信号的时候使用网络定位；
         * Battery_Saving:节电模式；只会使用网络进行定位；
         * Device_Sensors:传感器模式；只会使用GPS进行定位；
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0) {
                    for (int result : grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序！", Toast.LENGTH_SHORT).show();
                            finish();  //用户有任何一个权限被拒，就直接使用finish()方法关闭当前程序
                            return;
                        }
                    }
                    requestLocation();  //只有用户同意所有权限后才开始地理位置定位
                } else {
                    Toast.makeText(this, "发生未知错误！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();  //停止定位
        mapView.onDestroy();  //将地图控件销毁
        baiduMap.setMyLocationEnabled(false);  //关闭在地图上设置构建的“我”的位置信息数据功能
    }

    /**
     * 让百度地图定位到指定的位置（维度、经度）
     * @param location
     */
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);  //设置缩放比例级别
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        //构建“我”所在位置数据，并设置到地图上
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    /**
     * 自定义定位监听器内部类，定位结果会回调这个监听器中的onReceiveLocation()方法
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            //填充TextView信息
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("定位时间：").append(bdLocation.getTime()).append("\n");
                    currentPosition.append("定位类型：").append(bdLocation.getLocType()).append("\n");
                    currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                    currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
                    currentPosition.append("定位精度：").append(bdLocation.getRadius()).append("\n");
                    currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
                    currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
                    currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
                    currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
                    currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
                    currentPosition.append("位置语义化信息：").append(bdLocation.getLocationDescribe()).append("\n");
                    if(bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("定位方式：").append("GPS").append("\n");
                        currentPosition.append("速度：").append(bdLocation.getSpeed()).append("Km/h").append("\n");
                        currentPosition.append("卫星数：").append(bdLocation.getSatelliteNumber()).append("个").append("\n");
                        currentPosition.append("海拔：").append(bdLocation.getAltitude()).append("m").append("\n");
                        currentPosition.append("方向：").append(bdLocation.getDirection()).append("度").append("\n");
                        currentPosition.append("地址：").append(bdLocation.getAddrStr()).append("").append("\n");
                    } else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("定位方式：").append("网络").append("\n");
                        currentPosition.append("地址：").append(bdLocation.getAddrStr()).append("").append("\n");
                        currentPosition.append("运营商信息：").append(bdLocation.getOperators()).append("").append("\n");
                    } else if(bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                        currentPosition.append("定位描述：").append("离线定位成功，离线定位结果也是有效的").append("\n");
                    } else if(bdLocation.getLocType() == BDLocation.TypeServerError) {
                        currentPosition.append("定位描述：").append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因").append("\n");
                    } else if(bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                        currentPosition.append("定位描述：").append("网络不通导致定位失败，请检查网络是否通畅").append("\n");
                    } else if(bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                        currentPosition.append("定位描述：").append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着关闭飞行模式或重启手机").append("\n");
                    }
                    positionText.setText(currentPosition);
                }
            });

            //将地图定位到当前所在位置
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
//            super.onConnectHotSpotMessage(s, i);
        }
    }


}
