package com.safetytech.senfuos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.SupportMapFragment;
import com.safetytech.senfuos.R;

/**
 * Created by ping6 on 2017/3/28.
 */

public class MapFragment extends SupportMapFragment implements View.OnClickListener{
    private Button btn_smap,btn_weixing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewReco = inflater.inflate(R.layout.fragment_map,container,false);
        return viewReco;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_smap = (Button) view.findViewById(R.id.btn_smap);
        btn_weixing = (Button) view.findViewById(R.id.btn_weixing);
        btn_smap.setOnClickListener(this);
        btn_weixing.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_smap:
                getMap().setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                break;
            case R.id.btn_weixing:
                getMap().setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                break;
        }
    }
}
