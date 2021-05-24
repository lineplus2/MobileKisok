package com.example.kw_mk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

import static com.example.kw_mk.LoginActivity.pref;


public class MypageModifyActivity extends AppCompatActivity {

    Button modify;

    EditText exPw, chPw, chPwC;

    DocumentReference docRef;

    ImageView profileImage;

    FirebaseUser user;

    Uri selectedImageUri;

    private final int GET_GALLERY_IMAGE = 200;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_mypage_modify);

        modify = findViewById(R.id.modify);
        chPw = findViewById(R.id.changePassword);
        chPwC = findViewById(R.id.changePasswordCheck);
        exPw = findViewById(R.id.exPassword);
        profileImage = findViewById(R.id.profileImageModify);

        user = App.mAuth.getCurrentUser();
        docRef = App.db.collection("User_Info").document(user.getEmail());

        if (user != null) {
        } else {
            Toast.makeText(this, "유저 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }


        // 프로필사진 변경
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });


        // 수정 버튼
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifycheack();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri);
        }
    }

    boolean passwordModify() {
        final String existingPassword = exPw.getText().toString();
        final String newPassword = chPw.getText().toString();
        final String newPasswordCheck = chPwC.getText().toString();

        if (!existingPassword.equals(App.LoginUserPw)) {
            Toast.makeText(MypageModifyActivity.this, "기존 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (existingPassword.equals(newPassword)) {
            Toast.makeText(MypageModifyActivity.this, "변경하려는 비밀번호와 기존 비밀번호가 같습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPassword == null || newPasswordCheck == null) {
            Toast.makeText(MypageModifyActivity.this, "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPassword.length() < 6) {
            Toast.makeText(MypageModifyActivity.this, "비밀번호를 6자 이상으로 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPassword.equals(newPasswordCheck)) {
            Toast.makeText(MypageModifyActivity.this, "변경하려는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MypageModifyActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                // user_profile pw 변경
                                docRef.update("pw", newPassword);
                            }
                        }
                    });
            return true;
        }
    }


    void modifycheack() {
        // 비밀번호 변경 시
        if (passwordModify()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            ConsumerMainActivity.AActivity.finish();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ID", "");
            editor.putString("PWD", "");

            editor.commit();
        }
        // 프로필 사진 번경 시
        if (selectedImageUri != null) {
            App.storageRef.child("User_Info").child(App.LoginUserEmail).child("profileImage").putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MypageModifyActivity.this, "성공", Toast.LENGTH_SHORT).show();
                    StorageReference stoRef;
                    stoRef = App.storageRef.child("User_Info").child(App.LoginUserEmail+"/profileImage");
                    stoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            App.LoginUserUri = uri;
                            Glide.with(FragmentConsumerMypage.conContext).load(App.LoginUserUri).into(FragmentConsumerMypage.profileImage);
                        }
                    });
                }
            });
        }

        finish();
    }
}
