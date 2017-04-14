package com.safetytech.senfuos;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.common.mission.waypoint.Waypoint;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;

public class MainActivity extends CheckPermissionsActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,LocationSource,AMapLocationListener,
        TextureView.SurfaceTextureListener {
    private TabLayout tab_bottom;
    private AMap aMap;
    private MapView mapView;
    private Button btn_smap,btn_weixing,to_option2,to_option3,setPoint;
    private ImageView to_option1,backto_option2;
    private FrameLayout data,camera;
    private UiSettings mUiSettings;
    private TextView location;
    private Marker locationMarker;
    private EditText jingdu,weidu,mission_name,mission_addr;
    private RelativeLayout map_container;
    private ScrollView option1,option2,option3;
    private Spinner mission_type,mission_mode;
    private String task_name,task_addr;
    private DrawerLayout drawer;
    //定位
    private OnLocationChangedListener mLocationChangeListener;
    private AMapLocationClientOption mLocationOption;
    public AMapLocationClient mLocationClient;
    private SeekBar gaodu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_title);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        immersiveStick();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        map_container = (RelativeLayout) findViewById(R.id.map_container);
        data = (FrameLayout) findViewById(R.id.data);
        camera = (FrameLayout) findViewById(R.id.camera);
        mapView = (MapView) findViewById(R.id.map);
        option1 = (ScrollView) findViewById(R.id.option1);
        option2 = (ScrollView) findViewById(R.id.option2);
        option3 = (ScrollView) findViewById(R.id.option3);
        tab_bottom = (TabLayout) findViewById(R.id.tab_bottom);

        btn_smap = (Button) findViewById(R.id.btn_smap);
        btn_smap.setOnClickListener(this);
        btn_weixing = (Button) findViewById(R.id.btn_weixing);
        btn_weixing.setOnClickListener(this);
        location = (TextView) findViewById(R.id.location);
        to_option1 = (ImageView) findViewById(R.id.to_option1);
        to_option1.setOnClickListener(this);
        to_option2 = (Button) findViewById(R.id.to_option2);
        to_option2.setOnClickListener(this);
        backto_option2 = (ImageView) findViewById(R.id.backto_option2);
        backto_option2.setOnClickListener(this);
        to_option3 = (Button) findViewById(R.id.to_option3);
        to_option3.setOnClickListener(this);
        setPoint = (Button) findViewById(R.id.setPoint);
        setPoint.setOnClickListener(this);
        jingdu = (EditText) findViewById(R.id.jingdu);
        weidu = (EditText) findViewById(R.id.weidu);
        mission_name = (EditText) findViewById(R.id.mission_name);
        mission_addr = (EditText) findViewById(R.id.mission_addr);
        mission_type = (Spinner) findViewById(R.id.mission_type);
        mission_mode = (Spinner) findViewById(R.id.mission_mode);
        gaodu = (SeekBar) findViewById(R.id.seek_gaodu);
        gaodu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tab_bottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        map_container.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);
                        camera.setVisibility(View.GONE);
                        break;
                    case 1:
                        map_container.setVisibility(View.GONE);
                        data.setVisibility(View.VISIBLE);
                        camera.setVisibility(View.GONE);
                        break;
                    case 2:
                        map_container.setVisibility(View.GONE);
                        data.setVisibility(View.GONE);
                        camera.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //地图
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);//指南针

        aMap.setLocationSource(this);// 设置定位监听
        mUiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        mission_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos >=1 && pos <= 3){
                    mission_mode.setVisibility(View.VISIBLE);
                }else {
                    mission_mode.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        immersiveStick();
        return true;
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
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_smap:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                break;
            case R.id.btn_weixing:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                break;
            case R.id.setPoint:
                String j = jingdu.getText().toString();
                String w = weidu.getText().toString();
                if(j.isEmpty() || w.isEmpty()){
                    Toast.makeText(this,"经纬度不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng latLng = new LatLng(Double.parseDouble(j),Double.parseDouble(w));
                aMap.addMarker(new MarkerOptions().position(latLng).title(task_name));
                break;
            case R.id.to_option2:
                task_name = mission_name.getText().toString();
                task_addr = mission_addr.getText().toString();
                if(task_name.isEmpty() || task_addr.isEmpty()){
                    Toast.makeText(this,"任务名称或地址不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                option1.setVisibility(View.GONE);
                option2.setVisibility(View.VISIBLE);
                option3.setVisibility(View.GONE);
                setMapClickListener();
                break;
            case R.id.to_option3:
                option1.setVisibility(View.GONE);
                option2.setVisibility(View.GONE);
                option3.setVisibility(View.VISIBLE);
                aMap.setOnMapClickListener(null);
                break;
            case R.id.to_option1:
                option1.setVisibility(View.VISIBLE);
                option2.setVisibility(View.GONE);
                option3.setVisibility(View.GONE);
                aMap.setOnMapClickListener(null);
                break;
            case R.id.backto_option2:
                option1.setVisibility(View.GONE);
                option2.setVisibility(View.VISIBLE);
                option3.setVisibility(View.GONE);
                setMapClickListener();
                break;
            default:
                break;
        }
    }

    private void setMapClickListener(){
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double j = latLng.latitude;
                Double w = latLng.longitude;
                if(null != jingdu){
                    jingdu.setText(j.toString());
                }
                if(null != weidu){
                    weidu.setText(w.toString());
                }
                LatLng jw = new LatLng(j,w);
                aMap.addMarker(new MarkerOptions().position(jw).title("任务名称").snippet("任务内容"));
            }
        });
    }

    /**
     * 沉浸式
     */
    private void immersiveStick(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 激活定位
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangeListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mLocationOption.setHttpTimeOut(20000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mLocationOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
            mLocationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
            mLocationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
            mLocationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);

            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mLocationChangeListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mLocationChangeListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {

                location.setText("经度：" + aMapLocation.getLatitude() +
                        "纬度：" + aMapLocation.getLongitude() +
                        "地址：" + aMapLocation.getCountry() + "," + aMapLocation.getProvince()
                        + "," + aMapLocation.getCity() + "," + aMapLocation.getAddress());
                LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                //添加Marker显示定位位置
                if (locationMarker == null) {
                    locationMarker = aMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));
                } else {
                    locationMarker.setPosition(latLng);
                }
                  //然后可以移动到定位点,使用animateCamera就有动画效果
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//                mLocationChangeListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                Toast.makeText(MainActivity.this,"定位失败，" + aMapLocation.getErrorInfo(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
    
}
