package com.example.kw_mk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConsumerMainActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentConsumerHome fragmentConsumerHome;
    private FragmnetConsumerOderlist fragmentConsumerOderlist;
    private FragmentConsumerMypage fragmentConsumerMypage;
    private FragmentTransaction trans;

    public static Activity AActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main);

        fragmentConsumerHome = new FragmentConsumerHome();
        fragmentConsumerOderlist = new FragmnetConsumerOderlist();
        fragmentConsumerMypage = new FragmentConsumerMypage();

        AActivity = ConsumerMainActivity.this;

        trans = fm.beginTransaction();
        trans.replace(R.id.frameLayout, fragmentConsumerHome).commitAllowingStateLoss();

        BottomNavigationView navView = findViewById(R.id.navigationView);
        navView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        getSupportActionBar().setTitle("홈");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ConsumerMainActivity.this, Select_Page.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener { // BottomNavigationView 버튼 속성
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            trans = fm.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.page_home:  // 홈
                    getSupportActionBar().setTitle("홈");
                    trans.replace(R.id.frameLayout, fragmentConsumerHome).commitAllowingStateLoss();
                    break;
                case R.id.page_tv:  // 주문내역
                    getSupportActionBar().setTitle("주문내역");
                    trans.replace(R.id.frameLayout, fragmentConsumerOderlist).commitAllowingStateLoss();
                    break;
                case R.id.page_calendar:  // 내정보
                    getSupportActionBar().setTitle("내 정보");
                    trans.replace(R.id.frameLayout, fragmentConsumerMypage).commitAllowingStateLoss();  // FragmentConsumerMyPage.class 수정필요
                    break;
            }
            return true;
        }
    }
}
