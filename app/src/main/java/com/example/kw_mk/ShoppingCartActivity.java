package com.example.kw_mk;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.example.kw_mk.App.imageOptions;
import static com.example.kw_mk.App.payMenuListItem;
import static com.example.kw_mk.App.payMenuListItem2;
import static com.example.kw_mk.App.storageRef;

public class ShoppingCartActivity extends AppCompatActivity {

    TextView menuName, menuPrice, mount;
    Button addCart, incBtn, decBtn;
    ImageView menuImg;
    String amount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_home_menu_add);

        menuName = findViewById(R.id.shoppingName);
        menuPrice = findViewById(R.id.shoppingPrice);
        addCart = findViewById(R.id.addCart);
        menuImg = findViewById(R.id.shoppingImg);
        mount = findViewById(R.id.mount);
        incBtn = findViewById(R.id.incBtn);
        decBtn = findViewById(R.id.decBtn);

        initData();

    }

    void initData() {
        String name = getIntent().getStringExtra("menuName");
        String price = getIntent().getStringExtra("menuPrice");
        String email = getIntent().getStringExtra("email");

        storageRef.child("Store_Info").child(email).child("Menu").child(name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ShoppingCartActivity.this).load(uri).apply(imageOptions).into(menuImg);
            }
        });

        menuName.setText(name);
        menuPrice.setText(price);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = mount.getText().toString();
                payMenuListItem.add(new payMenuList(menuName.getText().toString(), menuPrice.getText().toString(), amount));
                payMenuListItem2.add(new payMenuList(menuName.getText().toString(), menuPrice.getText().toString(), amount));
                finish();
            }
        });

        incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(mount.getText().toString());
                a++;
                mount.setText(String.valueOf(a));
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(mount.getText().toString());
                if (a > 1) {
                    a--;
                }
                mount.setText(String.valueOf(a));
            }
        });


    }
}
