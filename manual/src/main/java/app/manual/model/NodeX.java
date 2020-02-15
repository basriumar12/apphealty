package app.manual.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by aisatriani on 21/08/15.
 */
@DatabaseTable(tableName = "node")
public class NodeX {

    @DatabaseField(generatedId = true)
    private int idnode;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "id")
    private Koordinat id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "s1")
    private Koordinat s1;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "s2")
    private Koordinat s2;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "s3")
    private Koordinat s3;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "s4")
    private Koordinat s4;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "s5")
    private Koordinat s5;

    public Koordinat getId() {
        return id;
    }

    public void setId(Koordinat id) {
        this.id = id;
    }

    public Koordinat getS1() {
        return s1;
    }

    public void setS1(Koordinat s1) {
        this.s1 = s1;
    }

    public Koordinat getS2() {
        return s2;
    }

    public void setS2(Koordinat s2) {
        this.s2 = s2;
    }

    public Koordinat getS3() {
        return s3;
    }

    public void setS3(Koordinat s3) {
        this.s3 = s3;
    }

    public Koordinat getS4() {
        return s4;
    }

    public void setS4(Koordinat s4) {
        this.s4 = s4;
    }

    public Koordinat getS5() {
        return s5;
    }

    public void setS5(Koordinat s5) {
        this.s5 = s5;
    }

    @Override
    public String toString() {
        return "id " +String.valueOf(getId().getIdkoordinat()) + " s5 :" + String.valueOf(getS5().getIdkoordinat()) ;
    }
}
