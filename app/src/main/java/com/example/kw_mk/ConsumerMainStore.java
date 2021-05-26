package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.kw_mk.App.db;

public class ConsumerMainStore extends AppCompatActivity {

    TextView StoreName, StoreNum, StoreContent, StoreOwner, StoreAddress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_store);

        StoreName = findViewById(R.id.Store_Info_Name);
        StoreNum = findViewById(R.id.Store_Info_Num);
        StoreContent = findViewById(R.id.Store_Info_Content);
        StoreOwner = findViewById(R.id.Store_Info_Owner);
        StoreAddress = findViewById(R.id.Store_Info_Address);


        init_Info();
    }

    void init_Info() {
        String email = getIntent().getStringExtra("Email");
        db.collection("Store_Info").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                StoreName.setText(document.get("가게이름").toString());
                StoreNum.setText(document.get("전화번호").toString());
                StoreContent.setText(document.get("가게소개"). toString());
                StoreOwner.setText(document.get("사업자이메일").toString());
                StoreAddress.setText(document.get("주소").toString());
            }
        });


    }
}
