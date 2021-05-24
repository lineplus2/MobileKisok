package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class Select_Page extends AppCompatActivity {

    private Button btn_order;
    private Button btn_customer;
    boolean store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_page);

        DocumentReference doc = App.db.collection("User_Info").document(App.LoginUserEmail);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                store = document.getBoolean("store");

            }
        });

        btn_order = (Button) findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (store == true) { // 판매자 메인화면
                    //Intent intent = new Intent(Select_Page.this, 메인);
                    //startActivity(intent);
                } else if (store == false) { // 가게 추가하는 곳
                    //Intent intent = new Intent(Select_Page.this, 가게추가하는 곳);
                    //startActivity(intent);
                } else {
                    Toast.makeText(Select_Page.this, "Error", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btn_customer = (Button) findViewById(R.id.btn_customer);
        btn_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_Page.this, ConsumerMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
