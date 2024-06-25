package com.example.eventify.services;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;

import org.checkerframework.checker.index.qual.PolySameLen;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface categoriaService {
    @GET("categorias")
    Call<List<Categoria>> getCategoriasAll();

    @POST("categorias")
    Call<Categoria> createCategoria(@Body Categoria categoria);

    @DELETE("categorias/{id}")
    Call<Categoria> deleteCategoria(@Path("id") String idCategoria);

    @PATCH("categorias/{id}")
    Call<Categoria> updateCategoria(@Path("id") String idCategoria, @Body Categoria categoria);
}
