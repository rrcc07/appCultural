package com.rrcc.appcultural20;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class adapterCategorias extends AppCompatActivity {

    RecyclerView postRecyclerView ;
    post_adapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> postList;
    String e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_categorias);

        postRecyclerView  = findViewById(R.id.categoriasAdapterRV);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("catName")) {
            e = getIntent().getStringExtra("catName");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get List Posts from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                //obtener fechas
                Calendar cal= Calendar.getInstance();
                String date=twoDigits(cal.get(Calendar.DAY_OF_MONTH)) + "/" + twoDigits(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                String dia = diaSemana(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    //dias del evento y fecha actual
                    String df = post.getFechaFin().substring(4,6);
                    String dD = date.substring(0,2);

                    int dF= Integer.valueOf(df);
                    int dDI= Integer.valueOf(dD);

                    if(post.getSpinnerOpc().equals(getTipo(e)) && !(dF<dDI)){
                        postList.add(post) ;
                    }
                }
                postAdapter = new post_adapter(getApplicationContext(),postList);
                postRecyclerView.setAdapter(postAdapter);

                if(postList.isEmpty()) {
                    showMessage("No Existe ningun Evento de tipo: " + getTipo(e));
                    onBackPressed();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    String getTipo (String e)
    {
        String letraD="";
        switch (e){
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fartesania.jpg?alt=media&token=6242767c-bf8a-4475-9889-04bdeb1a4e1c": letraD = "Artesania"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fcine.jpg?alt=media&token=4ad9a48c-df66-47b8-bfc2-20daf1d27ca2": letraD = "Cine"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fcomida.jpg?alt=media&token=343dcd58-a293-435d-9940-38b6a7e73a19": letraD = "Comida"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fdanza.jpeg?alt=media&token=e4e0ffdb-0a94-4e3e-90da-9796c2894d9c": letraD = "Danza"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fdeportes.png?alt=media&token=a2c8374d-8bfa-4678-9f57-4b13341924a9": letraD = "Deportes"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fferias.jpg?alt=media&token=e81e2b7e-d76b-4add-b5f1-e75eb8bd34ea": letraD = "Ferias"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Ffiesta.jpg?alt=media&token=7f78d0fc-4999-4deb-b250-54d7c3f12de0": letraD = "Fiestas"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fliteratura.jpg?alt=media&token=2a6fb081-f6eb-4cde-87aa-8abd6ca6c83d": letraD = "Literatura"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2FimgCatMusica.png?alt=media&token=b6784a66-8dc6-4b91-a2e1-1a1580e1a1bf": letraD = "Musica"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fpintura.jpg?alt=media&token=d6fa3bf7-d8e8-4aa4-8e51-ebdd7b231180": letraD = "Pintura"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fteatro.jpg?alt=media&token=40b91a7c-cba1-4f11-acb7-9439265d3434": letraD = "Teatro"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Ftecnologia.png?alt=media&token=68d192e3-036d-4ee0-947d-33bef036e23b": letraD = "Tecnologia"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fviajes.jpg?alt=media&token=0ebfce59-96af-49f0-8051-b6e0e2e3de3e":letraD = "Viajes"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Feducacion.jpg?alt=media&token=38802c6d-fcd9-4193-af37-763a8ab86b45":letraD = "Educacion"; break;
            case "https://firebasestorage.googleapis.com/v0/b/appcultural20-d5b49.appspot.com/o/Categorias%2Fjuegos.jpg?alt=media&token=01d96a93-dafb-43d9-9f03-fc4f51d831c4":letraD = "Juegos"; break;
        }
        return letraD;
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    private String diaSemana (int dia, int mes, int ano)
    {
        String letraD="";
        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        calendar.set(ano, mes-1, dia);
        int nD=calendar.get(Calendar.DAY_OF_WEEK);
        Log.i("result","diaSemana: "+nD+" dia:"+dia+" mes:"+mes+ "año:" +ano);
        switch (nD){
            case 1: letraD = "Dom"; break;
            case 2: letraD = "Lun"; break;
            case 3: letraD = "Mar"; break;
            case 4: letraD = "Mie"; break;
            case 5: letraD = "Jue"; break;
            case 6: letraD = "Vie"; break;
            case 7: letraD = "Sab"; break;
        }
        return letraD;
    }
}
