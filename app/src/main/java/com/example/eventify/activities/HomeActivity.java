package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eventify.CustomTypefaceSpan;
import com.example.eventify.Fragments.CategoriasFragment;
import com.example.eventify.Fragments.HomeFragment;
import com.example.eventify.Fragments.NotifiFragment;
import com.example.eventify.Fragments.PerfilFragment;
import com.example.eventify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    public ImageView agregar, buscar;
    public BottomNavigationView menuOpciones;
    private boolean isAdmin = false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        agregar = findViewById(R.id.imgAdd);
        buscar = findViewById(R.id.imgSearch);
        menuOpciones = findViewById(R.id.menu);
        mAuth = FirebaseAuth.getInstance();
        isAdmin = checkIfUserIsAdmin();

        if(!isAdmin){
            Menu menu = menuOpciones.getMenu();
            menu.findItem(R.id.menuCategoria).setVisible(false);
        }

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

        // Cargar el tipo de letra personalizado desde assets
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Aclonica.ttf");

        // Iterar sobre los elementos del menú y aplicar el tipo de letra
        Menu menu = menuOpciones.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            applyFontToMenuItem(menuItem, typeface);
        }

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
                    case R.id.menuCategoria:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CategoriasFragment()).commit();
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
    private void applyFontToMenuItem(MenuItem menuItem, Typeface typeface) {
        SpannableString title = new SpannableString(menuItem.getTitle());
        title.setSpan(new CustomTypefaceSpan("", typeface), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(title);
    }

    private boolean checkIfUserIsAdmin(){
        FirebaseUser user = mAuth.getCurrentUser();
        boolean admin = false;
        if(user.getEmail().equals("admin@gmail.com")){
            admin = true;
        }
        return admin;
    }

}