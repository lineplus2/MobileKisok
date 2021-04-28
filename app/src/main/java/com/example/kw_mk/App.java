package com.example.kw_mk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class App extends Application {

    public static String LoginUserEmail = null;
    public static String LoginUserPw = null;
    public static String LoginUserName = null;
    public static String LoginUserPhone = null;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static GpsTracker gpsTracker;


}
