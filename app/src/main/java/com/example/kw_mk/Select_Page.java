package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Select_Page extends AppCompatActivity {

    private Button btn_order;
    private Button btn_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_page);

        btn_order = (Button) findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_Page.this, SellerMainActivity.class);
                startActivity(intent);
                finish();
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
