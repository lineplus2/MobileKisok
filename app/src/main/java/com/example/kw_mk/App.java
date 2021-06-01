package com.example.kw_mk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class App extends Application {

    public static final int GET_GALLERY_IMAGE = 200;
    public static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    public static String LoginUserEmail = null;
    public static String LoginUserPw = null;
    public static String LoginUserName = null;
    public static String LoginUserPhone = null;
    public static String LoginUserStore = null;
    public static Uri LoginUserUri = null;
    public static Uri StoreUri = null;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReference();


    public static Uri test = null;
    public static Intent serviceIntent;

    public static void userInit() {
        LoginUserEmail = null;
        LoginUserPw = null;
        LoginUserName = null;
        LoginUserPhone = null;
        LoginUserStore = null;
        LoginUserUri = null;
        StoreUri = null;
    }

    public static ArrayList<payMenuList> payMenuListItem = new ArrayList<>();

    public static GpsTracker gpsTracker;

    public static Location myLocation = new Location("Pi");




}
