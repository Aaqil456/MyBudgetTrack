package com.example.mybudgettrack.Item;

public class BajetItem {
    private String namaBajet;
    private String jumlahBajet;
    private String tempohBajet;


    public BajetItem(String namaBajet, String jumlahBajet, String tempohBajet) {
        this.namaBajet = namaBajet;
        this.jumlahBajet = jumlahBajet;
        this.tempohBajet = tempohBajet;
    }

    public String getNamaBajet() {
        return namaBajet;
    }

    public String getJumlahBajet() {
        return jumlahBajet;
    }

    public String getTempohBajet() {
        return tempohBajet;
    }
}
