package com.tenilodev.healthyfinder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.tenilodev.healthyfinder.model.Tempat;
import com.tenilodev.healthyfinder.utils.DbUtil;
import com.tenilodev.healthyfinder.utils.GeoUtil;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SearchResultActivity extends BaseActivity {

    private Pref prefence;
    private RecyclerView rv;
    private SearchView searchView;
    private LinearLayout layoutNotfound;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initVarUI() {

        prefence = new Pref(this);

        rv = (RecyclerView)findViewById(R.id.rv);

        rv.setHasFixedSize(true);
        rv.setScrollbarFadingEnabled(true);

        layoutNotfound = (LinearLayout)findViewById(R.id.searchNotfound);
        layoutNotfound.setVisibility(View.GONE);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = new SearchView(this);
        getSupportActionBar().setCustomView(searchView);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleQuery(query);
                System.out.println("seach on click");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //searchView.onActionViewExpanded();

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(intent.getExtras() == null){
            //Toast.makeText(this,"intent null",Toast.LENGTH_SHORT).show();
            if(searchView.getQuery().equals("")){

            }
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            searchView.setQuery(query,true);

            handleQuery(query);
            System.out.print("query search :" +query);
        }
    }

    private void handleQuery(String query) {

        try {

            Dao<Tempat, Integer> daoTempat = DaoManager.createDao(DbUtil.getConnectionSource(), Tempat.class);
            final List<Tempat> listTempat = daoTempat.queryBuilder().where().like("nama_tempat","%"+query+"%").or().like("keterangan","%"+query+"%").query();

            for (int i = 0; i < listTempat.size(); i++){
                double jarak = GeoUtil.distance(prefence.getCurrentLocation().latitude, prefence.getCurrentLocation().longitude, listTempat.get(i).getKoordinat().getLatitude(), listTempat.get(i).getKoordinat().getLongitude());
                listTempat.get(i).setJarak(jarak);
            }

            Collections.sort(listTempat, new Comparator<Tempat>() {
                @Override
                public int compare(Tempat tempat, Tempat t1) {
                    return Double.compare(tempat.getJarak(),t1.getJarak());
                }
            });

            RVAdapter adapter = new RVAdapter(listTempat,1);
            rv.setAdapter(adapter);

            adapter.setOnItemClickListener(new RVAdapter.onItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {

                    Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                    i.putExtra(AppConfig.INTENT_IDTEMPAT, listTempat.get(position).getId_tempat());
                    startActivity(i);
                }
            });

            if(listTempat.size() == 0){
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                layoutNotfound.setVisibility(View.VISIBLE);
            }else{
                layoutNotfound.setVisibility(View.GONE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_button) {
            searchView.setQuery(searchView.getQuery(), true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setQuery(searchView.getQuery(), true);
    }
}
