package com.example.eventify.Objets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Evento {
    @SerializedName("idEvento")
    @Expose
    private Integer idEvento;
    @SerializedName("nombreEvento")
    @Expose
    private String nombreEvento;
    @SerializedName("asistentes")
    @Expose
    private Integer asistentes;
    @SerializedName("Descripción")
    @Expose
    private String descripciN;
    @SerializedName("ubicacion")
    @Expose
    private String ubicacion;
    @SerializedName("Fecha")
    @Expose
    private String fecha;
    @SerializedName("idUsuario")
    @Expose
    private String idUsuario;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("idCategoria")
    @Expose
    private Integer idCategoria;
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("fechaCreacion")
    @Expose
    private String fechaCreacion;

    /**
     * No args constructor for use in serialization
     *
     */
    public Evento() {
    }

    /**
     *
     * @param nombreEvento
     * @param fecha
     * @param ubicacion
     * @param img
     * @param descripciN
     * @param idEvento
     * @param idUsuario
     * @param categoria
     * @param fechaCreacion
     * @param idCategoria
     * @param asistentes
     * @param username
     */
    public Evento(Integer idEvento, String nombreEvento, Integer asistentes, String descripciN, String ubicacion, String fecha, String idUsuario, String username, Integer idCategoria, String categoria, String img, String fechaCreacion) {
        super();
        this.idEvento = idEvento;
        this.nombreEvento = nombreEvento;
        this.asistentes = asistentes;
        this.descripciN = descripciN;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.username = username;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.img = img;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public Integer getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(Integer asistentes) {
        this.asistentes = asistentes;
    }

    public String getDescripciN() {
        return descripciN;
    }

    public void setDescripciN(String descripciN) {
        this.descripciN = descripciN;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
