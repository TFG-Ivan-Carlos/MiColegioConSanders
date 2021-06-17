package com.tfg.controlparental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class AuthActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("message", "Integracion de Firebase completa");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //setUp
        setUp();
    }

    private void setUp() {
        setTitle("Authentication");
        Button sign_in = findViewById(R.id.SignUpButton);
        Button log_in = findViewById(R.id.LogInButton);
        sign_in.setOnClickListener(this::signUp);

        log_in.setOnClickListener(this::LogIn);
    }

    public void signUp(View view) {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }


    public void LogIn(View view) {
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText pass = findViewById(R.id.editTextTextPassword);

        if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty() ){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        // Sign in success, update UI with the signed-in user's information
                        showHome(email.getText().toString());

                    }else{
                        // If sign in fails, display a message to the user.
                        showAlert();
                    }
                }

            });
        }
    }


    //funcion que muestra un pop up al dar error e inicio de sesion
    private void showAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Error al autenticar usuario");
        alert.setPositiveButton("Aceptar", null);
        AlertDialog pop = alert.create();
        alert.show();
    }

    //funcion que pasa a la pantalla Home
    private void showHome(String user){

        Intent home = new Intent(this, HomeActivity.class);
        home.putExtra("email", user);
        startActivity(home);

    }
}