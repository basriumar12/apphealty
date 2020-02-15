package com.tenilodev.healthyfinder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tenilodev.healthyfinder.model.Koordinat;
import com.tenilodev.healthyfinder.model.NodeX;
import com.tenilodev.healthyfinder.model.Tempat;
import com.tenilodev.healthyfinder.utils.DbUtil;
import com.tenilodev.healthyfinder.utils.Hipsters;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;

public class RouteActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int id_tempat;
    private Dao<Tempat, Integer> daoTempat;
    private Dao<Koordinat, Integer> daoKoordinat;
    private LatLng curLatLng;
    private Tempat tempat;
    private Dao<NodeX, Integer> daoNode;
    private HipsterDirectedGraph<Integer, Double> graph;
    private Pref pref;
    private SQLiteDatabase database;
    private TextView tvDistance, tvWaktu;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_route;
    }

    @Override
    public void initVarUI() {

        pref = new Pref(getApplicationContext());
        database = SQLiteDatabase.openDatabase(DbUtil.getFullPathDb(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        id_tempat = getIntent().getExtras().getInt(AppConfig.INTENT_IDTEMPAT);
        tvDistance = (TextView)findViewById(R.id.textViewDistance);
        tvWaktu = (TextView)findViewById(R.id.textViewWaktu);

        try {
            daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(), Tempat.class);
            daoKoordinat = DaoManager.createDao(DbUtil.getConnectionSource(), Koordinat.class);
            daoNode = DaoManager.createDao(DbUtil.getConnectionSource(), NodeX.class);

            tempat = daoTempat.queryForId(id_tempat);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(tempat.getNama_tempat());

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
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
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

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        curLatLng = new LatLng(tempat.getKoordinat().getLatitude(),tempat.getKoordinat().getLongitude());

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(midPoint(tempat.getKoordinat().getLatitude(), tempat.getKoordinat().getLongitude(), pref.getCurrentLocation().latitude, pref.getCurrentLocation().longitude), 13));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatLng,14));
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(curLatLng,15)));
        Marker markerTarget = mMap.addMarker(new MarkerOptions()
            .position(curLatLng)
            .title(tempat.getNama_tempat()));

        markerTarget.showInfoWindow();

        Marker markerCurrent = mMap.addMarker(new MarkerOptions()
                        .position(pref.getCurrentLocation())
                        .title("my location")
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
        );

        builder.include(markerTarget.getPosition());
        builder.include(markerCurrent.getPosition());

        final LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,width,height,100));
        //cariTitikTerdekat(curLatLng);

        //initAlgorithm();
        startRoute();

    }

    private LatLng midPoint(double lat1, double long1, double lat2,double long2)
    {

        return new LatLng((lat1+lat2)/2, (long1+long2)/2);

    }

    private void startRoute() throws NoSuchElementException{

        graph = ((App)getApplication()).getGraph();
        Koordinat k = CariTitikTerdekat(pref.getCurrentLocation());
        LatLng target_latlng = new LatLng(tempat.getKoordinat().getLatitude(), tempat.getKoordinat().getLongitude());
        Koordinat x = CariTitikTerdekat(target_latlng);

        System.out.println("id koordinat target : "+x.getIdkoordinat());

        SearchProblem searchProblem = GraphSearchProblem
                .startingFrom(k.getIdkoordinat())
                .in(graph)
                .takeCostsFromEdges()
                .build();


        //String res = Hipster.createBellmanFord(searchProblem).search(660).toString();
        //System.out.println(res);
        double jarak = 0.0;
        List<LatLng> listRoute = new ArrayList<LatLng>();
        listRoute.add(pref.getCurrentLocation());

        Algorithm.SearchResult searchResult = Hipsters.createSAHillClimbing(searchProblem).search(x.getIdkoordinat());
        System.out.println(searchResult.toString());
        if(searchResult.getGoalNodes().size() > 0){
            List<WeightedNode> res = searchResult.getGoalNode().path();
            for(WeightedNode w : res){
                //System.out.print(w.state().toString() + " ");

                double lat = QueryLokasi(Integer.parseInt(w.state().toString())).getLatitude();
                double lon = QueryLokasi(Integer.parseInt(w.state().toString())).getLongitude();
                LatLng latLong = new LatLng(lat,lon);
                listRoute.add(latLong);
                jarak = Double.valueOf(w.getCost().toString());
            }

            Double mtokm = jarak * 0.001;

            //double jarak = jarak;
            double w = mtokm/40.0;
            int j = (int) (w);
            double sm = w-j;
            int m = (int)(sm*60);
            double sd = (sm*60)-m;
            int d = (int)(sd*60);
            //jarak = jarak/1000;

            String sjarak = String.format(getString(R.string.distance)+" %f km", mtokm);
            String swaktu = String.format(getString(R.string.travelingTime)+" %d:%d:%d. "+getString(R.string.hour) , j,m,d);


            String jarakFormat = new DecimalFormat("#.###").format(mtokm);

            listRoute.add(target_latlng);
            tvDistance.setText(getString(R.string.distance) + " " + jarakFormat + " km");
            tvWaktu.setText(swaktu);

            mMap.addPolyline(new PolylineOptions().addAll(listRoute).width(15.0f).color(Color.BLUE));

        }else {
            Toast.makeText(getApplicationContext(),"Lokasi ini belum terhubung dengan vertex di database",Toast.LENGTH_SHORT).show();
        }

    }

    private Koordinat QueryLokasi(int id){
        Koordinat koordinat = null;
        try {
            koordinat = daoKoordinat.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return koordinat;
    }

    private Koordinat CariTitikTerdekat(LatLng skrg){
        Koordinat lt = new Koordinat();
        String sql = "SELECT k.lat,k.lon,n.id FROM node n,koordinat k WHERE k.idkoordinat=n.id AND n.s2>0";
        Cursor cursor = database.rawQuery(sql ,null);
        double jterdekat = 0.0;
        int i=0;
        while(cursor.moveToNext()){
            double l = cursor.getDouble(0);
            double o = cursor.getDouble(1);
            int idx = cursor.getInt(2);

            // hitung jarak
            double jarak = distance(skrg.latitude, skrg.longitude, l, o);
            if(i==0){
                jterdekat = jarak;
                lt.setIdkoordinat(idx);
                lt.setLatitude(l);
                lt.setLongitude(o);
            }else{
                if(jarak<jterdekat){
                    jterdekat = jarak;
                    lt.setIdkoordinat(idx);
                    lt.setLatitude(l);
                    lt.setLongitude(o);
                }
            }
            i++;
        }

        return lt;
    }


    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0; // miles 3958.75 (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c * 1000 ;

        return dist;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }
}
