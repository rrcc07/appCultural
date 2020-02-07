package com.rrcc.appcultural20;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker markerDrag;
    private static final int LOCATION_REQUEST_CODE = 1;
    private Button mTypeBtnNor;
    private Button mTypeBtnHib;
    private Button seleccionado;
    private double lat;
    private double lng;
    String e, dir,dirKey;
    LocationManager locationManager;

    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //botones de configuracion de mapas
        mTypeBtnNor = (Button) findViewById(R.id.btnNormal);
        mTypeBtnHib = (Button) findViewById(R.id.btnHibrido);
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
        getIncomingIntent();
        seleccionado=findViewById(R.id.btnSeleccionar);
        seleccionado.setVisibility(View.INVISIBLE);
        seleccionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseReference myRef =  mDatabase.child("Direcciones").push();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Direcciones").push();
                // get post unique ID and upadte post key
                String Dirkey = myRef.getKey();

                Direccion dir = new Direccion(Double.toString(lat),Double.toString(lng),e);
                dir.setDirKey(Dirkey);
                //mDatabase.child("Direcciones").push().setValue(dir);
                myRef.setValue(dir).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //popAddPost.dismiss();
                    }
                });
                onBackPressed();
                Toast.makeText(getApplicationContext(),"Añadido",Toast.LENGTH_SHORT).show();
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /****Mejora****/
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(MapsActivity.this, "Active su GPS, porfavor",Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }
    private void getIncomingIntent() {
        if(getIntent().hasExtra("tituloPost")) {
            e = getIntent().getStringExtra("tituloPost");
            //Toast.makeText(this,e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Toast.makeText(this,"Pulsa el icono sin soltar, arrastra y selecciona el Lugar del evento",Toast.LENGTH_LONG).show();

        LatLng p = new LatLng(-17.3935853, -66.1569588);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 13));

        LatLng plaza = new LatLng(-17.3935853, -66.1569588);
        markerDrag =googleMap.addMarker(new MarkerOptions()
                .position(p)
                .title("Arrastra este marcador sin soltar y selecciona un lugar")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(plaza));
        googleMap.setOnMarkerDragListener(this);
        // Controles UI
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        seleccionado.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMarkerDrag(Marker marker) {    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(marker.equals(markerDrag)){
            String newTitle = String.format(Locale.getDefault(),
                    getString(R.string.marker_detail_latlng),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);
            lat=marker.getPosition().latitude;
            lng=marker.getPosition().longitude;
            //Toast.makeText(this,newTitle,Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,"lat="+ lat + "long"+lng,Toast.LENGTH_SHORT).show();
        }
        seleccionado.setVisibility(View.VISIBLE);
        setLocation(lat,lng);
    }

    public void setLocation(double lat, double log) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (lat != 0.0 && log != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        lat, log, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    dir = ""+ DirCalle.getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this,"Agregar esta direccion:"+dir,Toast.LENGTH_LONG).show();

    }
}
