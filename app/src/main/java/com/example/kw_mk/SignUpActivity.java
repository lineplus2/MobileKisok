package com.example.kw_mk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";


    String email;
    String password;
    String passwordCheck;
    String name;
    String phone;
    FirebaseAuth mAuth = App.mAuth;

    DatabaseReference mDBReference = null;
    Map<String, Object> userValue = null;
    UserInfo userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Cheak");
        setContentView(R.layout.activity_sign_up);

        this.mDBReference = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);

        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#62B4F5")));

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpButton:
                    signUp();
                    break;
            }
        }
    };

    private void signUp() {
        this.email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        this.password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        this.passwordCheck = ((EditText) findViewById(R.id.passwordCheckEditText)).getText().toString();
        this.name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        this.phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0 && phone.length() > 0 && name.length() > 0) {
            if (password.equals(passwordCheck)) { // 회원가입시 비밀번호 체크
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startToast("회원가입에 성공하였습니다.");
                                    userInfo = new UserInfo(email, password, name, phone); // 유저정보셋팅
                                    userValue = userInfo.toMap(); // 유저정보대입준비

                                    //FireStore 값(유저정보) 삽입
                                    App.db.collection("User_Info").document(email)
                                            .set(userValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startLoginActivity();
                                } else {
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        } else {
            startToast("모든 항목을 채워주세요.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}