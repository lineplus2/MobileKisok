package com.example.kw_mk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.kw_mk.App.GET_GALLERY_IMAGE;
import static com.example.kw_mk.App.SEARCH_ADDRESS_ACTIVITY;
import static com.example.kw_mk.App.db;

public class SellerStoreAdd extends AppCompatActivity {

    ImageView store_image;
    EditText store_name, store_content, store_num, store_ownerName, store_buinessNum, store_address2;
    Button store_add_btn, addres_details;
    TextView store_address1;
    Spinner categorySpinner;

    String name, content, num, ownerName, businessnumber, address1, address2, category;

    ArrayAdapter<String> arrayAdater;
    String[] arr;

    Uri selectedImageUri;
    Geocoder geocoder;

    List<Address> geolist = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_store_add);

        store_image = findViewById(R.id.store_image);
        store_name = findViewById(R.id.store_name);
        store_content = findViewById(R.id.store_content);
        store_num = findViewById(R.id.store_num);
        store_ownerName = findViewById(R.id.store_ownerName);
        store_buinessNum = findViewById(R.id.store_businessnum);
        store_address1 = findViewById(R.id.store_address1);
        store_address2 = findViewById(R.id.store_address2);
        store_add_btn = findViewById(R.id.store_add_btn);
        addres_details = findViewById(R.id.addres_details);
        categorySpinner = findViewById(R.id.spinner);
        geocoder = new Geocoder(this);

        setCategory();


        getSupportActionBar().setTitle("????????????");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


        store_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        store_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_add();

                Intent intent = new Intent(SellerStoreAdd.this, SellerMainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        addres_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerStoreAdd.this, AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            store_image.setImageURI(selectedImageUri);
        }

        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String addressData = data.getExtras().getString("data");
                    if (data != null) {
                        store_address1.setText(addressData);
                    }
                }
                break;
        }
    }

    void setCategory() {
        arr = getResources().getStringArray(R.array.category);
        arrayAdater = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        categorySpinner.setAdapter(arrayAdater);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        category = "??????";
                        break;
                    case 1:
                        category = "??????";
                        break;
                    case 2:
                        category = "??????";
                        break;
                    case 3:
                        category = "??????";
                        break;
                    case 4:
                        category = "???????????????";
                        break;
                    case 5:
                        category = "??????";
                        break;
                    case 6:
                        category = "????????????";
                        break;
                    case 7:
                        category = "??????";
                        break;
                    case 8:
                        category = "??????";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void store_add() {

        this.name = store_name.getText().toString();
        this.content = store_content.getText().toString();
        this.num = store_num.getText().toString();
        this.address1 = store_address1.getText().toString();
        this.address2 = store_address2.getText().toString();
        this.ownerName = store_ownerName.getText().toString();
        this.businessnumber = store_buinessNum.getText().toString();

        if (name.equals("")) {
            Toast.makeText(this, "???????????? ??????????????????", Toast.LENGTH_LONG).show();
        } else if (content.equals("")) {
            Toast.makeText(this, "???????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else if (num.equals("")) {
            Toast.makeText(this, "??????????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("????????????", name);
            result.put("????????????", content);
            result.put("????????????", num);
            result.put("??????????????????", App.LoginUserEmail);
            result.put("??????", address1);
            result.put("????????????", address2);
            result.put("????????????", ownerName);
            result.put("???????????????", businessnumber);
            result.put("????????????", category);

            //???,?????? ?????????

            geolist = new ArrayList<>();
            String address = address1;

            try {
                geolist = geocoder.getFromLocationName(address, 1);
            } catch (IOException e) {
                Toast.makeText(this, "???????????? ??????", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            if (geolist != null) {
                if (geolist.size() == 0) {
                } else {
                    result.put("??????", String.valueOf(geolist.get(0).getLatitude()));
                    result.put("??????", String.valueOf(geolist.get(0).getLongitude()));
                }
            } else {
                result.put("??????", "0");
                result.put("??????", "0");
            }

            // db ??????
            DocumentReference docref = db.collection("Store_Info").document(App.LoginUserEmail);
            DocumentReference userdoc = db.collection("User_Info").document(App.LoginUserEmail);
            userdoc.update("store", "1");
            docref.set(result);
        }

        if (selectedImageUri != null) {
            App.storageRef.child("Store_Info").child(App.LoginUserEmail).child("storeImage").putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference stoRef;
                    stoRef = App.storageRef.child("Store_Info").child(App.LoginUserEmail + "/storeImage");
                    stoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            App.StoreUri = uri;
                        }
                    });
                }
            });
        }

    }
}
