package com.example.eventify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class loginActivity extends AppCompatActivity {

    public TextView txt_sign_up;
    public EditText email_input_signin;
    public EditText password_input_signin;
    public Button btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_sign_up = findViewById(R.id.txt_sign_up);
        email_input_signin = findViewById(R.id.email_input_signin);
        password_input_signin = findViewById(R.id.password_input_signin);
        btn_sign_in = findViewById(R.id.btn_sign_in);

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });
    }
}