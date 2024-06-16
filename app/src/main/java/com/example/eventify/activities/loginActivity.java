package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    public TextView txt_sign_up;
    public EditText email_input_signin;
    public EditText password_input_signin;
    public Button btn_sign_in;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_sign_up = findViewById(R.id.txt_sign_up);
        email_input_signin = findViewById(R.id.email_input_signin);
        password_input_signin = findViewById(R.id.password_input_signin);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        mAuth = FirebaseAuth.getInstance();

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sign_in.setVisibility(View.INVISIBLE);
                final String correo = email_input_signin.getText().toString();
                final String contraseña = password_input_signin.getText().toString();
                if(correo.isEmpty() || contraseña.isEmpty()){
                    Toast.makeText(loginActivity.this, "Por favor verifique y llene todos los campos", Toast.LENGTH_SHORT).show();
                    //progreso.setVisibility(View.INVISIBLE);
                    btn_sign_in.setVisibility(View.VISIBLE);
                }
                else {
                    acceder(correo, contraseña);
                }
            }
        });
    }

    private void acceder(String correo, String contraseña) {
        mAuth.signInWithEmailAndPassword(correo,contraseña).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //progreso.setVisibility(View.INVISIBLE);
                        btn_sign_in.setVisibility(View.VISIBLE);
                        UpdateUI();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginActivity.this, "Correo y contraseña Invalidos", Toast.LENGTH_SHORT).show();
                        //progreso.setVisibility(View.INVISIBLE);
                        btn_sign_in.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void UpdateUI() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            UpdateUI();
        }
    }
}