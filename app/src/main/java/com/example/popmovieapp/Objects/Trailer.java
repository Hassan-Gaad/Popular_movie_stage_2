package com.example.popmovieapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    private String key,size,name,site;
    public Trailer(String key,String name,String site,String size){
        this.key=key;
        this.name=name;
        this.site=site;
        this.size=size;
    }

    public Trailer(){}

    protected Trailer(Parcel in) {
        key = in.readString();
        size = in.readString();
        name = in.readString();
        site = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(size);
        dest.writeString(name);
        dest.writeString(site);
    }
}
