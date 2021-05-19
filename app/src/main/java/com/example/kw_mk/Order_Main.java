package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Order_Main extends AppCompatActivity {
    private static final String TAG = "Order_Main";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Order_Main_Storemgmt order_main_storemgmt = new Order_Main_Storemgmt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if(firebaseUser == null){
            myStartActivity(Order_storeaddActivity.class);
        }else{
            firebaseFirestore = FirebaseFirestore.getInstance();
            myStartActivity(Order_storeaddActivity.class);

        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_main_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.order_store:
                break;
            case R.id.order_menu:
                myStartActivity(Order_menuActivity.class);
                break;
            case R.id.order_order:
                break;
            case R.id.order_review:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
