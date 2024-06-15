package com.example.eventify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean isReady = false;
    public ImageView login;
    public TextView txtlogin;
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
}