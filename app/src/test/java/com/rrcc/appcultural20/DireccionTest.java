package com.rrcc.appcultural20;

import org.junit.Test;

import static org.junit.Assert.*;

public class DireccionTest {
    Direccion dir;
    Post post;
    String esperado;
    String actual;
    @Test
    public void testDireccionEvento(){
        dir = new Direccion("-17.374790313903212","-66.15314919501543", "Conferencias  filosofia y literatura");
        post = new Post("Conferencias  filosofia y literatura", "76905468", "description", "Sab,28/11/2019", "Mar,30/11/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        esperado = post.getTitle();
        actual = dir.getIdDireccion();
        System.out.println("titulo Direccion :"+ actual);
        System.out.println("titulo evento :"+ esperado);
        assertEquals(esperado,actual);
    }
    @Test
    public void testFailDireccionEvento(){
        dir = new Direccion("-17.374790313903212","-66.15314919501543", "Calabozos y Cementerios");
        post = new Post("Conferencias  filosofia y literatura", "76905468", "description", "Sab,28/11/2019", "Mar,30/11/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        esperado = post.getTitle();
        actual = dir.getIdDireccion();
        System.out.println("titulo Direccion :"+ actual);
        System.out.println("titulo evento :"+ esperado);
        assertNotEquals(esperado,actual);
    }
    @Test
    public void testExistDireccion(){
        dir = new Direccion("-17.374790313903212","-66.15314919501543", "Calabozos y Cementerios");
        System.out.println("titulo Direccion :"+ dir.getIdDireccion());
        assertNotNull(dir.getIdDireccion());
    }
}