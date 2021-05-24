package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("User_Info").document(email);


                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete( Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String result = (String) document.get("store");
                            if (result.equals("0")) {
                                Intent intent = new Intent(Select_Page.this, SellerStoreAdd.class);
                                startActivity(intent);
                                Toast.makeText(Select_Page.this, "Store :: 0", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (result.equals("1")) {
                                Intent intent = new Intent(Select_Page.this, SellerMainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Select_Page.this, "Store :: 1", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                finish();
                            }
                        }
                    }
                });
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
