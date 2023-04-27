package com.example.mybudgettrack.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class BajetItem implements Parcelable {
    private String namaBajet;
    private String jumlahBajet;
    private String tempohBajet;

    public BajetItem() {
    }

    public BajetItem(String namaBajet, String jumlahBajet, String tempohBajet) {
        this.namaBajet = namaBajet;
        this.jumlahBajet = jumlahBajet;
        this.tempohBajet = tempohBajet;
    }

    protected BajetItem(Parcel in) {
        namaBajet = in.readString();
        jumlahBajet = in.readString();
        tempohBajet = in.readString();
    }

    public static final Creator<BajetItem> CREATOR = new Creator<BajetItem>() {
        @Override
        public BajetItem createFromParcel(Parcel in) {
            return new BajetItem(in);
        }

        @Override
        public BajetItem[] newArray(int size) {
            return new BajetItem[size];
        }
    };



    public String getNamaBajet() {
        return namaBajet;
    }

    public String getJumlahBajet() {
        return jumlahBajet;
    }

    public String getTempohBajet() {
        return tempohBajet;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namaBajet);
        dest.writeString(jumlahBajet);
        dest.writeString(tempohBajet);

    }
}
