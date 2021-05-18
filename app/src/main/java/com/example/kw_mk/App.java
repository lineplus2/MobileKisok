package com.example.kw_mk;

import android.app.Application;
import android.location.Location;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class App extends Application {

    public static String LoginUserEmail = null;
    public static String LoginUserPw = null;
    public static String LoginUserName = null;
    public static String LoginUserPhone = null;
    public static Uri LoginUserUri = null;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReference();


    public static GpsTracker gpsTracker;

    public static Location testLo;


}
