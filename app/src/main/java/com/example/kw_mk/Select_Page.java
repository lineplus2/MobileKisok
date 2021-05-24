package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Select_Page extends AppCompatActivity {

    private Button btn_order;
    private Button btn_customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_page);


        btn_order = (Button) findViewById(R.id.btn_order);
        btn_customer = (Button) findViewById(R.id.btn_customer);


        // 오더용 버튼
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_order.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;
                        switch (App.LoginUserStore) {
                            case "0":
                                intent = new Intent(Select_Page.this, SellerMainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Select_Page.this, "Store :: 0", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case "1":
                                intent = new Intent(Select_Page.this, SellerStoreAdd.class);
                                startActivity(intent);
                                Toast.makeText(Select_Page.this, "Store :: 1", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            default:
                                Toast.makeText(Select_Page.this, "Error", Toast.LENGTH_SHORT).show();
                                finish();
                        }
                    }
                }, 1000);

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
