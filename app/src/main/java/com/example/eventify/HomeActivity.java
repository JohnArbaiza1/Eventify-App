package com.example.eventify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.eventify.Fragment.add_event;
import com.example.eventify.Fragment.events;
import com.example.eventify.Fragment.profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    public BottomNavigationView menu_opciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        menu_opciones = findViewById(R.id.menu_opciones);

        // Cargar el tipo de letra personalizado desde assets
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Aclonica.ttf");

        // Iterar sobre los elementos del menú y aplicar el tipo de letra
        Menu menu = menu_opciones.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            applyFontToMenuItem(menuItem, typeface);
        }

        menu_opciones.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_event:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new add_event()).commit();
                        break;
                    case R.id.display_event:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new events()).commit();
                        break;
                    case R.id.perfil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new profile()).commit();
                        break;
                }
                return true;
            }
        });
    }

    private void applyFontToMenuItem(MenuItem menuItem, Typeface typeface) {
        SpannableString title = new SpannableString(menuItem.getTitle());
        title.setSpan(new CustomTypefaceSpan("", typeface), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(title);
    }
}