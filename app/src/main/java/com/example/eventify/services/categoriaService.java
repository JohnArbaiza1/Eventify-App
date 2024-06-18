package com.example.eventify.services;

import com.example.eventify.Objets.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface categoriaService {
    @GET("categorias")
    Call<List<Categoria>> getCategoriasAll();
}
