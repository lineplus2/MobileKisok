package com.example.kw_mk;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConsumerMainActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentConsumerHome FragmentConsumerHome;
    private FragmnetConsumerOderlist FragmentConsumerOderlist;
    private FragmentTransaction trans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main);

        FragmentConsumerHome = new FragmentConsumerHome();
        FragmentConsumerOderlist = new FragmnetConsumerOderlist();

        trans = fm.beginTransaction();
        trans.replace(R.id.frameLayout, FragmentConsumerHome).commitAllowingStateLoss();

        BottomNavigationView navView = findViewById(R.id.navigationView);
        navView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener { // BottomNavigationView 버튼 속성
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            trans = fm.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.page_home:  // 홈
                    trans.replace(R.id.frameLayout, FragmentConsumerHome).commitAllowingStateLoss();
                    break;
                case R.id.page_tv:  // 주문내역
                    trans.replace(R.id.frameLayout, FragmentConsumerOderlist).commitAllowingStateLoss();
                    break;
                case R.id.page_calendar:  // 내정보
                    //
                    break;
            }
            return true;
        }
    }
}
