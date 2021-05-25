package com.example.kw_mk;

public class StoreInfo {
    private String name;
    private String content;
    private String num;
    private String address1;
    private String address2;

    public StoreInfo(String name, String introduce, String tellnumber, String adress, String adress2) {
        this.name = name;
        this.content = introduce;
        this.num = tellnumber;
        this.address1 = address1;
        this.address2 = address2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
}
