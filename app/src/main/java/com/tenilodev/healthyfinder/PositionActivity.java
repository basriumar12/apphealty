package com.tenilodev.healthyfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tenilodev.healthyfinder.utils.MyLocation;

import java.io.IOException;
import java.util.List;


public class PositionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int INTENT_RESULT_PICK = 2;
    private Pref preference;
    private Handler handler;
    private TextView textAddress, textProgress;
    private ProgressBar pb;
    private ImageView imgSuccess;
    private Button btnPick, btnFinish;
    private LocationManager locationManager;
    private boolean isPickLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        preference = new Pref(getApplicationContext());
        handler = new Handler();

        textAddress = (TextView)findViewById(R.id.textViewAddress);
        textProgress = (TextView)findViewById(R.id.textViewProgress);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        btnPick = (Button)findViewById(R.id.buttonPickLocation);
        btnFinish = (Button)findViewById(R.id.buttonFinish);
        imgSuccess = (ImageView)findViewById(R.id.imageViewSuccess);
        imgSuccess.setVisibility(View.INVISIBLE);

        btnFinish.setEnabled(false);

        btnPick.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        locationManager  = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //buildAlertMessageNoGps();
            pb.setVisibility(View.GONE);
            textProgress.setVisibility(View.GONE);
            imgSuccess.setImageResource(R.drawable.ic_gps_off);
            imgSuccess.setVisibility(View.VISIBLE);

            textAddress.setText(Html.fromHtml("Please enable gps device to detect location automatically." +
                    " Go to location setting to <a href ='test'>enable it</a>"));

            textAddress.setMovementMethod(new TextViewLinkHandler() {
                @Override
                public void onLinkClick(String url) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });

        }else{
            textAddress.setText("");
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this, new MyLocRes(this));

        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("PositionActivity", "on resume");

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //buildAlertMessageNoGps();
            pb.setVisibility(View.GONE);
            textProgress.setVisibility(View.GONE);
            imgSuccess.setImageResource(R.drawable.ic_gps_off);

            textAddress.setText(Html.fromHtml("Please enable gps device to detect location automatically." +
                    " Go to location setting to <a href ='test'>enable it</a>"));

            textAddress.setMovementMethod(new TextViewLinkHandler() {
                @Override
                public void onLinkClick(String url) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });

        }else{


                textAddress.setText("");
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(this, new MyLocRes(this));




        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_position, menu);
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

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.buttonPickLocation:
                handleButtonPick();
                break;
            case R.id.buttonFinish:
                handleButtonFinish();
                break;
        }

    }

    private void handleButtonFinish() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void handleButtonPick() {

        Intent iPick = new Intent(this, PickLocationActivity.class);
        startActivityForResult(iPick, INTENT_RESULT_PICK);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == INTENT_RESULT_PICK){

            isPickLocation = true;

            double lat = data.getDoubleExtra("latitude",0.0);
            double lon = data.getDoubleExtra("longitude",0.0);
            preference.saveCurrentLocation(lat, lon);

            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();

        }
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
                                        myLocation.getLocation(PositionActivity.this, new MyLocRes(ctx));
                                    }
                                })
                                .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();

                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();

                    }
                });


            } else {

                if(!isPickLocation){

                    Log.d("lpg",
                            "daapa lokasi dengan latitude"
                                    + location.getLatitude());

                    preference.saveCurrentLocation(location.getLatitude(), location.getLongitude());

                }

                try {
                    List<Address> addresses = new Geocoder(PositionActivity.this).getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                    final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    final String city = addresses.get(0).getLocality();
                    final String state = addresses.get(0).getAdminArea();
                    final String country = addresses.get(0).getCountryName();
                    final String postalCode = addresses.get(0).getPostalCode();
                    final String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String text = String.format("%s %s %s",address, city, state);
                            textAddress.setText(text);

                            pb.setVisibility(View.GONE);
                            textProgress.setVisibility(View.GONE);
                            imgSuccess.setVisibility(View.VISIBLE);
                            btnFinish.setEnabled(true);
                        }
                    });



                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

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

    public abstract class TextViewLinkHandler extends LinkMovementMethod {

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_UP)
                return super.onTouchEvent(widget, buffer, event);

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                onLinkClick(link[0].getURL());
            }
            return true;
        }

        abstract public void onLinkClick(String url);
    }
}
