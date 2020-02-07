package com.rrcc.appcultural20;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView popupPostImage,popupAddBtn;
    CircleImageView popupUserImage;
    Button popupDireccion2;
    EditText popupTitle, popupTelefono,popupDescription,popupDireccion;
    TextView popupInicioFecha, popupFinFecha, popupInicioHora, popupFinHora;
    ProgressBar popupClickProgress;
    Spinner popupSpinner;
    ImageView btnImg, btnFechaInicio, btnFechaFin, btnHoraInicio, btnHoraFin, btnMapa, btnMapaYa;
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //evento
    Dialog popAddPost ;
    private Uri pickedImgUri = null;
    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    //variable local para el FloatinActionButton
    FloatingActionButton fab;
    //añadimos las fechas
    //aalisis fechas
    int in,fi,inMes,fiMes,inM,fiM,inHora,fiHora;
    boolean flaMapa;
    //popoup de fecha y hora
    private DatePickerDialog.OnDateSetListener mDateInicioSetListener;
    private DatePickerDialog.OnDateSetListener mDateFinSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeInicioSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeFinSetListener;

    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //pag de inicio
        setFragmentInicio(new InicioFragment());

        //iniciamos firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Direcciones");
        updateNavHeader();
        flaMapa = false;
        // ini agregar evento
        //iniPopup();
        //setupPopupImageClick();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flaMapa=false;
                iniPopup();
                setupPopupImageClick();
                popAddPost.show();
                showMessage("Porfavor, Llena de forma ordenada todos los campos");
            }
        });
    }

    //metodos para ocultar/mostrar el floatingActionButton
    public void showFloatingActionButton() { fab.show(); };
    public void hideFloatingActionButton() { fab.hide(); };
    //clase q controla el estado del usuario (identificado/desconocido)

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.evento_add);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        //CircleImageView = popAddPost.findViewById(R.id.image_usuario_evento)
        popupUserImage = popAddPost.findViewById(R.id.image_usuario_evento);
        popupPostImage = popAddPost.findViewById(R.id.imagen_evento);
        popupTitle = popAddPost.findViewById(R.id.titulo_evento);
        popupTelefono = popAddPost.findViewById(R.id.telefono);
        popupInicioFecha = popAddPost.findViewById(R.id.fecha_inicio_evento);
        popupFinFecha = popAddPost.findViewById(R.id.fecha_final_evento);
        popupInicioHora = popAddPost.findViewById(R.id.tiempo_inicio_evento);
        popupFinHora = popAddPost.findViewById(R.id.tiempo_final_evento);
        popupDescription = popAddPost.findViewById(R.id.detalle_evento);
        popupDireccion = popAddPost.findViewById(R.id.ubicacion_evento);
        popupDireccion2 = popAddPost.findViewById(R.id.btnMapa1);
        popupAddBtn = popAddPost.findViewById(R.id.btn_subir_evento);
        popupClickProgress = popAddPost.findViewById(R.id.progressBar_evento);

        btnImg = popAddPost.findViewById(R.id.btnImagenYa);
        btnFechaInicio = popAddPost.findViewById(R.id.btnFechaInicioYa);
        btnFechaFin = popAddPost.findViewById(R.id.btnFechaFinYa);
        btnHoraInicio = popAddPost.findViewById(R.id.btnHoraInicioYa);
        btnHoraFin = popAddPost.findViewById(R.id.btnHoraFinYa);
        btnMapa = popAddPost.findViewById(R.id.btnMapaYa);
        btnMapaYa = popAddPost.findViewById(R.id.btnDone);

        //agregamos el spinner
        popupSpinner= (Spinner)popAddPost.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.combo_dias,R.layout.new_spinner);
        popupSpinner.setAdapter(adapter);

        // añadir foto de usuario

        Glide.with(MainActivity.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        //añadir boton de subir evento

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image
                if (!popupTitle.getText().toString().isEmpty()
                        && !popupTelefono.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && !popupInicioFecha.getText().toString().isEmpty()
                        && !popupFinFecha.getText().toString().isEmpty()
                        && !popupInicioHora.getText().toString().isEmpty()
                        && !popupFinHora.getText().toString().isEmpty()
                        && !popupSpinner.getSelectedItem().toString().isEmpty()
                        && !popupDireccion.getText().toString().isEmpty()
                        && pickedImgUri != null) {
                    //verificar fechas
                    String i = (popupInicioFecha.getText().toString()).substring(4,6);
                    String f = (popupFinFecha.getText().toString()).substring(4,6);
                    String imes = (popupInicioFecha.getText().toString()).substring(7,9);
                    String fmes = (popupFinFecha.getText().toString()).substring(7,9);

                    in= Integer.valueOf(i);
                    fi= Integer.valueOf(f);
                    inMes= Integer.valueOf(imes);
                    fiMes= Integer.valueOf(fmes);
                    //showMessage(""+in+"---"+fi);
                    //showMessage(""+inMes+"---"+fiMes);
                    //verificar horas
                    String ihi = (popupInicioHora.getText().toString()).substring(0,2);
                    String ihf = (popupFinHora.getText().toString()).substring(0,2);
                    String imi = (popupInicioHora.getText().toString()).substring(3);
                    String imf = (popupFinHora.getText().toString()).substring(3);
                    //showMessage(""+ihi+"---"+ihf);
                    //showMessage(""+imi+"---"+imf);

                    inHora= Integer.valueOf(ihi);
                    fiHora= Integer.valueOf(ihf);
                    inM = Integer.valueOf(imi);
                    fiM = Integer.valueOf(imf);

                    boolean fla1=true;
                    boolean fla2=true;
                    boolean fla3=true;
                    boolean flaSpinner=true;
                    boolean flaImgEvento=true;

                    Calendar cal= Calendar.getInstance();
                    String dateAyer=twoDigits(cal.get(Calendar.DAY_OF_MONTH)-1) + "/" + twoDigits(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

                    if(in>fi || inMes>fiMes){
                        showMessage("La fecha de inicio debe ser menor o igual a la fecha final");
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popupClickProgress.setVisibility(View.INVISIBLE);
                    }
                    if(in==fi && inMes==fiMes){
                        if(inHora>fiHora){
                            fla1 = false;
                            showMessage("La hora del inicio del evento debe ser menor a la hora final");
                            popupAddBtn.setVisibility(View.VISIBLE);
                            popupClickProgress.setVisibility(View.INVISIBLE);}
                        if(inHora==fiHora && inM>=fiM){
                            fla2 = false;
                            showMessage("La hora del inicio del evento debe ser menor a la hora final");
                            popupAddBtn.setVisibility(View.VISIBLE);
                            popupClickProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                    if((popupInicioFecha.getText().toString().substring(4)).equals(dateAyer)) {
                        fla3 = false;
                        showMessage("La fecha de inicio no debe ser una fecha pasada");
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popupClickProgress.setVisibility(View.INVISIBLE);
                    }
                    if(!flaMapa){
                        showMessage("seleccione un ubicacion en el Mapa");
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popupClickProgress.setVisibility(View.INVISIBLE);
                    }
                    if(popupSpinner.getSelectedItem().toString().equals("*Categorias*")){
                        flaSpinner = false;
                        showMessage("seleccione una categoria");
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popupClickProgress.setVisibility(View.INVISIBLE);
                    }
                    if(pickedImgUri == null){
                        flaImgEvento = false;
                        showMessage("seleccione una Imagen para el evento");
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popupClickProgress.setVisibility(View.INVISIBLE);
                    }
                    if(in<=fi && inMes<=fiMes && fla1 && fla2 && fla3 && flaMapa && flaSpinner && flaImgEvento){
                        todoOk();
                    }
                }
                else {
                    showMessage("Por favor verifica llenar todas las opciones para el evento") ;
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
        popupDireccion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!popupTitle.getText().toString().isEmpty()){
                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    intent.putExtra("tituloPost",popupTitle.getText().toString());
                    startActivity(intent);
                }else{
                    showMessage("llena los campos anteriores antes de seleccionar la ubicacion");
                }
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot direccion: dataSnapshot.getChildren()){
                            Direccion dir = direccion.getValue(Direccion.class);
                            String idDir = dir.getIdDireccion();
                            String key = dir.getDirKey();
                            String tituloPost = popupTitle.getText().toString();
                            if(tituloPost.equals(idDir)){
                                btnMapa.setVisibility(View.INVISIBLE);
                                btnMapaYa.setVisibility(View.VISIBLE);
                                flaMapa = true;
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });
        //inicializamos las fechas
        //evento del añadir fecha inicio
        popupInicioFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateInicioSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateInicioSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String d = diaSemana(dayOfMonth,month,year);
                String date=d+"," +twoDigits(dayOfMonth) + "/" + twoDigits(month) + "/" + year;
                popupInicioFecha.setText(date);
                btnFechaInicio.setVisibility(View.INVISIBLE);
            }
        };
        //evento del añadir fecha Fin
        popupFinFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateFinSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateFinSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String d = diaSemana(dayOfMonth,month,year);
                String date=d+"," +twoDigits(dayOfMonth) + "/" + twoDigits(month) + "/" + year;
                popupFinFecha.setText(date);
                btnFechaFin.setVisibility(View.INVISIBLE);
            }
        };
        //inicio las horas
        //evento del añadir hora inicio
        popupInicioHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);

                TimePickerDialog dialog=new TimePickerDialog(
                        MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        mTimeInicioSetListener, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mTimeInicioSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                popupInicioHora.setText(twoDigitsHora(hourOfDay)+":"+twoDigitsHora(minute));
                btnHoraInicio.setVisibility(View.INVISIBLE);
            }
        };
        //evento del añadir hora fin
        popupFinHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);

                TimePickerDialog dialog=new TimePickerDialog(
                        MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        mTimeFinSetListener, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mTimeFinSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                popupFinHora.setText(twoDigitsHora(hourOfDay)+":"+twoDigitsHora(minute));
                btnHoraFin.setVisibility(View.INVISIBLE);
            }
        };
    }
    private void todoOk()
    {
        //everything is okey no empty or null value
        // TODO Create Post Object and add it to firebase database
        // first we need to upload post Image
        // access firebase storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
        final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownlaodLink = uri.toString();
                        // create post Object
                        Post post = new Post(popupTitle.getText().toString(),
                                popupTelefono.getText().toString(),
                                popupDescription.getText().toString(),
                                popupInicioFecha.getText().toString(),
                                popupFinFecha.getText().toString(),
                                popupInicioHora.getText().toString(),
                                popupFinHora.getText().toString(),
                                popupSpinner.getSelectedItem().toString(),
                                popupDireccion.getText().toString(),
                                imageDownlaodLink,
                                currentUser.getUid(),
                                currentUser.getDisplayName(),
                                currentUser.getEmail(),
                                currentUser.getPhotoUrl().toString(),
                                getHourPhone()
                        );
                        // Add post to firebase database
                        addPost(post);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // something goes wrong uploading picture
                        showMessage(e.getMessage());
                        popupClickProgress.setVisibility(View.INVISIBLE);
                        popupAddBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and upadte post key
        String key = myRef.getKey();
        post.setPostKey(key);

        // añade el evento al firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Añadido el evento correctamente!!");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
                pickedImgUri=null;
            }
        });
    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
                // we did this before in register activity I'm just going to copy the code to save time ...
                checkAndRequestForPermission();
            }
        });
    }
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(MainActivity.this,"Por favor acepta los permisos",Toast.LENGTH_SHORT).show();
            }

            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else
            // everything goes well : we have permission to access user gallery
            openGallery();
    }
    // when user picked an image ...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            popupPostImage.setImageURI(pickedImgUri);
            btnImg.setVisibility(View.INVISIBLE);
        }
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    public void showMessage(String message) {
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, IdentificarseActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            setFragmentInicio(new InicioFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_categorias) {
            setFragmentCategorias(new CategoriasFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Categorias", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_ultimos) {
            setFragmentUltimos(new UltimosFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Agregados recientemente", Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.nav_acercaDe) {
            setFragmentAcercaDe(new acercaDe());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Acerca De", Toast.LENGTH_SHORT).show();

        }
        else if(id == R.id.nav_salir){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, IdentificarseActivity.class));
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragmentAcercaDe(acercaDe acercaDe) {
        if(acercaDe!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, acercaDe);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void setFragmentInicio(InicioFragment inicioFragment) {

        if(inicioFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, inicioFragment);
            ft.commit();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
    private void setFragmentCategorias(CategoriasFragment categoriasFragment) {
        if(categoriasFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, categoriasFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void setFragmentUltimos(UltimosFragment ultimosFragment) {
        if(ultimosFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, ultimosFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    /*cargamos la foto y datos del usuario*/
    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.nav_name_user);
        TextView navUserMail = headerView.findViewById(R.id.nav_email_user);
        CircleImageView navUserPhot=headerView.findViewById(R.id.nav_imageView_user);

        navUsername.setText(currentUser.getDisplayName());
        navUserMail.setText(currentUser.getEmail());

        // Check if user's email is verified
        //boolean emailVerified = currentUser.isEmailVerified();
        // ahora usando Glide cargaremos la foto de usuario
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
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
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    private String twoDigitsHora(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}
