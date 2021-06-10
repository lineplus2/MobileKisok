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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_store_add);

        store_image = (ImageView) findViewById(R.id.store_image);
        store_name = (EditText) findViewById(R.id.store_name);
        store_content = (EditText) findViewById(R.id.store_content);
        store_num = (EditText) findViewById(R.id.store_num);
        store_ownerName = findViewById(R.id.store_ownerName);
        store_buinessNum = findViewById(R.id.store_businessnum);
        store_address1 = (TextView) findViewById(R.id.store_address1);
        store_address2 = (EditText) findViewById(R.id.store_address2);
        store_add_btn = (Button) findViewById(R.id.store_add_btn);
        addres_details = (Button) findViewById(R.id.addres_details);
        categorySpinner = findViewById(R.id.spinner);
        geocoder = new Geocoder(this);

        setCategory();


        getSupportActionBar().setTitle("매장등록");
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
                        category = "한식";
                        break;
                    case 1:
                        category = "일식";
                        break;
                    case 2:
                        category = "중식";
                        break;
                    case 3:
                        category = "양식";
                        break;
                    case 4:
                        category = "패스트푸드";
                        break;
                    case 5:
                        category = "분식";
                        break;
                    case 6:
                        category = "베이커리";
                        break;
                    case 7:
                        category = "찜탕";
                        break;
                    case 8:
                        category = "술집";
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
            Toast.makeText(this, "상호명을 입력해주세요", Toast.LENGTH_LONG).show();
        } else if (content.equals("")) {
            Toast.makeText(this, "설명란을 입력해주세요.", Toast.LENGTH_LONG).show();
        } else if (num.equals("")) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("가게이름", name);
            result.put("가게소개", content);
            result.put("전화번호", num);
            result.put("사업자이메일", App.LoginUserEmail);
            result.put("주소", address1);
            result.put("상세주소", address2);
            result.put("사업자명", ownerName);
            result.put("사업자번호", businessnumber);
            result.put("카테고리", category);

            //위,경도 구하기
            List<Address> list = null;
            try {
                list = geocoder.getFromLocationName(address1, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (list != null) {
                if (list.size() == 0) {
                } else {
                    result.put("위도", String.valueOf(list.get(0).getLatitude()));
                    result.put("경도", String.valueOf(list.get(0).getLongitude()));
                }
            } else {
                result.put("위도", "0");
                result.put("경도", "0");
            }

            // db 등록
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
