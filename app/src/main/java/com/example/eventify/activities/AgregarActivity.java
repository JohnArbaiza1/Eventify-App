package com.example.eventify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.eventify.R;

public class AgregarActivity extends AppCompatActivity {

    public ImageView img_eventos;
    public EditText txt_nombre_eventos, txt_descripcion_eventos, txt_ubicacion_eventos, txt_cupos_eventos;
    public Spinner spinner_categoria_eventos;
    public Button btn_guardar_eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        img_eventos = findViewById(R.id.img_eventos);
        txt_nombre_eventos = findViewById(R.id.nombre_eventos);
        txt_descripcion_eventos = findViewById(R.id.descripcion_eventos);
        txt_ubicacion_eventos = findViewById(R.id.ubicacion_eventos);
        txt_cupos_eventos = findViewById(R.id.cupos_eventos);
        spinner_categoria_eventos = findViewById(R.id.spinner_eventos);
        btn_guardar_eventos = findViewById(R.id.btn_guardar_eventos);

        // Agregar opciones al Spinner
        String[] opciones = {"Concierto", "Congreso", "Cumpleaños", "15 años", "Boda"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categoria_eventos.setAdapter(adapter);
    }
}