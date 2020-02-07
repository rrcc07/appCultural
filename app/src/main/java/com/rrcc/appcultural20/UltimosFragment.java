package com.rrcc.appcultural20;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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


public class UltimosFragment extends android.support.v4.app.Fragment {

    RecyclerView ultimosRecyclerView;
    post_adapter favoritosAdapter;
    List<Post> postList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferencePost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_ultimos, container,false);
        ultimosRecyclerView = fragmentView.findViewById(R.id.ultimosRV);
        ultimosRecyclerView.setHasFixedSize(true);
        ultimosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferencePost = firebaseDatabase.getReference("Posts");

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get List Favoritos from the database
        databaseReferencePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //obtener fechas
                Calendar cal= Calendar.getInstance();
                String date=cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                String dia = diaSemana(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));

                String dateAnt=(cal.get(Calendar.DAY_OF_MONTH)-1) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                String diaAnt = diaSemana(cal.get(Calendar.DAY_OF_MONTH)-1,cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));

                String timeAnt= getHourPhone();
                int numEntero = Integer.parseInt(timeAnt.substring(0,2));

                postList = new ArrayList<>();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    String time = post.getTimePhone();
                    int timeEntero = Integer.parseInt(time.substring(0,2));
                    if(timeEntero > (numEntero-5))
                    {
                        postList.add(post);
                    }
                }
                favoritosAdapter = new post_adapter(getActivity(), postList);
                ultimosRecyclerView.setAdapter(favoritosAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //ocultar el floatingActionButton
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFloatingActionButton();
        }
    }
    public void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
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
    public static String getHourPhone() {
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteHour = df.format(dt.getTime());
        return formatteHour;
    }
}
