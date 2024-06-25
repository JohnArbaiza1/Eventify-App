package com.example.eventify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventify.Adaptador.EventosAdapter;
import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.categoriaService;
import com.example.eventify.services.eventoService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarCategoria extends AppCompatActivity {
    public TextInputEditText nombreCategoria, descripcionCategoria;
    public Button btnActualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categoria);
        nombreCategoria = findViewById(R.id.nombreCategoriaEdit);
        descripcionCategoria = findViewById(R.id.descripcionCategoriaEdit);
        btnActualizar = findViewById(R.id.btnGuardarCategoriaEdit);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");
        String nombre = bundle.getString("nombre");
        String descripcion = bundle.getString("descripcion");

        nombreCategoria.setText(nombre);
        descripcionCategoria.setText(descripcion);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatos()){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    categoriaService categoriaservice = retrofit.create(categoriaService.class);
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(Integer.parseInt(id));
                    categoria.setCategoria(nombreCategoria.getText().toString());
                    categoria.setDescripción(descripcionCategoria.getText().toString());
                    Call<Categoria> call = categoriaservice.updateCategoria(id, categoria);
                    call.enqueue(new Callback<Categoria>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if(response.isSuccessful()){
                                actualizarEventos(nombreCategoria.getText().toString(), id);
                                Toast.makeText(EditarCategoria.this, "Categoria Actualizada", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditarCategoria.this, HomeActivity.class);
                                intent.putExtra("abrirInscripciones", "AbrirCategoria");
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {
                            Toast.makeText(EditarCategoria.this, "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void actualizarEventos(String string, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        eventoService eventoService = retrofit.create(eventoService.class);

        // Realizar la llamada asíncrona
        Call<List<Evento>> call = eventoService.getEventosAll();
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful()) {
                    List<Evento> listEventos = response.body();
                    for (Evento item: listEventos) {
                        if(item.getIdCategoria() == Integer.parseInt(id)){
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            eventoService eventoservice = retrofit.create(eventoService.class);
                            item.setCategoria(string);
                            Call<Evento> actualizarEvento = eventoservice.updateEvent(String.valueOf(item.getIdEvento()), item);
                            actualizarEvento.enqueue(new Callback<Evento>() {
                                @Override
                                public void onResponse(Call<Evento> call, Response<Evento> response) {
                                    if(response.isSuccessful()){
                                        Log.d("Evento->", "Evento Actualizado");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Evento> call, Throwable t) {
                                    Toast.makeText(EditarCategoria.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                } else {
                    Toast.makeText(EditarCategoria.this, "Error al obtener los eventos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(EditarCategoria.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarDatos(){
        boolean estado = false;
        String nombreValidar = nombreCategoria.getText().toString().trim();
        String descripcionValidar = descripcionCategoria.getText().toString().trim();
        if(nombreValidar.isEmpty()){
            estado = false;
            nombreCategoria.setError("Este campo es requerido");
        }
        if(descripcionValidar.isEmpty()){
            estado = false;
            descripcionCategoria.setError("Este campo es requerido");
        }
        else{
            nombreCategoria.setError(null);
            descripcionCategoria.setError(null);
            estado = true;
        }
        return estado;
    }
}