package com.example.mybudgettrack.Model;

public class BilModel {
    String id, wangBil, tarikhBayar, peneranganBil;

    public BilModel() {
    }

    public BilModel(String id, String wangBil, String tarikhBayar, String peneranganBil) {
        this.id = id;
        this.wangBil = wangBil;
        this.tarikhBayar = tarikhBayar;
        this.peneranganBil = peneranganBil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWangBil() {
        return wangBil;
    }

    public void setWangBil(String wangBil) {
        this.wangBil = wangBil;
    }

    public String getTarikhBayar() {
        return tarikhBayar;
    }

    public void setTarikhBayar(String tarikhBayar) {
        this.tarikhBayar = tarikhBayar;
    }

    public String getPeneranganBil() {
        return peneranganBil;
    }

    public void setPeneranganBil(String peneranganBil) {
        this.peneranganBil = peneranganBil;
    }
}
