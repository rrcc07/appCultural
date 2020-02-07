package com.rrcc.appcultural20;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends Activity {

    //parametros fuente
    private TextView title;
    private Typeface BillSmith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //modificacion de FUENTE
        String fuente = "fuentes/BillSmith.ttf";
        this.BillSmith = Typeface.createFromAsset(getAssets(),fuente);
        title = (TextView) findViewById(R.id.titleSplash);
        title.setTypeface(BillSmith);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, IdentificarseActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
