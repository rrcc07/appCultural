package com.rrcc.appcultural20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RegistrarseActivity<integer> extends AppCompatActivity {

    //declarar datos
    private TextInputLayout text_input_username, text_email, text_password, text_password2;
    //declarar botones
    private Button btnRegistrarse;
    TextView btnRegreseIdentificate;
    TextView btnSeleccionarFoto;

    //progressBar
    private ProgressBar loadingProgress;
    //declaro firebase autetificacion
    private FirebaseAuth mAuth;

    Uri pickedImgUri ;
    ImageView ImgUserPhoto;
    boolean flag = false;
    //CircleImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        //animaciones
        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(fadeIn);
        getWindow().setAllowEnterTransitionOverlap(false);
        setContentView(R.layout.activity_registrarse);

        //inicializar datos
        text_input_username=(TextInputLayout) findViewById(R.id.text_input_username);
        text_email= (TextInputLayout) findViewById(R.id.text_input_email);
        text_password= (TextInputLayout) findViewById(R.id.text_input_password);
        text_password2= (TextInputLayout) findViewById(R.id.text_input_Password2);
        loadingProgress = findViewById(R.id.regProgressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        btnRegistrarse=findViewById(R.id.btnRegistrarse);
        btnRegreseIdentificate=findViewById(R.id.btnRegresaIdentificate);

        btnSeleccionarFoto=findViewById(R.id.btn_selec_imagen);
        //innicializo firease autentificacion
        mAuth = FirebaseAuth.getInstance();

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegistrarse.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);

                final String email = text_email.getEditText().getText().toString();
                final String password = text_password.getEditText().getText().toString();
                final String password2 = text_password2.getEditText().getText().toString();
                final String name = text_input_username.getEditText().getText().toString();

                if( email.isEmpty() || name.isEmpty() || password.isEmpty()  || password.isEmpty() || password2.isEmpty() || flag == false) {
                    // a veces muestra error: todos los datos deben estar llenos
                    // necesitamos mostrar el mensaje de error
                    showMessage("Porfavor verifique que los campos esten llenados") ;
                    if(flag==false) { showMessage("seleccione una foto de Usuario");}
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else {
                    if(password.equals(password2)){
                        // todo esta bien y todos los datos estan llenos, ahora podremos crear el usuario
                        registrarUsuario(email, name, password, password2);
                    }
                    else{
                        showMessage("las contraseñas deben ser iguales");
                        btnRegistrarse.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                        return;
                    }
                }
            }
        });

        btnRegreseIdentificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarseActivity.this, IdentificarseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //iniciamos el dato de la foto

        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 21) {
                    checkAndRequestForPermission(); }
                else
                {  openGallery(); }
            }
        });
    }

    // simple metodo para mostrar un Toast general
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    private void registrarUsuario(String email, final String name, String password, String password2) {
        if (TextUtils.isEmpty(password2)) {Toast.makeText(this, "Falta ingresar la confirmacion de contraseña", Toast.LENGTH_LONG).show();
            return;}
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish(); // no volver a abrir el activity
                            // cuenta de usuario creada con exito
                            showMessage("Cuenta crada con exito!!!");
                            // despues de crear el usuario necistamos cargar la imagen y nombre
                            actualizacionInformacionUsuario( name ,pickedImgUri,mAuth.getCurrentUser());
                        }
                        else
                        {
                            // cuenta creada fallida
                            showMessage("creacion de cuenta fallida ");
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(RegistrarseActivity.this, "La dirección de correo electrónico está mal escrita", Toast.LENGTH_LONG).show();
                                    text_email.setError("The email address is badly formatted.");
                                    text_email.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(RegistrarseActivity.this, "La contraseña no es válida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                                    text_password.setError("password is incorrect ");
                                    text_password.requestFocus();
                                    text_password.setHint("");
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(RegistrarseActivity.this, "La dirección de correo electrónico ya está en uso por otra cuenta.   ", Toast.LENGTH_LONG).show();
                                    text_email.setError("The email address is already in use by another account.");
                                    text_email.requestFocus();
                                    break;
                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(RegistrarseActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(RegistrarseActivity.this, "No hay registro de usuario correspondiente a este identificador. El usuario puede haber sido eliminado.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(RegistrarseActivity.this, "La contraseña no es válida, debe tener al menos 6 caracteres.", Toast.LENGTH_LONG).show();
                                    text_password.setError("The password is invalid it must 6 characters at least");
                                    text_password.requestFocus();
                                    break;
                                }
                            btnRegistrarse.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    // cargar foto de usuario y nombre
    private void actualizacionInformacionUsuario(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // primero necesitamos cargar la foto a firebase storage y pedir la url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("fotos_usuario");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // imagen subida con exito
                // ahora pedimos la imagen
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contiene la imagen url
                        final UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("registro completo");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegistrarseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrarseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegistrarseActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(RegistrarseActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
            flag = true;
        }
    }

    private void updateUI() {
        Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
        //Intent intent= new Intent(RegistrarseActivity.this, MainActivity.class);
        //flag para evitar volver a lanzar la pila de activities previos
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
        //finish();
    }
    //fin de la animacion
    public void onBackClicked(View view) { finishAfterTransition(); }
}


