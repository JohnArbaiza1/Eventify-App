package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity {

    public TextView txt_sign_in;
    public EditText user_input_signup;
    public EditText email_input_signup;
    public EditText password_input_signup;
    public EditText confirm_password_input_signup;
    public Button btn_sign_up;
    //variable para la autenticación de Firebase
    private FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(registerActivity.this, "Registrando", Toast.LENGTH_SHORT).show();
                final String nombre = user_input_signup.getText().toString();
                final String email = email_input_signup.getText().toString();
                final String password = password_input_signup.getText().toString();
                final String password2 = confirm_password_input_signup.getText().toString();
                if(nombre.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty() ){
                    Toast.makeText(registerActivity.this, "Por favor verifique todos los Datos", Toast.LENGTH_SHORT).show();
                    //progeso.setVisibility(View.INVISIBLE);
                    btn_sign_up.setVisibility(View.VISIBLE);
                } else if (!password.equals(password2)) {
                    Toast.makeText(registerActivity.this, "Los campos de contraseña deben coincidir", Toast.LENGTH_SHORT).show();
                    //progeso.setVisibility(View.INVISIBLE);
                    btn_sign_up.setVisibility(View.VISIBLE);
                }
                else{
                    boolean cont = isValidPassword(password);
                    boolean correo = isValidEmail(email);
                    if(!cont){
                        Toast.makeText(registerActivity.this, "La Contraseña debe Tener minimo 8 Caracteres", Toast.LENGTH_SHORT).show();
                        Toast.makeText(registerActivity.this, "Debe incluir Caracteres especiales", Toast.LENGTH_SHORT).show();
                    }
                    if (!correo) {
                        Toast.makeText(registerActivity.this, "El correo no tiene formato Valido", Toast.LENGTH_SHORT).show();
                        Toast.makeText(registerActivity.this, "Formato Valido: user@gmail.com", Toast.LENGTH_SHORT).show();

                    }
                    if(cont && correo){
                        //progeso.setVisibility(View.VISIBLE);
                        btn_sign_up.setVisibility(View.INVISIBLE);
                        createUserAccount(nombre, email, password);
                    }
                }
            }
        });
    }
    //para validar contrasenia
    private boolean isValidPassword(String password) {
        // Password should have at least 8 characters and contain special characters
        if (password.length() >= 8) {
            // Check for special characters
            String specialCharacters = "!@#$%^&*()-_=+|<>?{}[]~";
            for (char c : password.toCharArray()) {
                if (specialCharacters.contains(String.valueOf(c))) {
                    return true;
                }
            }
        }
        return false;
    }
    // para validar Email
    private boolean isValidEmail(String email) {
        // Regular expression to validate email ending with @gmail.com
        String emailPattern = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailPattern);
        return email != null && pattern.matcher(email).matches();
    }
    private void createUserAccount(String nombre, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Toast.makeText(activity_register.this, "Todo Salio Bien", Toast.LENGTH_SHORT).show();
                        updateUserInfo(nombre, mAuth.getCurrentUser());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registerActivity.this, "Salio algo mal " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        //progeso.setVisibility(View.INVISIBLE);
                        btn_sign_up.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void updateUserInfo(String nombre, FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombre)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registerActivity.this, "Registro Completado", Toast.LENGTH_SHORT).show();
                            UpdateUI();
                        } else {
                            Toast.makeText(registerActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseUpdateError", "Error updating profile", task.getException());
                        }
                    }
                });
    }

    private void UpdateUI() {
        Intent inten = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(inten);
        finish();
    }
}