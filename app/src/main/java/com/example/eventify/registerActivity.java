package com.example.eventify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class registerActivity extends AppCompatActivity {

    public TextView txt_sign_in;
    public EditText user_input_signup;
    public EditText email_input_signup;
    public EditText password_input_signup;
    public EditText confirm_password_input_signup;
    public Button btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_sign_in = findViewById(R.id.txt_sign_in);
        user_input_signup = findViewById(R.id.user_input_signup);
        email_input_signup = findViewById(R.id.email_input_signup);
        password_input_signup = findViewById(R.id.password_input_signup);
        confirm_password_input_signup = findViewById(R.id.confirm_password_input_signup);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
    }
}