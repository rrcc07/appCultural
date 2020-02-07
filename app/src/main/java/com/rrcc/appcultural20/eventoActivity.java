package com.rrcc.appcultural20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class eventoActivity extends AppCompatActivity {
    Button btnMap,btnEliminarEvento;
    String t;
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    FirebaseDatabase firebaseDatabase;
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;

    String postKeyEvento;
    String tituloPost;
    String usuarioPropietario;
    String correoPropietario;
    String correoSuperUsuario="rrcc01@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference("Direcciones");

        btnMap = findViewById(R.id.btnMapa);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(eventoActivity.this,MapsActivity2.class);
                intent.putExtra("tEvento",t);
                startActivity(intent);
            }
        });
        btnEliminarEvento = findViewById(R.id.eliminarEvento);
        btnEliminarEvento.setVisibility(View.INVISIBLE);
        btnEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(eventoActivity.this,R.style.AlertDialogStyle);
                alertDialogBuilder
                        .setMessage("¿Eliminar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int id) {
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postsnap: dataSnapshot.getChildren()){
                                            Post post =  postsnap.getValue(Post.class);
                                            String idPost = post.getPostKey();
                                            tituloPost = post.getTitle();
                                            if(postKeyEvento.equals(idPost)){
                                                databaseReference.child(postKeyEvento).removeValue();
                                                showMessage("Evento eliminado con exito!");
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                                databaseReference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot direccion: dataSnapshot.getChildren()){
                                            Direccion dir = direccion.getValue(Direccion.class);
                                            String idDir = dir.getIdDireccion();
                                            String key = dir.getDirKey();
                                            if(tituloPost.equals(idDir)){
                                                //showMessage("---------"+tituloPost);
                                                //showMessage(idDir);
                                                //showMessage(key);
                                                databaseReference1.child(key).removeValue();
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                onBackPressed();
                                //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });
        getIncomingIntent();
    }
    private void getIncomingIntent() {
        if(getIntent().hasExtra("tituloPost") && getIntent().hasExtra("numeroPropietario") && getIntent().hasExtra("detallePost")
                && getIntent().hasExtra("horaIniPost") && getIntent().hasExtra("horaFinPost")
                && getIntent().hasExtra("fechaIniPost") && getIntent().hasExtra("fechaFinPost")
                && getIntent().hasExtra("direccionPost") && getIntent().hasExtra("imagenPost")
                && getIntent().hasExtra("categoria") && getIntent().hasExtra("usuarioPropietario")
                && getIntent().hasExtra("correoPropietario") && getIntent().hasExtra("postKey")) {

            String tituloPost = t = getIntent().getStringExtra("tituloPost");
            String numTelefono = getIntent().getStringExtra("numeroPropietario");
            String detallePost = getIntent().getStringExtra("detallePost");
            String horaPost = getIntent().getStringExtra("horaIniPost")+" hasta "+getIntent().getStringExtra("horaFinPost");
            String fechaPost = getIntent().getStringExtra("fechaIniPost")+" hasta el "+getIntent().getStringExtra("fechaFinPost");
            String direccionPost = getIntent().getStringExtra("direccionPost");
            String imagenPost = getIntent().getStringExtra("imagenPost");
            String categoria = getIntent().getStringExtra("categoria");
            usuarioPropietario = getIntent().getStringExtra("usuarioPropietario");
            correoPropietario = getIntent().getStringExtra("correoPropietario");
            String postKey = getIntent().getStringExtra("postKey");
            //t=getIntent().getStringExtra("tituloPost");
            setPost(tituloPost, numTelefono, detallePost, horaPost,fechaPost, direccionPost,
                    imagenPost,categoria,usuarioPropietario,correoPropietario);

            verificarPropiedadEvento(usuarioPropietario,correoPropietario,postKey);
        }
    }

    private void verificarPropiedadEvento(String usuarioPro, String correoPro, String postKey) {

        String userActual= currentUser.getDisplayName();
        String correoActual = currentUser.getEmail();
        postKeyEvento = postKey;
        if(userActual.equals(usuarioPro) && correoActual.equals(correoPro) || correoActual.equals(correoSuperUsuario)){
            btnEliminarEvento.setVisibility(View.VISIBLE);
        }
        //showMessage("actual: "+userActual+"\n"+"correoActual: "+correoActual);
        //showMessage("holas"+usuarioPro+"  "+correoPro);
    }
    private void setPost(String tituloPost, String telefonoPro, String detalle, String horaPost, String fechaPost,
                         String dirPost, String imagenPost, String categoria, String usuarioP, String correoP) {

        TextView mes = findViewById(R.id.editMes);
        TextView diaNum = findViewById(R.id.editNum);
        TextView titulo = findViewById(R.id.tituloEvento);
        TextView telefonoP = findViewById(R.id.telefonoPropietario);
        TextView horaIni = findViewById(R.id.textTime);
        TextView fechaIni = findViewById(R.id.textDate);
        TextView direccionPost = findViewById(R.id.textPlace);
        ImageView imagenP = findViewById(R.id.imagenEvento);
        TextView detallePost = findViewById(R.id.textDetalle);
        TextView cat = findViewById(R.id.categoria);
        TextView usuarioPo = findViewById(R.id.nameOrganizador);
        TextView correoPo = findViewById(R.id.correoOrgaizador);

        diaNum.setText(fechaPost.substring(0,6));
        titulo.setText(tituloPost);
        telefonoP.setText(telefonoPro);
        horaIni.setText(horaPost);
        fechaIni.setText(fechaPost);
        direccionPost.setText(dirPost);
        cat.setText("categoria: "+ categoria);
        usuarioPo.setText("Creado por: "+ usuarioP);
        correoPo.setText("Correo: "+ correoP);
        Glide.with(this).load(imagenPost).into(imagenP);

        int mesInt = Integer.parseInt(fechaPost.substring(7,9));
        mes.setText(getMes(12,mesInt,2019));
        detallePost.setText(detalle);
    }
    public void showMessage(String message) {
        Toast.makeText(eventoActivity.this,message,Toast.LENGTH_LONG).show();
    }
    String getMes (int dia, int mes, int ano)
    {
        String letraD="";
        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        calendar.set(ano, mes-1, dia);
        int nD=calendar.get(Calendar.MONTH);
        switch (nD){
            case 0: letraD = "Ene"; break;
            case 1: letraD = "Feb"; break;
            case 2: letraD = "Mar"; break;
            case 3: letraD = "Abr"; break;
            case 4: letraD = "May"; break;
            case 5: letraD = "Jun"; break;
            case 6: letraD = "Jul"; break;
            case 7: letraD = "Ago"; break;
            case 8: letraD = "Sep"; break;
            case 9: letraD = "Oct"; break;
            case 10: letraD = "Nov"; break;
            case 11: letraD = "Dic"; break;
        }
        return letraD;
    }
}
