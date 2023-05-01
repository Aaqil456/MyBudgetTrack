package com.example.mybudgettrack.Model;

public class BajetModel {
     String id, wangBajet, tarikhBajet, peneranganBajet;

    public BajetModel() {
    }

    public BajetModel(String id, String wangBajet, String tarikhBajet, String peneranganBajet) {
        this.id = id;
        this.wangBajet = wangBajet;
        this.tarikhBajet = tarikhBajet;
        this.peneranganBajet = peneranganBajet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWangBajet() {
        return wangBajet;
    }

    public void setWangBajet(String wangBajet) {
        this.wangBajet = wangBajet;
    }

    public String getTarikhBajet() {
        return tarikhBajet;
    }

    public void setTarikhBajet(String tarikhBajet) {
        this.tarikhBajet = tarikhBajet;
    }

    public String getPeneranganBajet() {
        return peneranganBajet;
    }

    public void setPeneranganBajet(String peneranganBajet) {
        this.peneranganBajet = peneranganBajet;
    }
}
