package com.example.kw_mk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_login;
    private FirebaseAuth mAuth;
    String Email, Password;
    String TAG = "LoginActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);

        btn_signup.setOnClickListener(onClickListener);
        btn_login.setOnClickListener(onClickListener);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    Login();
                    break;
                case R.id.btn_signup:
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
            }
        }
    };


    private void Login() {  //로그인
        Email = ((EditText) findViewById(R.id.et_id)).getText().toString();
        Password = ((EditText) findViewById(R.id.et_pw)).getText().toString();

        if (TextUtils.isEmpty(Email)) {
            return;
        } else if (TextUtils.isEmpty(Password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, Select_Page.class);
                    LoginUserDataSet(Email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginUserDataSet(String Email) {  // 로그인유저 데이터 저장
        DocumentReference docRef = db.collection("User_Info").document(Email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        App.LoginUserEmail = (String) document.get("email");
                        App.LoginUserPw = (String) document.get("pw");
                        App.LoginUserName = (String) document.get("name");
                        App.LoginUserPhone = (String) document.get("phone");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}