package com.tenilodev.healthyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tenilodev.healthyfinder.model.SubKategori;
import com.tenilodev.healthyfinder.model.Tempat;
import com.tenilodev.healthyfinder.utils.DbUtil;
import com.tenilodev.healthyfinder.utils.GeoUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListDokterActivity extends BaseActivity {

    private RecyclerView rv;
    private String title;
    private int id;
    private Pref prefence;
    private Spinner spinSort, spinKategori;
    private LinearLayout layoutFilter;
    private List<Tempat> tempatList;
    private RVAdapter adapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("7535AF4EB2936B47ED5B7A27A0547F7F")
                .build();
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7486974203577954/1852043152");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
        interstitialAd.loadAd(adRequest);


    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_list;
    }

    @Override
    public void initVarUI() {

        prefence = new Pref(this);

        title = getIntent().getExtras().getString("title");
        id = getIntent().getExtras().getInt("id");

        rv = (RecyclerView)findViewById(R.id.rv);
        layoutFilter = (LinearLayout)findViewById(R.id.linbottom);
        spinKategori = (Spinner)findViewById(R.id.spinnerSubkategori);

        if(id != 1){
            layoutFilter.setVisibility(View.GONE);
        }


        rv.setHasFixedSize(true);
        rv.setScrollbarFadingEnabled(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        final List<Tempat> tempatListSort = new ArrayList<>();

        try {
            final Dao<Tempat, Integer> daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(),Tempat.class);
            Dao<SubKategori, Integer> daoSub = DaoManager.createDao(DbUtil.getConnectionSource(), SubKategori.class);
            tempatList = daoTempat.queryForEq("id_kategori", id);
            final List<SubKategori> subKategoriList = daoSub.queryForAll();

            final ArrayAdapter<SubKategori> adapterSub = new ArrayAdapter<SubKategori>(this,android.R.layout.simple_spinner_item, subKategoriList);
            adapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinKategori.setAdapter(adapterSub);
            spinKategori.setSelection(11, false);
            spinKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                    SubKategori subKategori = (SubKategori) subKategoriList.get(position);
                    try {
                        if(position == 11){
                            tempatList = daoTempat.queryForEq("id_kategori", id);
                        }else{
                            tempatList = daoTempat.queryBuilder().where().eq("id_kategori", id).and().eq("id_subkategori", subKategori.getId_sub()).query();
                        }

                        for(int x = 0; x < tempatList.size(); x++){
                            double jarak = GeoUtil.distance(prefence.getCurrentLocation().latitude, prefence.getCurrentLocation().longitude, tempatList.get(x).getKoordinat().getLatitude(), tempatList.get(x).getKoordinat().getLongitude());
                            tempatList.get(x).setJarak(jarak);
                        }

                       adapter = new RVAdapter(tempatList, id);
                        rv.setAdapter(adapter);

                        final List<Tempat> fTempatList = tempatList;
                        adapter.setOnItemClickListener(new RVAdapter.onItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                                i.putExtra(AppConfig.INTENT_IDTEMPAT, fTempatList.get(position).getId_tempat());
                                startActivity(i);
                            }
                        });


                        //adapter.notifyDataSetChanged();
                        System.out.println("selected sub : " + subKategori.getNama_subkategori());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
//            spinKategori.post(new Runnable() {
//                public void run() {
//
//
//                }
//            });


            for(Tempat t : tempatList){
                Tempat tpt = new Tempat();
                tpt.setNama_tempat(t.getNama_tempat());
                tpt.setKoordinat(t.getKoordinat());
                tpt.setKategori(t.getKategori());
                tpt.setId_tempat(t.getId_tempat());
                tpt.setBuka(t.getBuka());
                double jarak = GeoUtil.distance(prefence.getCurrentLocation().latitude, prefence.getCurrentLocation().longitude, t.getKoordinat().getLatitude(), t.getKoordinat().getLongitude());
                tpt.setJarak(jarak);
                tpt.setKeterangan(t.getKeterangan());

                tempatListSort.add(tpt);

            }

            Collections.sort(tempatListSort, new Comparator<Tempat>() {
                @Override
                public int compare(Tempat tempat, Tempat t1) {
                    return Double.compare(tempat.getJarak(), t1.getJarak());
                }
            });

            adapter = new RVAdapter(tempatListSort, id);
            rv.setAdapter(adapter);

            final List<Tempat> fTempatList = tempatListSort;
            adapter.setOnItemClickListener(new RVAdapter.onItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                    i.putExtra(AppConfig.INTENT_IDTEMPAT, fTempatList.get(position).getId_tempat());
                    startActivity(i);
                }
            });




        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idx = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (idx == R.id.action_maps) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra(AppConfig.INTENT_KATEGORI, id);
            startActivity(intent);
            return true;
        }

        if(idx == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
