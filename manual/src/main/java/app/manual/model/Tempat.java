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

@DatabaseTable(tableName = "tempat")
public class Tempat {
    @DatabaseField(generatedId = true)
    private int id_tempat;
    @DatabaseField
    private String nama_tempat;
    @DatabaseField
    private String keterangan;
    @DatabaseField
    private String buka;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "id_kategori")
    private Kategori kategori;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "id_koordinat")
    private Koordinat koordinat;
    @DatabaseField
    private int favorite;

    private double jarak;

    public int getId_tempat() {
        return id_tempat;
    }

    public void setId_tempat(int id_tempat) {
        this.id_tempat = id_tempat;
    }

    public Koordinat getKoordinat() {
        return koordinat;
    }

    public void setKoordinat(Koordinat koordinat) {
        this.koordinat = koordinat;
    }

    public String getNama_tempat() {
        return nama_tempat;
    }

    public void setNama_tempat(String nama_tempat) {
        this.nama_tempat = nama_tempat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBuka() {
        return buka;
    }

    public void setBuka(String buka) {
        this.buka = buka;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }
}
