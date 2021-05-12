package com.example.kw_mk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class Order_Main extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Order_Main_Storemgmt order_main_storemgmt = new Order_Main_Storemgmt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main);
    }
}
