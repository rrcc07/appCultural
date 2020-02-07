package com.rrcc.appcultural20;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;

    private Button mTypeBtnNor;
    private Button mTypeBtnHib;
    private static final int LOCATION_REQUEST_CODE = 1;
    String e;
    String detalles;
    String tipo;
    private ArrayList<Marker> tmpReal = new ArrayList<>();
    private ArrayList<Marker> realReal = new ArrayList<>();
    LocationManager locationManager;
    AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        //botones de configuracion de mapas
        mTypeBtnNor = (Button) findViewById(R.id.btnNormal1);
        mTypeBtnHib = (Button) findViewById(R.id.btnHibrido2);
        mTypeBtnNor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        mTypeBtnHib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
        //inicializamos los datos de firebase y de los datos pasados del anterior activity
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getIncomingIntent();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /****Mejora****/
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(MapsActivity2.this, "Active su GPS, porfavor",Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        /********/
    }
    //los datos del anterior activity
    private void getIncomingIntent() {
        if (getIntent().hasExtra("tEvento")) {
            e = getIntent().getStringExtra("tEvento");
            //Toast.makeText(MapsActivity2.this, "",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Marcadores
        LatLng p = new LatLng(-17.3935853, -66.1569588);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 13));

        //variables de acceso a firebase
        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    //Toast.makeText(MapsActivity2.this, "  1 ",Toast.LENGTH_SHORT).show();
                    if(post.getTitle().equals(e)){
                        //mostramos la categoroia
                        detalles = post.getDescription();
                        tipo = post.getSpinnerOpc();
                        //Toast.makeText(MapsActivity2.this, "   ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("Direcciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(Marker marker : realReal)
                { marker.remove(); }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Direccion d= snapshot.getValue(Direccion.class);
                    //obtengo latitud y longitud del evento
                    double latitud = Double.parseDouble(d.getLat());
                    double longitud = Double.parseDouble(d.getLng());
                    MarkerOptions markerOptions = new MarkerOptions();
                    if(d.getIdDireccion().equals(e)){
                        //lo añado al mapa
                        if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            //Toast.makeText(MapsActivity2.this, "Selecciona el evento para mas opciones",Toast.LENGTH_LONG).show();
                            }
                        String nD=tipo;
                        switch (nD){
                            case "Artesania": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.artezania)); break;
                            case "Cine": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.cine)); break;
                            case "Comida": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.comida)); break;
                            case "Danza": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.danza)); break;
                            case "Deportes": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.deportes)); break;
                            case "Ferias": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ferias)); break;
                            case "Fiesta": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.fiesta)); break;
                            case "Literatura": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.literatura)); break;
                            case "Musica": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.musica)); break;
                            case "Pintura": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pintura)); break;
                            case "Teatro": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.teatro)); break;
                            case "Tecnologia": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.tecnologia)); break;
                            case "Viajes": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.viajes)); break;
                            case "Educacion": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.educacion)); break;
                            case "Juegos": markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles).icon(BitmapDescriptorFactory.fromResource(R.mipmap.juegos)); break;
                        }
                        //markerOptions.position(new LatLng(latitud,longitud)).title(e).snippet(detalles);
                        tmpReal.add(mMap.addMarker(markerOptions));
                    }
                }
                //añadimos a la lista de markers
                realReal.clear();
                realReal.addAll(tmpReal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
