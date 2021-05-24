package com.example.kw_mk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SellerStoreAdd extends AppCompatActivity {

    ImageView store_image;
    EditText store_name, store_content, store_num, store_address2;
    Button store_add_btn, addres_details;
    TextView store_address1;

    String name, content, num, address1, address2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_store_add);

        store_image = (ImageView)findViewById(R.id.store_image);
        store_name = (EditText)findViewById(R.id.store_name);
        store_content = (EditText)findViewById(R.id.store_content);
        store_num = (EditText)findViewById(R.id.store_num);
        store_address1 = (TextView)findViewById(R.id.store_address1);
        store_address2 = (EditText)findViewById(R.id.store_address2);
        store_add_btn = (Button)findViewById(R.id.store_add_btn);
        addres_details = (Button)findViewById(R.id.addres_details);

        store_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_add_btn.setEnabled(false);

            }
        });



    }

    private void store_add() {

        final FirebaseFirestore db;

        this.name = store_name.getText().toString();
        this.content = store_content.getText().toString();
        this.num = store_num.getText().toString();
        this.address1 = store_address1.getText().toString();
        this.address2 = store_address2.getText().toString();
        if(name.equals("")){
            Toast.makeText(this, "상호명을 입력해주세요", Toast.LENGTH_LONG).show();
        } else if (content.equals("")) {
            Toast.makeText(this, "설명란을 입력해주세요.", Toast.LENGTH_LONG).show();
        }else if (num.equals("")) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show();
        }else if (address1.equals("")&&address2.equals("")) {
            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_LONG).show();
        }

    }
}
