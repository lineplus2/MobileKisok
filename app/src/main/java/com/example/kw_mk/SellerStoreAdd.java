package com.example.kw_mk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SellerStoreAdd extends AppCompatActivity {

    ImageView store_image;
    EditText store_name, store_content, store_num, store_address1, store_address2;
    Button store_add_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_store_add);

        store_image = findViewById(R.id.store_image);
        store_name = findViewById(R.id.store_name);
        store_content = findViewById(R.id.store_content);
        store_num = findViewById(R.id.store_num);
        store_address1 = findViewById(R.id.store_address1);
        store_address2 = findViewById(R.id.store_address2);
        store_add_btn = findViewById(R.id.store_add_btn);




    }
}
