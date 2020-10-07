package com.example.bizquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Space;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


    private TextView logoText, sloganText;
    private static  int SPLASH_SCREEN=3000;
    private Animation topAnim, bottomAnim;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);



        logoText=findViewById(R.id.logo_txt);
        sloganText = findViewById(R.id.slogan_txt);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim =AnimationUtils.loadAnimation(this,R.anim.bottom_anim);


        logoText.setAnimation(topAnim);
        sloganText.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
