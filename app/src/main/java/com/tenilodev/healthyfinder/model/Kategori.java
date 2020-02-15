package com.tenilodev.healthyfinder.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by aisatriani on 17/08/15.
 */
public class Kategori {

    @DatabaseField(id = true)
    private int id_kategori;
    @DatabaseField
    private String nama_kategori;
    @DatabaseField
    private String keterangan;

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public String toString() {
        return getNama_kategori();
    }
}
