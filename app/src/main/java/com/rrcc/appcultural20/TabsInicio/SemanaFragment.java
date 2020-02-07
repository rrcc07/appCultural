package com.rrcc.appcultural20.TabsInicio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrcc.appcultural20.MainActivity;
import com.rrcc.appcultural20.Post;
import com.rrcc.appcultural20.R;
import com.rrcc.appcultural20.post_adapter;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class SemanaFragment extends android.support.v4.app.Fragment {

    RecyclerView postRecyclerView ;
    post_adapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_semana,container, false);
        postRecyclerView = fragmentView.findViewById(R.id.postRVSemana);
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
                String dia = diaSemana(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));

                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    String df = post.getFechaFin().substring(4,6);
                    String dD = date.substring(0,2);
                    int dF= Integer.valueOf(df);
                    int dDI= Integer.valueOf(dD);
                    if(dDI<=dF)
                    { postList.add(post); }
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
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    String diaSemana (int dia, int mes, int ano)
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
