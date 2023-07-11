package com.example.legisunicor.db.models;

public class Reporte {
    int id;
    private String periodo;
    private String descripcion;
    private String enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre;

    private boolean isChecked;

    public Reporte(int id, String periodo, String descripcion, String enero, String febrero, String marzo, String abril, String mayo, String junio, String julio, String agosto, String septiembre, String octubre, String noviembre, String diciembre) {
        this.id = id;
        this.periodo = periodo;
        this.descripcion = descripcion;
        this.enero = enero;
        this.febrero = febrero;
        this.marzo = marzo;
        this.abril = abril;
        this.mayo = mayo;
        this.junio = junio;
        this.julio = julio;
        this.agosto = agosto;
        this.septiembre = septiembre;
        this.octubre = octubre;
        this.noviembre = noviembre;
        this.diciembre = diciembre;
    }

    public Reporte() {

    }

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setSelected(boolean selected) {
        this.isChecked = selected;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnero() {
        return enero;
    }

    public void setEnero(String enero) {
        this.enero = enero;
    }

    public String getFebrero() {
        return febrero;
    }

    public String getAbril() {
        return abril;
    }

    public String getMarzo() {
        return marzo;
    }

    public String getMayo() {
        return mayo;
    }

    public String getJunio() {
        return junio;
    }

    public String getJulio() {
        return julio;
    }

    public String getOctubre() {
        return octubre;
    }

    public String getAgosto() {
        return agosto;
    }

    public String getDiciembre() {
        return diciembre;
    }

    public String getNoviembre() {
        return noviembre;
    }

    public String getSeptiembre() {
        return septiembre;
    }

    public void setAbril(String abril) {
        this.abril = abril;
    }

    public void setAgosto(String agosto) {
        this.agosto = agosto;
    }

    public void setDiciembre(String diciembre) {
        this.diciembre = diciembre;
    }

    public void setFebrero(String febrero) {
        this.febrero = febrero;
    }

    public void setJulio(String julio) {
        this.julio = julio;
    }

    public void setMarzo(String marzo) {
        this.marzo = marzo;
    }

    public void setJunio(String junio) {
        this.junio = junio;
    }

    public void setMayo(String mayo) {
        this.mayo = mayo;
    }

    public void setNoviembre(String novienbre) {
        this.noviembre = novienbre;
    }

    public void setOctubre(String octuble) {
        this.octubre = octuble;
    }

    public void setSeptiembre(String septiembre) {
        this.septiembre = septiembre;
    }
}
