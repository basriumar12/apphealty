/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenilodev.healthyfinder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author aisatriani
 */
@DatabaseTable(tableName = "node")
public class Node {
    
    @DatabaseField(generatedId = true)
    private int idnode;
    @DatabaseField
    private int id;
    @DatabaseField
    private int s1;
    @DatabaseField
    private int s2;
    @DatabaseField
    private int s3;
    @DatabaseField
    private int s4;
    @DatabaseField
    private int s5;

    public int getIdnode() {
        return idnode;
    }

    public void setIdnode(int idnode) {
        this.idnode = idnode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getS1() {
        return s1;
    }

    public void setS1(int s1) {
        this.s1 = s1;
    }

    public int getS2() {
        return s2;
    }

    public void setS2(int s2) {
        this.s2 = s2;
    }

    public int getS3() {
        return s3;
    }

    public void setS3(int s3) {
        this.s3 = s3;
    }

    public int getS4() {
        return s4;
    }

    public void setS4(int s4) {
        this.s4 = s4;
    }

    public int getS5() {
        return s5;
    }

    public void setS5(int s5) {
        this.s5 = s5;
    }
    
    
    
    
}
