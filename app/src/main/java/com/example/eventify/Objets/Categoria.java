package com.example.eventify.Objets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categoria {
    @SerializedName("idCategoria")
    @Expose
    private Integer idCategoria;
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("Descripción")
    @Expose
    private String descripción;

    /**
     * No args constructor for use in serialization
     *
     */
    public Categoria() {
    }

    /**
     *
     * @param descripción
     * @param categoria
     * @param idCategoria
     */
    public Categoria(Integer idCategoria, String categoria, String descripción) {
        super();
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.descripción = descripción;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripciN() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }
}
