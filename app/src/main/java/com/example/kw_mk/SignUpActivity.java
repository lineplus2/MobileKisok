package com.example.kw_mk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    String email;
    String password;
    String name;
    String phone;

    DatabaseReference mDBReference = null;
    HashMap<String, Object> childUpdates = null;
    Map<String, Object> userValue = null;
    UserInfo userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.mDBReference = FirebaseDatabase.getInstance().getReference();
        this.childUpdates = new HashMap<>();


        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
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
                    String ID = "dddd";
                    signUp();
                    userInfo = new UserInfo(ID, password, name, phone);
                    userValue = userInfo.toMap();
                    childUpdates.put("/User_info/" + ID, userValue);
                    mDBReference.updateChildren(childUpdates); // 데이터베이스 값 삽입

                    mDBReference.child("User_info").child(ID).child("pw").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() { // 데이터베이스 값 조회
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) { // 실패시
                                Log.e(TAG, "Error", task.getException());
                            } else { // 성공시
                                Log.d(TAG, String.valueOf(task.getResult().getValue()));
                            }
                        }
                    });
                    break;
            }
        }
    };

    private void signUp() {
        this.email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        this.password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        this.name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        this.phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}

class UserInfo {
    public String email;
    public String pw;
    public String name;
    public String phone;

    public UserInfo() {
    }

    public UserInfo(String id, String pw, String name, String phone) {
        this.email = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("pw", pw);
        result.put("name", name);
        result.put("phone", phone);

        return result;
    }
}