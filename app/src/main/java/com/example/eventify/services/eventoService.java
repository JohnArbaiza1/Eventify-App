package com.example.eventify.services;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface eventoService {
    @GET("Eventos")
    Call<List<Categoria>> getCategoriasAll();

    @GET("Eventos")
    Call<List<Evento>> getEventosAll();

    @POST("Eventos")
    Call<Evento> saveEvento(@Body Evento evento);

    @GET("EventosUser/{id}")
    Call<List<Evento>> getEventoByUserId(@Path("id") String userId);

    @PATCH("Eventos/{id}")
    Call<Evento> updateEvent(@Path("id") String eventoId, @Body Evento evento);

    @DELETE("Eventos/{id}")
    Call<Evento> deleteEvento(@Path("id") String eventoId);
}
