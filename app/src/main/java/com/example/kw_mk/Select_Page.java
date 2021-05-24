package com.example.kw_mk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.kw_mk.App.db;

public class Select_Page extends AppCompatActivity {

    private Button btn_order;
    private Button btn_customer;
    boolean store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_page);


        // 오더용 버튼
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_customer = (Button) findViewById(R.id.btn_customer);


        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.LoginUserStore.equals("1")) { // 판매자 메인화면
                    Intent intent = new Intent(Select_Page.this, SellerMainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Select_Page.this, "test1", Toast.LENGTH_SHORT).show();
                } else if (App.LoginUserStore.equals("0")) { // 가게 추가하는 곳
                    Intent intent = new Intent(Select_Page.this, SellerMainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Select_Page.this, "test2", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Select_Page.this, "Error", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        // 소비자 버튼
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
