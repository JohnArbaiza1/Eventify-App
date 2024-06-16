package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eventify.Fragments.HomeFragment;
import com.example.eventify.Fragments.NotifiFragment;
import com.example.eventify.Fragments.PerfilFragment;
import com.example.eventify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    public ImageView agregar, buscar;
    public EditText buscador;
    public BottomNavigationView menuOpciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        agregar = findViewById(R.id.imgAdd);
        buscar = findViewById(R.id.imgSearch);
        buscador = findViewById(R.id.txtBuscar);
        menuOpciones = findViewById(R.id.menu);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Boton para agregar", Toast.LENGTH_SHORT).show();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Busqueda General", Toast.LENGTH_SHORT).show();
            }
        });
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Caracter", String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        menuOpciones.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
                        break;
                    case R.id.menuNotificaciones:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new NotifiFragment()).commit();
                        break;
                    case R.id.menuPerfil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new PerfilFragment()).commit();
                        break;
                }
                return true;
            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AgregarActivity.class);
                startActivity(intent);
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BuscarActivity.class);
                startActivity(intent);
            }
        });
    }
}