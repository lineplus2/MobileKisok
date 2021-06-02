package com.example.kw_mk;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class record_contentActivity extends AppCompatActivity {

    TextView name, price, needs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_record_content);

        name = findViewById(R.id.recordName);
        price = findViewById(R.id.recordPrice);
        needs = findViewById(R.id.recordNeeds);

        init_Info();
    }

    void init_Info() {
        name.setText(getIntent().getStringExtra("storeName"));
        price.setText(getIntent().getStringExtra("payPrice"));
        needs.setText(getIntent().getStringExtra("needs"));
    }
}
