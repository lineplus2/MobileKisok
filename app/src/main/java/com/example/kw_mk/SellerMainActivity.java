package com.example.kw_mk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerMainActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentSellerHome fragmentSellerStore;
    private FragmentSellerOrder fragmentSellerOrder;
    private FragmentSellerReview fragmentSellerReview;
    private FragmentSellerMenu fragmentSellerMenu;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main);

        fragmentSellerStore = new FragmentSellerHome();
        fragmentSellerMenu = new FragmentSellerMenu();
        fragmentSellerOrder = new FragmentSellerOrder();
        fragmentSellerReview = new FragmentSellerReview();

        transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentSellerStore).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        getSupportActionBar().setTitle("홈");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SellerMainActivity.this, Select_Page.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener { // BottomNavigationView 버튼 속성
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            transaction = fm.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.order_store:  // 홈
                    getSupportActionBar().setTitle("홈");
                    transaction.replace(R.id.frameLayout, fragmentSellerStore).commitAllowingStateLoss();
                    break;
                case R.id.order_menu:  // 메뉴관리
                    getSupportActionBar().setTitle("메뉴관리");
                    transaction.replace(R.id.frameLayout, fragmentSellerMenu).commitAllowingStateLoss();
                    break;
                case R.id.order_order:  // 주문관리
                    getSupportActionBar().setTitle("주문관리");
                    transaction.replace(R.id.frameLayout, fragmentSellerOrder).commitAllowingStateLoss();
                    break;
                case R.id.order_review:  // 리뷰관리
                    getSupportActionBar().setTitle("리뷰관리");
                    transaction.replace(R.id.frameLayout, fragmentSellerReview).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
