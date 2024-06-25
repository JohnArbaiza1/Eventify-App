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
import com.example.eventify.Fragments.EventsByUserFragment;
import com.example.eventify.Fragments.HomeFragment;
import com.example.eventify.Fragments.NotifiFragment;
import com.example.eventify.Fragments.PerfilFragment;
import com.example.eventify.Fragments.RegisteredFragment;
import com.example.eventify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    public ImageView agregar, buscar, agregarCategoria;
    public BottomNavigationView menuOpciones;
    private boolean isAdmin = false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        agregar = findViewById(R.id.imgAdd);
        buscar = findViewById(R.id.imgSearch);
        agregarCategoria = findViewById(R.id.imgAgregarCategoria);
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
        String fragmentToOpen = getIntent().getStringExtra("abrirInscripciones");
        if (fragmentToOpen != null) {
            if (fragmentToOpen.equals("Incripciones")) {
                // Cargar el fragmento de favoritos
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new RegisteredFragment())
                        .commit();
                menuOpciones.setSelectedItemId(R.id.menuPerfil);
            } else if (fragmentToOpen.equals("Eventos")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new EventsByUserFragment()).commit();
                menuOpciones.setSelectedItemId(R.id.menuPerfil);
            }else if(fragmentToOpen.equals("AbrirCategoria")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CategoriasFragment()).commit();
                menuOpciones.setSelectedItemId(R.id.menuCategoria);
            } else {
                // Cargar el fragmento por defecto (por ejemplo, HomeFragment)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new HomeFragment())
                        .commit();
            }
        }
        menuOpciones.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        agregarCategoria.setVisibility(View.INVISIBLE);
                        agregar.setVisibility(View.VISIBLE);
                        buscar.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
                        break;
                    case R.id.menuNotificaciones:
                        agregarCategoria.setVisibility(View.INVISIBLE);
                        agregar.setVisibility(View.INVISIBLE);
                        buscar.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new NotifiFragment()).commit();
                        break;
                    case R.id.menuPerfil:
                        agregarCategoria.setVisibility(View.INVISIBLE);
                        agregar.setVisibility(View.INVISIBLE);
                        buscar.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new PerfilFragment()).commit();
                        break;
                    case R.id.menuCategoria:
                        agregarCategoria.setVisibility(View.VISIBLE);
                        agregar.setVisibility(View.INVISIBLE);
                        buscar.setVisibility(View.INVISIBLE);
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
        agregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AgregarCategoria.class);
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