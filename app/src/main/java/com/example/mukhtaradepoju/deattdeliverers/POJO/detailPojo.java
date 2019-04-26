package com.example.mukhtaradepoju.deattdeliverers.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mukhtaradepoju.deattdeliverers.DeliveryOrdersQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class detailPojo implements Parcelable {

   String name;
    String id;
    List<DeliveryOrdersQuery.Order> itemList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DeliveryOrdersQuery.Order> getItemList() {
        return itemList;
    }

    public void setItemList(List<DeliveryOrdersQuery.Order> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeList(this.itemList);
    }

    public detailPojo() {
    }

    protected detailPojo(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.itemList = new ArrayList<DeliveryOrdersQuery.Order>();
        in.readList(this.itemList, DeliveryOrdersQuery.Order.class.getClassLoader());
    }

    public static final Parcelable.Creator<detailPojo> CREATOR = new Parcelable.Creator<detailPojo>() {
        @Override
        public detailPojo createFromParcel(Parcel source) {
            return new detailPojo(source);
        }

        @Override
        public detailPojo[] newArray(int size) {
            return new detailPojo[size];
        }
    };
}
