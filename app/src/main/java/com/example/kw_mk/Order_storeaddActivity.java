/*
package com.example.kw_mk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Order_storeaddActivity extends AppCompatActivity {
    private static final String TAG = "Order_menuActivity";
    private ImageView storeimageView;
    private RelativeLayout loaderLayout;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_store_add);

        loaderLayout = findViewById(R.id.loaderlayout);
        findViewById(R.id.storeaddButton).setOnClickListener(onClickListener);


        storeimageView = findViewById(R.id.storeimageView);
        storeimageView.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.storeaddButton:
                    storeUpdate();
                    break;
                case R.id.storeimageView:
                    CardView cardView = findViewById(R.id.buttonsCardView);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    private void storeUpdate() {
        final String name = ((EditText) findViewById(R.id.storenameText)).getText().toString();
        final String introduce = ((EditText) findViewById(R.id.storeintroduceText)).getText().toString();
        final String tellnumber = ((EditText) findViewById(R.id.storetellnumberText)).getText().toString();
        final String adress = ((EditText) findViewById(R.id.storeadressText)).getText().toString();
        final String adress2 = ((EditText) findViewById(R.id.storeadress2Text)).getText().toString();

        if(name.length() > 0 && introduce.length() > 0 && tellnumber.length() > 0 && adress.length() > 0){
            //loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();
            StoreInfo storeInfo  = new StoreInfo(name, introduce, tellnumber, adress, adress2);
            storeUploader(storeInfo);
        }else{

        }
    }
    private void storeUploader(StoreInfo storeInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("store").document(user.getUid()).set(storeInfo);
    }
}
*/
