package com.example.kw_mk;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.kw_mk.App.db;

public class record_reviewActivity extends AppCompatActivity {
    Button write;
    EditText review;
    TextView storeName, price, menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_record_review);

        getSupportActionBar().setTitle("리뷰작성");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));

        storeName = findViewById(R.id.StoreName);
        price = findViewById(R.id.buyPrice);
        review = findViewById(R.id.review);
        menu = findViewById(R.id.reviewMenu);

        write = findViewById(R.id.btn_write);

        storeName.setText("testInfo");
        price.setText("10000");


        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewD = review.getText().toString();
                String buyMenu = menu.getText().toString();
                String storeN = storeName.getText().toString();

                reviewWrite(buyMenu, reviewD, storeN);
                finish();
            }
        });
    }


    void reviewWrite(String Menu, String Review, String StoreN) {
        DocumentReference doc = db.collection("Store_Info").document(StoreN).collection("review").document(App.LoginUserEmail);

        HashMap<String, Object> result = new HashMap<>();
        result.put("주문자", App.LoginUserEmail);
        result.put("주문메뉴", Menu);
        result.put("리뷰", Review);
        result.put("작성시간", FieldValue.serverTimestamp());

        doc.update(result);
    }
}
