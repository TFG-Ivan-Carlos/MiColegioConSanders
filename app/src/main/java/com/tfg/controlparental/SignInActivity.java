package com.tfg.controlparental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText clase, colegio, pass, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //setUp
        setUp();
    }

    private void setUp() {
        setTitle("Registration");

        email = findViewById(R.id.editMail);
        clase = findViewById(R.id.textClass);
        colegio = findViewById(R.id.textColegio);
        pass = findViewById(R.id.editPass);
        Button sign_in = findViewById(R.id.btnRegistro);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty() && !clase.getText().toString().isEmpty() && !colegio.getText().toString().isEmpty()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                }else{
                    showAlert();
                }
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Error al autenticar usuario");
        alert.setPositiveButton("Aceptar", null);
        AlertDialog pop = alert.create();
        alert.show();
    }

    private void showHome(String user) {
        Map<String, Object> data = new HashMap<>();

        data.put("mail", user);
        data.put("rol", "profesor");
        data.put("Clase", clase.getText().toString());
        data.put("Colegio", colegio.getText().toString());
        data.put("searches", new ArrayList<String>());

        db.collection("users").document(user).set(data);
        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
        home.putExtra("email", user);
        startActivity(home);

    }

}