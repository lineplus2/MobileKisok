package com.example.kw_mk;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;

import static com.example.kw_mk.App.db;

public class record_reviewActivity extends AppCompatActivity {
    Button write;
    EditText review;
    TextView storeName, price, menu;

    String email;

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

        storeName.setText(getIntent().getStringExtra("storeName"));
        price.setText(getIntent().getStringExtra("payPrice"));
        menu.setText(getIntent().getStringExtra("menuList"));
        email = getIntent().getStringExtra("email");

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();


        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buyMenu = menu.getText().toString();
                String reviewD = review.getText().toString();
                String storeN = storeName.getText().toString();

                reviewWrite(buyMenu, reviewD, storeN);
                finish();
            }
        });
    }


    void reviewWrite(String Menu, String Review, String StoreN) {


        HashMap<String, Object> result = new HashMap<>();
        result.put("주문자", App.LoginUserEmail);
        result.put("주문자명", App.LoginUserName);
        result.put("리뷰", Review);
        result.put("작성시간", FieldValue.serverTimestamp());
        result.put("주문목록", menu.getText().toString());

        db.collection("Store_Info").document(email).collection("Review").document().set(result);
    }
}
