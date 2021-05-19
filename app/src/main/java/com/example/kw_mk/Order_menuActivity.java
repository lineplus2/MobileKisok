package com.example.kw_mk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.kw_mk.WUtil.showToast;

import static com.example.kw_mk.WUtil.INTENT_PATH;

public class Order_menuActivity extends BasicActivity {
    private static final String TAG = "Order_menuActivity";
    private ImageView foodimageView;
    private RelativeLayout loaderLayout;
    private String profilePath;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_menuadd);

        loaderLayout = findViewById(R.id.loaderlayout);
        findViewById(R.id.menuaddButton).setOnClickListener(onClickListener);


        foodimageView = findViewById(R.id.foodimageView);
        foodimageView.setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.menuaddButton:
                    menuUpdate();
                    break;
                case R.id.foodimageView:
                    CardView cardView = findViewById(R.id.buttonsCardView);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.picture:
                    myStartActivity(CameraActivity.class);
                    break;
                case R.id.gallery:
                    myStartActivity(GalleryActivity.class, "image", 0);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: {
                if(resultCode == Activity.RESULT_OK){
                    profilePath = data.getStringExtra(INTENT_PATH);
                    Glide.with(this).load(profilePath).centerCrop().override(500).into(foodimageView);
                }
                break;
            }
        }
    }

    private void menuUpdate(){
        final String name = ((EditText)findViewById(R.id.sellerfoodname)).getText().toString();
        final String explain = ((EditText)findViewById(R.id.sellerfoodexplain)).getText().toString();
        final String price = ((EditText)findViewById(R.id.sellerfoodprice)).getText().toString();
        final String category = ((EditText)findViewById(R.id.sellerfoodcategory)).getText().toString();

        if(name.length() > 0 && explain.length() > 0 && price.length() > 0 && category.length() > 0){
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("food/"+user.getUid()+"/foodImage.jpg");
            if(profilePath == null){
                menuInfo menuInfo  = new menuInfo(name, explain, price, category);
                storeUploader(menuInfo);
            }else{
                try{
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                menuInfo menuInfo = new menuInfo(name, explain, price, category, downloadUri.toString());
                                storeUploader(menuInfo);
                            } else {
                                showToast(Order_menuActivity.this, "메뉴정보를 등록하는데 실패하였습니다.");
                            }
                        }
                    });
                }catch(FileNotFoundException e){
                    Log.e("로그","에러: " +e.toString());
                }
            }
        }else{
            startToast("메뉴정보를 입력해주세요.");
        }
    }
    private void storeUploader(menuInfo menuInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(menuInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(Order_menuActivity.this, "메뉴 등록을 성공하였습니다.");
                        loaderLayout.setVisibility(View.GONE);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(Order_menuActivity.this, "메뉴 등록에 실패하였습니다.");
                        loaderLayout.setVisibility(View.GONE);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }

    private void myStartActivity(Class c, String media, int requestCode) {
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        startActivityForResult(intent, requestCode);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
