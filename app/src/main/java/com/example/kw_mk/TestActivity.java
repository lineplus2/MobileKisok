package com.example.kw_mk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOError;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.test;

public class TestActivity extends AppCompatActivity {
    String TAG = "TestActivity :: ";


    Button testBtn;
    TextView test1, test2, test3;
    EditText testEdit;
    Geocoder geocoder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);

        geocoder = new Geocoder(this);

        testBtn = findViewById(R.id.testBtn);
        test1 = findViewById(R.id.testTextview1);
        test2 = findViewById(R.id.testTextview2);
        test3 = findViewById(R.id.testTextview3);
        testEdit = findViewById(R.id.testEditText);


        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TestActivity.this, MapViewTest.class);
//                startActivity(intent);

                List<Address> list = null;

                String str = testEdit.getText().toString();

                try {
                    list = geocoder.getFromLocationName(str, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TestActivity.this, "에러", Toast.LENGTH_SHORT).show();
                }

                if (list != null) {
                    if (list.size() == 0) {
                        test1.setText("해당되는 주소정보가 없음");
                    } else {
                        test1.setText(String.valueOf(list.get(0).getLatitude()));
                        test2.setText(String.valueOf(list.get(0).getLongitude()));
                    }
                }


            }
        });

    }
}

