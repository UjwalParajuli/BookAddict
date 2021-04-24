package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookaddict.utils.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView image_view_splash_screen;
    private TextView text_view_app_name, text_view_slogan;
    private static int SPLASH_TIME_OUT = 7000;
    Handler handler = new Handler();
    private Animation topAnim, bottomAnim, fade_in, fade_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_splash_screen);

        image_view_splash_screen = findViewById(R.id.image_view_splash_screen);
        text_view_app_name = findViewById(R.id.text_view_app_name);
        text_view_slogan = findViewById(R.id.text_view_slogan);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        image_view_splash_screen.setAnimation(fade_in);
        text_view_slogan.setAnimation(fade_in);
        text_view_app_name.setAnimation(bottomAnim);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);


    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}