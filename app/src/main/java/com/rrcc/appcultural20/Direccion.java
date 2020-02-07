package com.rrcc.appcultural20;

public class Direccion {

    String lat;
    String lng;
    String idDireccion;
    String dirKey;

    public Direccion() {
    }

    public Direccion(String lat, String lng, String idDireccion) {
        this.lat = lat;
        this.lng = lng;
        this.idDireccion = idDireccion;
    }

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getIdDireccion() {
        return idDireccion;
    }
    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getDirKey() { return dirKey; }
    public void setDirKey(String dirKey) { this.dirKey = dirKey; }
}
