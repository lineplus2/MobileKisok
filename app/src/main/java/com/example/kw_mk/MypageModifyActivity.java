package com.example.kw_mk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MypageModifyActivity extends Activity {
    String TAG = "MyPageModifyActivity";

    FirebaseFirestore db = App.db;
    String ConPassword;

    EditText password, chPassword, chPasswordCheck;

    Button modify;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_mypage_modify);

        password = findViewById(R.id.exPassword);
        chPassword = findViewById(R.id.changePassword);
        chPasswordCheck = findViewById(R.id.changePasswordCheck);

        modify = findViewById(R.id.modify);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chPassword.getText().toString() == chPasswordCheck.getText().toString()) {
                    changePassword(password.getText().toString(), chPassword.getText().toString());
                } else if (chPassword.getText().toString() != chPasswordCheck.getText().toString()) {
                    Toast.makeText(MypageModifyActivity.this, "변경 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString() == null || chPassword.getText().toString() == null || chPasswordCheck.getText().toString() == null) {
                    Toast.makeText(MypageModifyActivity.this, "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void changePassword(String password, String chPassword) {
        DocumentReference docRef = db.collection("User_Info").document(App.LoginUserEmail);


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ConPassword = (String) document.get("pw");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        if (ConPassword == password) {
            docRef.update("pw", chPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MypageModifyActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else if (ConPassword != password) {
            Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
        }

    }
}
