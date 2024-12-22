package com.example.carpoolingapk1692022;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import android.widget.ImageView;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private ImageView logoImage;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Анимирана позадина
        ConstraintLayout constraintLayout = findViewById(R.id.mainContainer);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);

        // Анимација на лого
        logoImage = findViewById(R.id.logoImage);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        logoImage.startAnimation(pulseAnimation);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        DatabaseHelper.setCurrentUserId(-1);
    }
}