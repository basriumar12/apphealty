package com.tenilodev.healthyfinder;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;


public class AboutActivity extends BaseActivity {

    @Override
    public int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    public void initVarUI() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textVersion = (TextView)findViewById(R.id.textViewVersion);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            String versionName = packageInfo.versionName;
            textVersion.setText("V"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
