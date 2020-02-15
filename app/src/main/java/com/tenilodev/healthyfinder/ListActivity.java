package com.tenilodev.healthyfinder;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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


public class ListActivity extends BaseActivity {

    private RecyclerView rv;
    private String title;
    private int id;
    private Pref prefence;
    private Spinner spinSort, spinKategori;
    private LinearLayout layoutFilter;
    private List<Tempat> tempatList;
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);



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
            tempatList = daoTempat.queryForEq("id_kategori", id);


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
