package com.example.kw_mk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.kw_mk.LoginActivity.pref;


public class MypageModifyActivity extends Activity {

    Button modify;

    EditText exPw, chPw, chPwC;

    DocumentReference docRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_mypage_modify);

        modify = findViewById(R.id.modify);
        chPw = findViewById(R.id.changePassword);
        chPwC = findViewById(R.id.changePasswordCheck);
        exPw = findViewById(R.id.exPassword);

        final FirebaseUser user = App.mAuth.getCurrentUser();
        docRef = App.db.collection("User_Info").document(user.getEmail());


        if (user != null) {
        } else {
            Toast.makeText(this, "유저 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String existingPassword = exPw.getText().toString();
                final String newPassword = chPw.getText().toString();
                final String newPasswordCheck = chPwC.getText().toString();

                if (!existingPassword.equals(App.LoginUserPw)) {
                    Toast.makeText(MypageModifyActivity.this, "기존 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                } else if (existingPassword.equals(newPassword)) {
                    Toast.makeText(MypageModifyActivity.this, "변경하려는 비밀번호와 기존 비밀번호가 같습니다.", Toast.LENGTH_SHORT).show();
                } else if (newPassword == null || newPasswordCheck == null) {
                    Toast.makeText(MypageModifyActivity.this, "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (newPassword.length() < 6) {
                    Toast.makeText(MypageModifyActivity.this, "비밀번호를 6자 이상으로 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(newPasswordCheck)) {
                    Toast.makeText(MypageModifyActivity.this, "변경하려는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {
                    // 비밀번호 변경 ( 변경 성공시 로그아웃 )
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MypageModifyActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                        // user_profile pw 변경
                                        docRef.update("pw", newPassword);
                                        modifycheack();
                                    }
                                }
                            });
                }
            }
        });

    }

    void modifycheack() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID", "");
        editor.putString("PWD", "");

        editor.commit();
        ConsumerMainActivity.AActivity.finish();
        finish();
    }
}
