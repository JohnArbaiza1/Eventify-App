package com.example.eventify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private boolean isReady = false;
    public ImageView login;
    public TextView txtlogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_Theme_Eventify);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Comprueba si los datos iniciales están listos.
                if (isReady) {
                    // Los datos están listos; comienza a dibujar.
                    content.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                } else {
                    // Los datos no están listos; suspende el dibujo.
                    return false;
                }
            }
        });

        // Simulación de una tarea que hace que los datos estén listos después de un retraso.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isReady = true;
            }
        }, 1500); // 5 segundos de retraso

        login = findViewById(R.id.imgLogin);
        txtlogin = findViewById(R.id.txtLogin);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            UpdateUI();
        }
    }
    private void UpdateUI() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}