package com.example.eventify.Objets;

public class Notificacion {
    private String fechaHora;
    private String mensaje;
    private String titulo;

    public Notificacion(String fechaHora, String mensaje, String titulo) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
        this.titulo = titulo;
    }
    public Notificacion(){}

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
