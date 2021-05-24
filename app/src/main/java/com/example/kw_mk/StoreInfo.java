package com.example.kw_mk;

import android.widget.EditText;

public class StoreInfo {
    private String name;
    private String introduce;
    private String tellnumber;
    private String adress;
    private String adress2;

    public StoreInfo(String name, String introduce, String tellnumber, String adress, String adress2) {
        this.name = name;
        this.introduce = introduce;
        this.tellnumber = tellnumber;
        this.adress = adress;
        this.adress2 = adress2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTellnumber() {
        return this.tellnumber;
    }

    public void setTellnumber(String tellnumber) {
        this.tellnumber = tellnumber;
    }

    public String getAdress() {
        return this.adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getAdress2() {
        return this.adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
    }
}
