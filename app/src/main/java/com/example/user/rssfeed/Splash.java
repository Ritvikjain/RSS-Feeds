package com.example.user.rssfeed;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = findViewById(R.id.imageView);
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv.setVisibility(View.VISIBLE);
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this, Categories.class));
                    }
                }, 2000);
            }
        }, 1000);
    }
}
