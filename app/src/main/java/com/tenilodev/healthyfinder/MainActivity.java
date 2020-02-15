package com.tenilodev.healthyfinder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends BaseActivity{

    ImageView img_doctor, img_apotek, img_puskes, img_klinik, img_rs, img_other;
    LinearLayout layImage1;
    private Drawer drawer;
    private AdView mAdView;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void initVarUI() {

        img_doctor = (ImageView)findViewById(R.id.img_doctor);
        img_apotek = (ImageView)findViewById(R.id.img_apotek);
        img_puskes = (ImageView)findViewById(R.id.img_puskesmas);
        img_klinik = (ImageView)findViewById(R.id.img_clinic);
        img_rs = (ImageView)findViewById(R.id.img_rumahsakit);
        img_other = (ImageView)findViewById(R.id.img_other);
        layImage1 = (LinearLayout)findViewById(R.id.layImage1);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("7535AF4EB2936B47ED5B7A27A0547F7F")
                .build();
        //banner
       /* mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(adRequest);*/
        //interstial
      /*  final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7486974203577954/1852043152");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
        interstitialAd.loadAd(adRequest);*/
        //YoYo.with(Techniques.FadeInLeft).duration(1500).playOn(img_doctor);
        //System.out.println(getApplicationInfo().dataDir);
        imageOnTouch();

        initDrawer();


    }

    private void initDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                        //.withHeightDp(100)
                .withCompactStyle(true)
                .withOnlyMainProfileImageVisible(true)
                .addProfiles(
                        new ProfileDrawerItem().withName("Lokasi Pelayanan Kesehatan").withEmail("").withIcon(R.drawable.other)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.label_favorite)
                                .withIcon(FontAwesome.Icon.faw_star),
                        new PrimaryDrawerItem()
                                .withName(R.string.label_search)
                                .withIcon(GoogleMaterial.Icon.gmd_search),
                        new PrimaryDrawerItem()
                                .withName(R.string.label_about)
                                .withIcon(new IconicsDrawable(this)
                                        .icon(FontAwesome.Icon.faw_ambulance)
                                        .color(Color.RED))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        if (i == 1) {
                            Intent iFav = new Intent(MainActivity.this, FavoriteActivity.class);
                            startActivityForResult(iFav, 1);
                        }
                        if (i == 2) {
                            Intent iSearch = new Intent(MainActivity.this, SearchResultActivity.class);
                            startActivityForResult(iSearch, 2);
                        }
                        if (i == 3) {
                            Intent iAbout = new Intent(MainActivity.this, AboutActivity.class);
                            startActivityForResult(iAbout, 3);
                        }

                        return false;
                    }
                })

                .build();


    }

    private void imageOnTouch() {

        img_doctor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        img_doctor.setImageResource(R.drawable.doctor_press);
                        //img_doctor.setImageBitmap(getResources().getDrawable(R.drawable.docter_pressed));
                        System.out.println("pressed");
                        break;
                    case MotionEvent.ACTION_UP:
                        img_doctor.setImageResource(R.drawable.doctor);
                        System.out.println("cancel pressed");
                        Intent i = new Intent(MainActivity.this, ListDokterActivity.class);
                        i.putExtra("title",getString(R.string.title_dokter));
                        i.putExtra("id",1);
                        startActivity(i);
                        break;
                }

                return true;
            }
        });

        img_apotek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        img_apotek.setImageResource(R.drawable.doctor_press);
                        //img_doctor.setImageBitmap(getResources().getDrawable(R.drawable.docter_pressed));
                        System.out.println("pressed");
                        break;
                    case MotionEvent.ACTION_UP:
                        img_apotek.setImageResource(R.drawable.apotek);
                        System.out.println("cancel pressed");
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        i.putExtra("title",getString(R.string.title_apotek));
                        i.putExtra("id",2);
                        startActivity(i);
                        break;
                }

                return true;
            }
        });

        img_puskes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        img_puskes.setImageResource(R.drawable.ambulance);
                        //img_doctor.setImageBitmap(getResources().getDrawable(R.drawable.docter_pressed));
                        System.out.println("pressed");
                        break;
                    case MotionEvent.ACTION_UP:
                        img_puskes.setImageResource(R.drawable.ambulance);
                        System.out.println("cancel pressed");
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        i.putExtra("title",getString(R.string.title_puskes));
                        i.putExtra("id",3);
                        startActivity(i);
                        break;
                }

                return true;
            }
        });

        img_klinik.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        img_klinik.setImageResource(R.drawable.clinic);
                        //img_doctor.setImageBitmap(getResources().getDrawable(R.drawable.docter_pressed));
                        System.out.println("pressed");
                        break;
                    case MotionEvent.ACTION_UP:
                        img_klinik.setImageResource(R.drawable.clinic);
                        System.out.println("cancel pressed");
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        i.putExtra("title", getString(R.string.title_clinic));
                        i.putExtra("id", 4);
                        startActivity(i);
                        break;
                }

                return true;
            }
        });

        img_rs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        img_rs.setImageResource(R.drawable.hospital);
                        //img_doctor.setImageBitmap(getResources().getDrawable(R.drawable.docter_pressed));
                        System.out.println("pressed");
                        break;
                    case MotionEvent.ACTION_UP:
                        img_rs.setImageResource(R.drawable.hospital);
                        System.out.println("cancel pressed");
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        i.putExtra("title", getString(R.string.title_rs));
                        i.putExtra("id", 5);
                        startActivity(i);
                        break;
                }

                return true;
            }
        });

        img_other.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        img_other.setImageResource(R.drawable.other);
                        //img_doctor.setImageBitmap(getResources().getDrawable(R.drawable.docter_pressed));
                        System.out.println("pressed");
                        break;
                    case MotionEvent.ACTION_UP:
                        img_other.setImageResource(R.drawable.other);
                        System.out.println("cancel pressed");
                        Intent i = new Intent(MainActivity.this, MapsActivity.class);
                        i.putExtra("title", getString(R.string.title_rs));
                        i.putExtra("id", 5);
                        startActivity(i);
                        break;
                }

                return true;
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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


    public void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath = "/data/data/" + this.getPackageName() + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    dir.mkdir();
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen()){
            drawer.closeDrawer();
        }else {
            super.onBackPressed();
        }
    }
}
