package com.rrcc.appcultural20;

public class Post {

    private String postKey;
    private String title;
    private String numTelefonoEvento;
    private String description;
    private String fechaIni;
    private String fechaFin;
    private String horaIni;
    private String horaFin;
    private String spinnerOpc;
    private String direccion;
    private String picture;
    private String userId;
    private String userName;
    private String correo;
    private String userPhoto;
    private String timephone ;

    public Post() {
    }

    public Post(String title, String numTelefonoEvento, String description, String fechaIni, String fechaFin,
                String horaIni, String horaFin, String spinnerOpc, String direccion,
                String picture, String userId, String userName, String correo, String userPhoto, String timephone) {
        this.title = title;
        this.numTelefonoEvento = numTelefonoEvento;
        this.description = description;
        this.direccion = direccion;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.spinnerOpc = spinnerOpc;
        this.picture = picture;
        this.userId = userId;
        this.userName = userName;
        this.correo = correo;
        this.userPhoto = userPhoto;
        this.timephone = timephone;
    }

    public String getPostKey() {
        return postKey;
    }
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumTelefonoEvento() { return numTelefonoEvento; }
    public void setNumTelefonoEvento(String numTelefonoEvento) { this.numTelefonoEvento = numTelefonoEvento; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getTimePhone() {
        return timephone;
    }
    public void setTimePhone(String timephone) {
        this.timephone = timephone;
    }

    public String getDireccion() {  return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getFechaIni() {   return fechaIni; }
    public void setFechaIni(String fechaIni) { this.fechaIni = fechaIni; }

    public String getFechaFin() {  return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getHoraIni() { return horaIni; }
    public void setHoraIni(String horaIni) { this.horaIni = horaIni; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName;    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getSpinnerOpc() { return spinnerOpc; }
    public void setSpinnerOpc(String spinnerOpc) {  this.spinnerOpc = spinnerOpc; }
}
