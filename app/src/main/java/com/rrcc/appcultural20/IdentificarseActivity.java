package com.rrcc.appcultural20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class IdentificarseActivity extends AppCompatActivity {

    //declaramnos las variables
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    //declaramos los botones
    private Button btnIdentificarse;
    private TextView btnCuenta;

    private ProgressBar loginProgress;

    //declarar Firebase atenticacion
    private FirebaseAuth mAuth;

    //parametros fuente
    private TextView title;
    private Typeface BillSmith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificarse);

        //modificacion de FUENTE
        String fuente = "fuentes/BillSmith.ttf";
        this.BillSmith = Typeface.createFromAsset(getAssets(),fuente);
        title = (TextView) findViewById(R.id.titleIdentificarse);
        title.setTypeface(BillSmith);

        textInputEmail = findViewById(R.id.textEmail);
        textInputPassword = findViewById(R.id.textPassword);
        btnIdentificarse = findViewById(R.id.btnIdentificarse);
        btnCuenta = findViewById(R.id.btnCrearCuenta);
        loginProgress = findViewById(R.id.ideProgressBar);

         btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSlideClicked(v);
            }
        });

        //inicializo firebase autentificacion
        mAuth = FirebaseAuth.getInstance();

        loginProgress.setVisibility(View.INVISIBLE);
        btnIdentificarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnIdentificarse.setVisibility(View.INVISIBLE);

                final String mail = textInputEmail.getEditText().getText().toString();
                final String password = textInputPassword.getEditText().getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Porfavor, llene todos los campos");
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnIdentificarse.setVisibility(View.VISIBLE);
                    return;
                }
                else
                {
                    identificarse(mail,password);
                }
            }
        });
    }
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
    //clase q controla el estado del usuario (identificado/desconocido)
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            //usuario esta conectado redireccionamos al activity del inicio
            updateUI();
        }
    }

    private void updateUI() {
        Intent intent= new Intent(IdentificarseActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "se encuentra identificado", Toast.LENGTH_LONG).show();
        finish();
    }
    public String getEmail(){
        return textInputEmail.getEditText().getText().toString();
    }
    private void identificarse(String mail, String password) {
        //Identificamos al usuario con firebase autenticacion
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnIdentificarse.setVisibility(View.VISIBLE);
                    updateUI();
                    Toast.makeText(getApplicationContext(),"Usuario autentificado con exito",Toast.LENGTH_SHORT).show();
                }
                else {
                    //showMessage(task.getException().getMessage());
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(IdentificarseActivity.this, "La dirección de correo electrónico está mal escrita", Toast.LENGTH_LONG).show();
                            textInputEmail.setError("The email address is badly formatted.");
                            textInputEmail.requestFocus();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(IdentificarseActivity.this, "La contraseña no es válida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                            textInputPassword.setError("password is incorrect ");
                            textInputPassword.requestFocus();
                            textInputPassword.setHint("");
                            break;
                        /*
                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(IdentificarseActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                            textInputEmail.setError("The email address is already in use by another account.");
                            textInputEmail.requestFocus();
                            break;
                        case "ERROR_USER_TOKEN_EXPIRED":
                            Toast.makeText(IdentificarseActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;
                         */
                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(IdentificarseActivity.this, "No hay registro de usuario correspondiente a este identificador. El usuario puede haber sido eliminado.", Toast.LENGTH_LONG).show();
                            break;
                    }
                    btnIdentificarse.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //animaciones
    public Transition transition;
    public void onSlideClicked(View view){
        transition = new Slide(Gravity.START);
        crearCuenta();
        //finish();
    }
    @SuppressWarnings("unchecked")
    private void crearCuenta() {
        transition.setDuration(1000);
        transition.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(transition);
        Intent intent = new Intent(this, RegistrarseActivity.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        //finish();
    }
}