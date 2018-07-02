package com.conceptioni.cafeapp.model;

import java.util.ArrayList;

/**
 * Created by win10 on 7/2/2018.
 */

public class MenuModel {
    String cid;
    String cname;
    ArrayList<MenuModel>items=new ArrayList<>();

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public ArrayList<MenuModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuModel> items) {
        this.items = items;
    }
}
