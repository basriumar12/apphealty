package com.tenilodev.healthyfinder.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.*;


public class DbUtil {

    private static ConnectionSource connectionSource;

    private static SQLiteDatabase database;

    public static final String databaseName = "dbkes.db";
    public static final String appFolder = "healthyfinder";
    public static final String databasePath = Environment.getExternalStorageDirectory().toString() + "/" + appFolder;
    public static final int databaseVersion = 1;



    public static ConnectionSource getConnectionSource(){

        if(connectionSource == null){
            connectionSource = new AndroidConnectionSource(getDatabase());
        }

        return connectionSource;
    }

    public static SQLiteDatabase getDatabase(){
        if(database == null) {
            database = SQLiteDatabase.openDatabase(getFullPathDb(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }

        return database;
    }

    public static String getFullPathDb() {
        //String path = databasePath + File.separator + databaseName;
        String path = "/data/data/com.tenilodev.healthyfinder/dbkes.db";
        return path;
    }




}