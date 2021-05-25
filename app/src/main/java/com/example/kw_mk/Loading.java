package com.example.kw_mk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class Loading extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_image);
        ImageView load = (ImageView) findViewById(R.id.loading);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(load);

        Glide.with(this).load(R.raw.load_gif).into(load);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(Loading.this, Select_Page.class);
                startActivity(intent);
                finish();
            }
    }, 1000);
    }


}
