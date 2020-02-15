package com.tenilodev.healthyfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.gms.maps.model.LatLng;


public class Pref {
	
	public static final String PREF_NAME = "pref_config";
	public static final String PREF_LATITUDE = "pref_latitude";
	public static final String PREF_LONGITUDE = "pref_longitude";
	
	private Context context;
	private SharedPreferences sp;
	
	public Pref(Context ctx){
		this.context = ctx;
		sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}
	
	public void saveCurrentLocation(double latitude, double longitude){
		Editor edit = sp.edit();
		edit.putLong(PREF_LATITUDE, Double.doubleToRawLongBits(latitude));
		edit.putLong(PREF_LONGITUDE, Double.doubleToRawLongBits(longitude));
		edit.commit();
	}
	
	public LatLng getCurrentLocation(){
		
		double lat = Double.longBitsToDouble(sp.getLong(PREF_LATITUDE, 0));
		double lng = Double.longBitsToDouble(sp.getLong(PREF_LONGITUDE, 0));

		LatLng lokasi = new LatLng(lat, lng);
		return lokasi;

	}
	
	public static LatLng getStaticCurrentLocation(Context ctx){
		
		Pref pref = new Pref(ctx);
		
		double lat = Double.longBitsToDouble(pref.sp.getLong(PREF_LATITUDE, 0));
		double lng = Double.longBitsToDouble(pref.sp.getLong(PREF_LONGITUDE, 0));

		LatLng lokasi = new LatLng(lat, lng);
		return lokasi;

	}



}
