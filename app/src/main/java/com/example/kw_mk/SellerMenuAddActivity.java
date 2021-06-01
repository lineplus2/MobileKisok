package com.example.kw_mk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static com.example.kw_mk.App.GET_GALLERY_IMAGE;
import static com.example.kw_mk.App.db;

public class SellerMenuAddActivity extends AppCompatActivity {


    ImageView menuImage;
    EditText menuName, menuContent, menuPrice;
    Button menuAddBtn;
    ProgressBar loading;
    Uri imageUri;
    Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_menuadd);

        menuImage = findViewById(R.id.sellerfoodImage);
        menuName = findViewById(R.id.sellerfoodname);
        menuContent = findViewById(R.id.sellerfoodexplain);
        menuPrice = findViewById(R.id.sellerfoodprice);
        menuAddBtn = findViewById(R.id.menuaddButton);
        loading = findViewById(R.id.loading3);

        handler = new Handler();

        getSupportActionBar().setTitle("메뉴추가");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        menuAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri Image = imageUri;
                String name = menuName.getText().toString();
                String content = menuContent.getText().toString();
                String Price = menuPrice.getText().toString();

                addMenu(Image, name, content, Price);

                loading.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                        loading.setVisibility(View.INVISIBLE);
                    }
                }, 1500);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            menuImage.setImageURI(imageUri);
        }
    }

    void addMenu(Uri uri, String name, String Content, String Price) {
        HashMap menuInfo = new HashMap();
        menuInfo.put("메뉴이름", name);
        menuInfo.put("메뉴설명", Content);
        menuInfo.put("메뉴가격", Price);

        if (uri != null) {
            App.storageRef.child("Store_Info").child(App.LoginUserEmail).child("Menu").child(name).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }

        db.collection("Store_Info").document(App.LoginUserEmail).collection("Menu").document(name).set(menuInfo);
    }
}
