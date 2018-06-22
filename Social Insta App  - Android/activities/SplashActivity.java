package com.whaddyalove.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.whaddyalove.R;
import com.whaddyalove.utils.SessionUtil;


public class SplashActivity extends AppCompatActivity {
    Animation zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        zoom = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        findViewById(R.id.splash_logo).startAnimation(zoom);
        findViewById(R.id.title).startAnimation(zoom);
        findViewById(R.id.sub_title).startAnimation(zoom);
        displayView();
    }

    public void displayView() {
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(1 * 3500);
                    SharedPreferences prefs = SessionUtil.getUserSessionPreferences(SplashActivity.this);
                    if (prefs.contains(SessionUtil.USER_ID)) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        Intent splashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(splashIntent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
