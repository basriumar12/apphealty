package com.tenilodev.healthyfinder;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class PickLocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_pick_location;
    }

    @Override
    public void initVarUI() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.\
        try {


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

        }catch (NullPointerException e){

        }
    }

    private void setUpMap() {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AppConfig.getDefaultLatLng(), 14f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();

                List<Address> addresses = null;
                try {
                    addresses = new Geocoder(PickLocationActivity.this).getFromLocation(latLng.latitude,latLng.longitude, 1);

                    final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    final String city = addresses.get(0).getLocality();
                    final String state = addresses.get(0).getAdminArea();
                    final String country = addresses.get(0).getCountryName();
                    final String postalCode = addresses.get(0).getPostalCode();
                    final String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    String text = String.format("%s %s %s", address, city, state);
                    //String text = String.format("%f , %f", latLng.latitude, latLng.longitude);

                    Marker mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(address).snippet(city));
                    mMarker.showInfoWindow();
                    mLatitude = latLng.latitude;
                    mLongitude = latLng.longitude;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_pick){

            if(mLatitude == 0.0){
                Toast.makeText(this,getString(R.string.pick_location_required),Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent i = new Intent();
            i.putExtra("latitude", mLatitude);
            i.putExtra("longitude",mLongitude);
            setResult(RESULT_OK, i);
            finish();
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
