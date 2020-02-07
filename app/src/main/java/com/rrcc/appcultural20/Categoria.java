package com.rrcc.appcultural20;

public class Categoria {

    private String catName;
    private String imagenCategoria;

    public Categoria() {
    }
    public Categoria(String catName, String imagenCategoria)
    {
        this.catName = catName;
        this.imagenCategoria = imagenCategoria;
    }

    public String getImagenCategoria() {
        return imagenCategoria;
    }

    public void setImagenCategoria(String imagenCategoria) {
        this.imagenCategoria = imagenCategoria;
    }

    public String getCat() { return catName; }
    public void setCat(String catName) { this.catName = catName;    }

}
