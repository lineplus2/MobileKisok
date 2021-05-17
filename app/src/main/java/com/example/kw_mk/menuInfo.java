package com.example.kw_mk;

import android.widget.EditText;

public class menuInfo {
    private String name;
    private String explain;
    private String price;
    private String category;

    public menuInfo(String name, String explain, String price, String category){
        this.name = name;
        this.explain = explain;
        this.price = price;
        this.category = category;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getExplain(){
        return this.explain;
    }
    public void setExplain(String explain){
        this.explain = explain;
    }

    public String getPrice(){
        return this.price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }
}
