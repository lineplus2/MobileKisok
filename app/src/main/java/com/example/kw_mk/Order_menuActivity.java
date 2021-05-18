package com.example.kw_mk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Order_menuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_menuadd);

        findViewById(R.id.menuaddButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.menuaddButton:
                menuUpdate();
                break;
            }
        }
    };

    private void menuUpdate(){
        String name = ((EditText)findViewById(R.id.sellerfoodname)).getText().toString();
        String explain = ((EditText)findViewById(R.id.sellerfoodexplain)).getText().toString();
        String price = ((EditText)findViewById(R.id.sellerfoodprice)).getText().toString();
        String category = ((EditText)findViewById(R.id.sellerfoodcategory)).getText().toString();

        if(name.length() > 0 && explain.length() > 0 && price.length() > 0 && category.length() > 0){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            menuInfo menuInfo = new menuInfo(name, explain, price, category);
            if(user != null){
                db.collection("menu_List").document("menu").set(menuInfo);
            }
        }else{
            startToast("메뉴정보를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
