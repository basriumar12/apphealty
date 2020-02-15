package app.manual;

import android.content.res.AssetManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import app.manual.model.Koordinat;
import app.manual.model.Tempat;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Dao<Tempat, Integer> daoTempat;
    private Dao<Koordinat, Integer> daoKoordinat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        copyFile("dbkes.db");

        try {
            daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(), Tempat.class);
            daoKoordinat = DaoManager.createDao(DbUtil.getConnectionSource(), Koordinat.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AppConfig.getDefaultLatLng(), 14));

        try {
            List<Tempat> listTempat = daoTempat.queryForAll();
            for(Tempat t : listTempat){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(t.getKoordinat().getLatitude(), t.getKoordinat().getLongitude()))
                        .title(t.getNama_tempat())
                );
            }

            List<Koordinat> listKoordinat = daoKoordinat.queryForAll();
            for(Koordinat k : listKoordinat){
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(k.getLatitude(), k.getLongitude()))
                                .title(String.valueOf(k.getIdkoordinat()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_node))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = "/data/data/" + this.getPackageName() + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

}
