package com.example.kw_mk;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;
import net.daum.android.map.location.MapViewLocationManager;

public class MapViewTest extends AppCompatActivity {

    MapViewLocationManager mL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        initView();

    }

    void initView() {

        MapPOIItem marker = new MapPOIItem();
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setItemName("Default");
        marker.setTag(0);

        ViewGroup mapViewContainer = this.findViewById(R.id.mapviewTest);


    }
}
