package com.example.eventify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.categoriaService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgregarCategoria extends AppCompatActivity {
    public TextInputEditText nombreCategoria, descripcionCategoria;
    public Button guardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);
        nombreCategoria = findViewById(R.id.nombreCategoria);
        descripcionCategoria = findViewById(R.id.descripcionCategoria);
        guardar = findViewById(R.id.btnGuardarCategoria);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatos()){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    categoriaService categoriaservice = retrofit.create(categoriaService.class);
                    Categoria categoria = new Categoria();
                    categoria.setCategoria(nombreCategoria.getText().toString());
                    categoria.setDescripción(descripcionCategoria.getText().toString());
                    Call<Categoria> call = categoriaservice.createCategoria(categoria);
                    call.enqueue(new Callback<Categoria>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(AgregarCategoria.this, "Categoria Creada", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarCategoria.this, HomeActivity.class);
                                intent.putExtra("abrirInscripciones", "AbrirCategoria");
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {

                        }
                    });
                }
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