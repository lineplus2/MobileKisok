package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Order_Main extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Order_Main_Storemgmt order_main_storemgmt = new Order_Main_Storemgmt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){

        }else{
            myStartActivity(Order_menuActivity.class);
        }


    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
