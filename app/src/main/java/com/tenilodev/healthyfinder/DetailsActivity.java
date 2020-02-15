package com.tenilodev.healthyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tenilodev.healthyfinder.model.Tempat;
import com.tenilodev.healthyfinder.utils.DbUtil;

import java.sql.SQLException;

public class DetailsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SlidingUpPanelLayout slidingLayout;
    private TextView name_title;
    Dao<Tempat, Integer> daoTempat;
    private Tempat tempat;
    private LatLng targetLatLng;
    private ImageButton btn_route;
    private MenuItem menuFavorite;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_details;
    }

    @Override
    public void initVarUI() {
        name_title = (TextView)findViewById(R.id.name);
        btn_route = (ImageButton)findViewById(R.id.btn_route);

        try {
            daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(), Tempat.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempat != null){
                    Intent i = new Intent(DetailsActivity.this, RouteActivity.class);
                    i.putExtra(AppConfig.INTENT_IDTEMPAT, tempat.getId_tempat());
                    System.out.println("id tempat yg dipilih :" + tempat.getId_tempat());
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.actionbar_gradient));
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000ff")));
        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        handleIntent(getIntent());

        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                if(v > 0.1f){
                    slidingLayout.setAnchorPoint(0.6f);
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                }

            }

            @Override
            public void onPanelCollapsed(View view) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 16));
            }

            @Override
            public void onPanelExpanded(View view) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 16));
            }

            @Override
            public void onPanelAnchored(View view) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0.0f, 250.0f));

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        setUpMapIfNeeded();
    }

    private void handleIntent(Intent intent) {
        if(intent.getExtras() != null){
            try {
                tempat = daoTempat.queryBuilder().where().eq("id_tempat", intent.getIntExtra(AppConfig.INTENT_IDTEMPAT,0)).queryForFirst();
                name_title.setText(tempat.getNama_tempat());
                targetLatLng = new LatLng(tempat.getKoordinat().getLatitude(), tempat.getKoordinat().getLongitude());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        menuFavorite = menu.findItem(R.id.action_favorite);
        if(tempat.getFavorite() == 1){
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_action_favoriteon);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        if(id == R.id.action_favorite){

            if(tempat.getFavorite() == 0){
                Tempat t = tempat;
                t.setFavorite(1);
                try {
                    daoTempat.update(t);
                    menuFavorite.setIcon(R.drawable.ic_action_favoriteon);
                    Toast.makeText(this,getString(R.string.add_favorite),Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return true;
            }
            if(tempat.getFavorite() == 1){
                Tempat t = tempat;
                t.setFavorite(0);
                try {
                    daoTempat.update(t);
                    menuFavorite.setIcon(R.drawable.ic_action_favoriteoff);
                    Toast.makeText(this,getString(R.string.remove_favorite),Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
/*            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(this);*/
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

        Marker tMarker = mMap.addMarker(new MarkerOptions()
                .position(targetLatLng)
                .snippet(tempat.getKeterangan())
                .title(tempat.getNama_tempat()));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(tMarker.getPosition());
        LatLngBounds bounds = builder.build();

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                bounds,
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels,
                200
        ));

        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Marker tMarker = mMap.addMarker(new MarkerOptions()
                .position(targetLatLng)
                .snippet(tempat.getKeterangan())
                .title(tempat.getNama_tempat()));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(tMarker.getPosition());
        LatLngBounds bounds = builder.build();

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                bounds,
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels,
                200
        ));

        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }
}
