package com.rrcc.appcultural20.TabsInicio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrcc.appcultural20.MainActivity;
import com.rrcc.appcultural20.Post;
import com.rrcc.appcultural20.R;
import com.rrcc.appcultural20.post_adapter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FinDeSemanaFragment extends android.support.v4.app.Fragment {

    RecyclerView postRecyclerView ;
    post_adapter postAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_fin_de_semana,container, false);
        postRecyclerView = fragmentView.findViewById(R.id.postRVfinDeSemana);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        return fragmentView ;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Get List Posts from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //obtener fechas
                Calendar cal= Calendar.getInstance();
                String date=twoDigits(cal.get(Calendar.DAY_OF_MONTH)) + "/" + twoDigits(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    //dias del evento y fecha actual
                    String di = post.getFechaIni().substring(4,6);
                    String df = post.getFechaFin().substring(4,6);
                    String dia = post.getFechaIni().substring(0,3);
                    String dD = date.substring(0,2);

                    int dI= Integer.valueOf(di);
                    int dF= Integer.valueOf(df);
                    int dDI= Integer.valueOf(dD);

                    String diaI = post.getFechaIni();
                    String diaF = post.getFechaFin();
                    boolean esEntreSemana = calcularRango(dI,dF,dia,dDI);

                    if((diaI.subSequence(0,4).equals("Sab,") || diaI.subSequence(0,4).equals("Dom,")
                        || diaF.subSequence(0,4).equals("Sab,") || diaF.subSequence(0,4).equals("Dom,")
                            || esEntreSemana) && !(dF<dDI))
                    {postList.add(post); }
                }
                postAdapter = new post_adapter(getActivity(),postList);
                postRecyclerView.setAdapter(postAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFloatingActionButton();
        }
    }

    private boolean calcularRango(int diaInicio, int diaFin, String diaI, int hoy) {
        boolean res=false;
        int diferencia = diaFin-diaInicio;
        if(diaI.equals("Vie")){
            if(diferencia>=3){
                res=true; }
        }
        if(diaI.equals("Jue")){
            if(diferencia>=4){
                res=true;
            }
        }
        if(diaI.equals("Mie")){
            if(diferencia>=5){
                res=true;
            }
        }
        if(diaI.equals("Mar")){
            if(diferencia>=6){
                res=true;
            }
        }
        if(diaI.equals("Lun")){
            if(diferencia>=7){
                res=true;
            }
        }
        return res;
    }

    public void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}
