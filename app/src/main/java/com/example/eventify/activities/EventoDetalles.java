package com.example.eventify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventify.R;

public class EventoDetalles extends AppCompatActivity {

    public ImageView imgEvento;
    public Button btnInscribirseEvento;
    public TextView txtNombreEvento, txtFechaDeEvento, txtFechaDeCreacion, txtUbicacionEvento,
            txtAsistenteEvento, txtCategoriaEvento, txtNombrePersona, txtDescripcionEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalles);

        imgEvento = findViewById(R.id.post_detail_image);

        btnInscribirseEvento = findViewById(R.id.btnInscribirseEvento);

        txtNombreEvento = findViewById(R.id.nombreEventoDetalle);
        txtFechaDeEvento = findViewById(R.id.fechaDelEvento);
        txtFechaDeCreacion = findViewById(R.id.fechaPublicacionEvento);
        txtUbicacionEvento = findViewById(R.id.ubicacionEventosDetalle);
        txtAsistenteEvento = findViewById(R.id.asistenteEventosDetalle);
        txtCategoriaEvento = findViewById(R.id.categoriaEventoDetalle);
        txtNombrePersona = findViewById(R.id.nombrePersonaDetalle);
        txtDescripcionEvento = findViewById(R.id.descripcionEventosDetalle);

        String img = getIntent().getExtras().getString("imagenEvento");
        Glide.with(this).load(img).into(imgEvento);

        String nombre = getIntent().getExtras().getString("nombreEvento");
        txtNombreEvento.setText(nombre);

        String fechaCreacion = getIntent().getExtras().getString("fechaCreacion");
        txtFechaDeCreacion.setText(fechaCreacion);

        String fechaEvento = getIntent().getExtras().getString("fechaDelEvento");
        txtFechaDeEvento.setText(fechaEvento);

        String ubicacion = getIntent().getExtras().getString("ubicacionEvento");
        txtUbicacionEvento.setText("Ubicacion: " + ubicacion);

        String asistente = getIntent().getExtras().getString("asistenteEvento");
        txtAsistenteEvento.setText("Cupos: " + asistente);

        String categoria = getIntent().getExtras().getString("categoriaEvento");
        txtCategoriaEvento.setText("Categoria: " + categoria);

        String descripcion = getIntent().getExtras().getString("descripcionEvento");
        txtDescripcionEvento.setText("Descripcion: " + descripcion);

        String nombrePersona = getIntent().getExtras().getString("nombrePersona");
        txtNombrePersona.setText("Publicado por: " + nombrePersona);
    }
}