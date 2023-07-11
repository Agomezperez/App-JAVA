package com.example.legisunicor.db.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private int idRol;
    private int logeado;

    public User(int id, String username, String password, String nombre, String apellidos, int idRol, int logeado) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idRol = idRol;
        this.logeado = logeado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getLogeado() {
        return logeado;
    }

    public void setLogeado(int logeado) {
        this.logeado = logeado;
    }
}
