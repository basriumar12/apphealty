/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.manual.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author aisatriani
 */
@DatabaseTable(tableName = "koordinat")
public class Koordinat implements Comparable<Koordinat> {
    
    @DatabaseField(generatedId = true)
    private int idkoordinat;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lon;
    private double jarak;

    public int getIdkoordinat() {
        return idkoordinat;
    }

    public void setIdkoordinat(int id) {
        this.idkoordinat = id;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double latitude) {
        this.lat = latitude;
    }

    public double getLongitude() {
        return lon;
    }

    public void setLongitude(double longitude) {
        this.lon = longitude;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }


    @Override
    public int compareTo(Koordinat another) {
        return new Double(another.getJarak()).compareTo(jarak);
    }
}
