package com.tenilodev.healthyfinder;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tenilodev.healthyfinder.model.Tempat;
import com.tenilodev.healthyfinder.utils.DbUtil;

import java.sql.SQLException;
import java.util.List;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Tempat> listTempat;
    private Pref pref;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_maps;
    }

    @Override
    public void initVarUI() {

        int id_kategori = getIntent().getExtras().getInt(AppConfig.INTENT_KATEGORI);
        pref = new Pref(getApplicationContext());
        System.out.println("id kategori :"+id_kategori);


        try {
            Dao<Tempat, Integer> daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(),Tempat.class);
            listTempat = daoTempat.queryForEq("id_kategori",id_kategori);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0D000000")));
        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AppConfig.getDefaultLatLng(),14));

        Marker mCurrent = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
                        .title("my location")
                        .position(pref.getCurrentLocation())
        );


        builder.include(mCurrent.getPosition());

        for(Tempat t : listTempat){
            Marker marker =
            mMap.addMarker(new MarkerOptions()
                .position(new LatLng(t.getKoordinat().getLatitude(), t.getKoordinat().getLongitude()))
                    .title(t.getNama_tempat())
                    .snippet(t.getKeterangan())
            );
            builder.include(marker.getPosition());
        }

        final LatLngBounds bounds = builder.build();
        // Gets screen size
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height,15 ));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }
}
