package com.example.kw_mk;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {     // UserDataSet
    public String email;
    public String pw;
    public String name;
    public String phone;
    public String store;

    public UserInfo(String id, String pw, String name, String phone) {
        this.email = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.store = "0";
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("pw", pw);
        result.put("name", name);
        result.put("phone", phone);
        result.put("store", store);
        return result;
    }
}
