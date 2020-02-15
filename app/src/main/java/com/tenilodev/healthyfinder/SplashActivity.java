package com.tenilodev.healthyfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tenilodev.healthyfinder.model.NodeX;
import com.tenilodev.healthyfinder.utils.DbUtil;
import com.tenilodev.healthyfinder.utils.MyLocation;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import es.usc.citius.hipster.graph.GraphBuilder;


public class SplashActivity extends BaseActivity {

    private Pref preference;
    private Handler handler;
    private LocationManager locationManager;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    public void initVarUI() {

        preference = new Pref(getApplicationContext());
        handler = new Handler();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        new Thread(new Runnable() {
            @Override
            public void run() {

//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                registerAllNode();

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        locationManager  = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            //buildAlertMessageNoGps();
                        }

                        //MyLocation myLocation = new MyLocation();
                        //myLocation.getLocation(SplashActivity.this, new MyLocRes(SplashActivity.this));

                    }
                });
            }
        }).start();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        copyFile("dbkes.db");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                registerAllNode();

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        locationManager  = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            //buildAlertMessageNoGps();
                        }



                        //MyLocation myLocation = new MyLocation();
                        //myLocation.getLocation(SplashActivity.this, new MyLocRes(SplashActivity.this));
                        //preference.saveCurrentLocation(location.getLatitude(), location.getLongitude());

                        Intent mainIntent = new Intent(SplashActivity.this, PositionActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();

                    }
                });
            }
        }).start();

    }

    private void registerAllNode() {
        GraphBuilder<Integer, Double> builder = GraphBuilder.create();
        Dao<NodeX, Integer> daoNode = null;
        try {
            daoNode = DaoManager.createDao(DbUtil.getConnectionSource(), NodeX.class);
            List<NodeX> list = daoNode.queryForAll();
            for (NodeX node : list) {

                //System.out.println(node);

                double asalLatitude = node.getId().getLatitude();
                double asalLongitude = node.getId().getLongitude();

                if (node.getS1().getIdkoordinat() != 0) {
                    double targetLatitude = node.getS1().getLatitude();
                    double targetLongitude = node.getS1().getLongitude();
                    double jarak = distance(asalLatitude, asalLongitude, targetLatitude, targetLongitude);
                    builder.connect(node.getId().getIdkoordinat()).to(node.getS1().getIdkoordinat()).withEdge(jarak);
                }
                if (node.getS2().getIdkoordinat() != 0) {
                    double targetLatitude = node.getS2().getLatitude();
                    double targetLongitude = node.getS2().getLongitude();
                    double jarak = distance(asalLatitude, asalLongitude, targetLatitude, targetLongitude);
                    builder.connect(node.getId().getIdkoordinat()).to(node.getS2().getIdkoordinat()).withEdge(jarak);
                }
                if (node.getS3().getIdkoordinat() != 0) {
                    double targetLatitude = node.getS3().getLatitude();
                    double targetLongitude = node.getS3().getLongitude();
                    double jarak = distance(asalLatitude, asalLongitude, targetLatitude, targetLongitude);
                    builder.connect(node.getId().getIdkoordinat()).to(node.getS3().getIdkoordinat()).withEdge(jarak);
                }
                if (node.getS4().getIdkoordinat() != 0) {
                    double targetLatitude = node.getS4().getLatitude();
                    double targetLongitude = node.getS4().getLongitude();
                    double jarak = distance(asalLatitude, asalLongitude, targetLatitude, targetLongitude);
                    builder.connect(node.getId().getIdkoordinat()).to(node.getS4().getIdkoordinat()).withEdge(jarak);
                }
                if (node.getS5().getIdkoordinat() != 0) {
                    double targetLatitude = node.getS5().getLatitude();
                    double targetLongitude = node.getS5().getLongitude();
                    double jarak = distance(asalLatitude, asalLongitude, targetLatitude, targetLongitude);
                    builder.connect(node.getId().getIdkoordinat()).to(node.getS5().getIdkoordinat()).withEdge(jarak);
                }
            }

            ((App)this.getApplication()).setGraph(builder.createDirectedGraph());

            //graph = builder.createDirectedGraph();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Fitur GPS dalam keadaan tidak aktiv, Untuk melanjutkan kepengaturan lokasi pilih opsi YA.")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        //isSetGPS = true;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        //buildAlertMode();
                        //finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public class MyLocRes extends MyLocation.LocationResult {

        private Context ctx;

        public MyLocRes(Context ctx) {
            super();
            this.ctx = ctx;
        }



        @Override
        public void gotLocation(Location locationx) {


            final Location location = locationx;
            // Got the location!
            if (location == null) {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setMessage("Perangkat GPS berjalan dengan baik, tetapi ada masalah saat pengambilan lokasi terkini. Coba lagi?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        MyLocation myLocation = new MyLocation();
                                        myLocation.getLocation(SplashActivity.this, new MyLocRes(ctx));

                                    }
                                })
                                .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                        System.exit(0);
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();

                    }
                });


            } else {
                Log.d("lpg",
                        "daapa lokasi dengan latitude"
                                + location.getLatitude());

                preference.saveCurrentLocation(location.getLatitude(), location.getLongitude());

                Intent mainIntent = new Intent(SplashActivity.this, PositionActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();

            }
        }

    }


}
