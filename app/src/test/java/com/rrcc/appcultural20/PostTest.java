package com.rrcc.appcultural20;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class PostTest {
    Post post;
    String actual;
    String esperado;
    @Test
    public void testUsuarioAdministrador(){
        post = new Post("title", "76905468", "description", "Sab,30/11/2019", "Mar,03/12/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc01@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        actual = post.getCorreo();
        esperado = "rrcc01@gmail.com";
        System.out.println(actual.toString()+" es Administrador");
        assertEquals(esperado,actual);
    }
    @Test
    public void testFailUsuarioAdministrador(){
        post = new Post("title", "76905468", "description", "Sab,30/11/2019", "Mar,03/12/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        actual = post.getCorreo();
        esperado = "rrcc01@gmail.com";
        System.out.println(actual.toString()+" no es Administrador");
        assertFalse(esperado.equals(actual));
    }
    @Test
    public void testFechas(){
        post = new Post("title", "76905468", "description", "Sab,28/11/2019", "Mar,30/11/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        int fechaInicio= Integer.valueOf(post.getFechaIni().substring(4,6));
        int fechaFinal= Integer.valueOf(post.getFechaFin().substring(4,6));

        System.out.print(post.getFechaIni().toString());
        System.out.println(" < "+post.getFechaFin().toString());
        assertTrue(fechaInicio<=fechaFinal);
    }
    @Test
    public void testHoras(){
        post = new Post("title", "76905468", "description", "Sab,28/11/2019", "Mar,30/11/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        int horaInicio= Integer.valueOf(post.getHoraIni().substring(0,2));
        int horaFinal= Integer.valueOf(post.getHoraFin().substring(0,2));

        System.out.print(post.getHoraIni().toString());
        System.out.println(" < "+post.getHoraFin().toString());
        assertTrue(horaInicio<horaFinal);
    }
    @Test
    public void filtrarFechaHoy(){
        post = new Post("title", "76905468", "description", "Lun,02/12/2019", "Jue,05/12/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        Calendar cal= Calendar.getInstance();
        String date=twoDigits(cal.get(Calendar.DAY_OF_MONTH)) + "/" + twoDigits(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

        int fechaInicio= Integer.valueOf(post.getFechaIni().substring(4,6));
        int fechaFinal= Integer.valueOf(post.getFechaFin().substring(4,6));
        int hoy= Integer.valueOf(date.substring(0,2));

        System.out.print(post.getFechaIni().toString()+" <= " );
        System.out.print("fecha de hoy:"+date.toString());
        System.out.println(" < "+post.getFechaFin().toString());

        assertTrue(fechaInicio<=hoy && hoy<fechaFinal);
    }

    @Test
    public void filtrarFinDeSemana(){
        post = new Post("title", "76905468", "description", "Lun,02/12/2019", "Sab,07/12/2019",
                "19:00", "21:00", "Literatura", "Av. America #37", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/blog_images",
                "idUser", "rrcc01", "rrcc02@gmail.com", "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/fotos_usuario%2Fimage%3A23374?alt=media&token=4ae0bdc1-80e6-4a59-8c99-643f60954b81",
                "19:01:43");
        Calendar cal= Calendar.getInstance();
        String date=twoDigits(cal.get(Calendar.DAY_OF_MONTH)) + "/" + twoDigits(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

        String fechaInicio= post.getFechaIni().substring(0,3);
        String fechaFinal= post.getFechaFin().substring(0,3);

        assertTrue(fechaInicio!="Sab" || fechaFinal!="Dom");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}