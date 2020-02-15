package com.tenilodev.healthyfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by aisatriani on 20/08/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public abstract int getLayoutResource();

    public abstract void initVarUI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        initVarUI();

    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}
