package com.tenilodev.healthyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tenilodev.healthyfinder.model.Tempat;
import com.tenilodev.healthyfinder.utils.DbUtil;
import com.tenilodev.healthyfinder.utils.GeoUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FavoriteActivity extends BaseActivity {

    private RecyclerView rv;
    private Pref prefence;
    private LinearLayout layoutNotfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initVarUI() {

        prefence = new Pref(this);

        layoutNotfound = (LinearLayout)findViewById(R.id.searchNotfound);
        layoutNotfound.setVisibility(View.GONE);
        rv = (RecyclerView)findViewById(R.id.rv);

        rv.setHasFixedSize(true);
        rv.setScrollbarFadingEnabled(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        List<Tempat> tempatList = null;
        List<Tempat> tempatListSort = new ArrayList<>();

        try {
            Dao<Tempat, Integer> daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(),Tempat.class);
            tempatList = daoTempat.queryForEq("favorite",1);

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

            RVAdapter adapter = new RVAdapter(tempatListSort, 1);
            rv.setAdapter(adapter);

            final List<Tempat> fTempatList = tempatListSort;
            adapter.setOnItemClickListener(new RVAdapter.onItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    //Toast.makeText(getApplicationContext(), finalTempatList.get(position).getNama_tempat(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                    i.putExtra(AppConfig.INTENT_IDTEMPAT, fTempatList.get(position).getId_tempat());
                    startActivity(i);
                }
            });

            if(tempatList.size() == 0){
                //Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                layoutNotfound.setVisibility(View.VISIBLE);
            }else{
                layoutNotfound.setVisibility(View.GONE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idx = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (idx == R.id.action_maps) {
            //Intent intent = new Intent(this, MapsActivity.class);
            //intent.putExtra(AppConfig.INTENT_KATEGORI, id);
            //startActivity(intent);
            return true;
        }

        if(idx == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
