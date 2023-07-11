package com.example.legisunicor.db.models;

public class Caso {
    private int idCaso;
    private String nombreCaso;
    private String descripcionBreve;
    private String estado;
    private int idCliente;

    public Caso(int idCaso, String nombreCaso, String descripcionBreve, String estado, int idCliente) {
        this.idCaso = idCaso;
        this.nombreCaso = nombreCaso;
        this.descripcionBreve = descripcionBreve;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public int getIdCaso() {
        return idCaso;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public String getNombreCaso() {
        return nombreCaso;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public void setNombreCaso(String nombreCaso) {
        this.nombreCaso = nombreCaso;
    }
}

