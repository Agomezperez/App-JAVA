package com.example.legisunicor.db.models;

public class Roles {
    private int idRol;
    private String rolname;

    public Roles(int idRol, String rolname) {
        this.idRol = idRol;
        this.rolname = rolname;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getRolname() {
        return rolname;
    }
}
