package com.example.kw_mk;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerMainActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentSellerStore fragmentSellerStore;
    private FragmentSellerOrder fragmentSellerOrder;
    private FragmentSellerReview fragmentSellerReview;
    private FragmentSellerMenu fragmentSellerMenu;
    private FragmentTransaction transaction;

    public static Activity AActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main);

        fragmentSellerStore = new FragmentSellerStore();
        fragmentSellerMenu = new FragmentSellerMenu();
        fragmentSellerOrder = new FragmentSellerOrder();
        fragmentSellerReview = new FragmentSellerReview();

        AActivity = SellerMainActivity.this;

        transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentSellerStore).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener { // BottomNavigationView 버튼 속성
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            transaction = fm.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.order_store:  // 홈
                    transaction.replace(R.id.frameLayout, fragmentSellerStore).commitAllowingStateLoss();
                    break;
                case R.id.order_menu:  // 주문내역
                    transaction.replace(R.id.frameLayout, fragmentSellerMenu).commitAllowingStateLoss();
                    break;
                case R.id.order_order:  // 내정보
                    transaction.replace(R.id.frameLayout, fragmentSellerOrder).commitAllowingStateLoss();
                    break;
                case R.id.order_review:  // 내정보
                    transaction.replace(R.id.frameLayout, fragmentSellerReview).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
