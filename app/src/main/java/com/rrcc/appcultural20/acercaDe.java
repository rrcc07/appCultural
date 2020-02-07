package com.rrcc.appcultural20;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class acercaDe extends android.support.v4.app.Fragment {

    //parametros fuente
    private TextView title;
    private Typeface BillSmith;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View fragmentView = inflater.inflate(R.layout.fragment_acerca_de, container, false);
       View contenedor=(View) container.getParent();

       return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //ocultar el floatingActionButton
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFloatingActionButton();
        }
    }
}
