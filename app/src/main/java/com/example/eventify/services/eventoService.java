package com.example.eventify.services;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface eventoService {
    @GET("Eventos")
    Call<List<Categoria>> getCategoriasAll();

    @POST("Eventos")
    Call<Evento> saveEvento(@Body Evento evento);
}
