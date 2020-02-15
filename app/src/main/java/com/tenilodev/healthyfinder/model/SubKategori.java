package com.tenilodev.healthyfinder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by My Thosiba on 15/12/2015.
 */
@DatabaseTable(tableName = "subkategori")
public class SubKategori {
    @DatabaseField(generatedId = true)
    private int id_sub;
    @DatabaseField
    private String nama_subkategori;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "id_kategori")
    private Kategori kategori;

    public int getId_sub() {
        return id_sub;
    }

    public void setId_sub(int id_sub) {
        this.id_sub = id_sub;
    }

    public String getNama_subkategori() {
        return nama_subkategori;
    }

    public void setNama_subkategori(String nama_subkategori) {
        this.nama_subkategori = nama_subkategori;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString() {
        return getNama_subkategori();
    }
}
