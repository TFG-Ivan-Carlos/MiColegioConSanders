package com.tfg.controlparental.ui.opciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.controlparental.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpcionesFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OpcionesViewModel opcionesViewModel;
    EditText horas;
    Button actualiza;
    private String email, horasString;
    private Map<String, Object> note;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        opcionesViewModel =
                ViewModelProviders.of(this).get(OpcionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_opciones, container, false);

        opcionesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        actualiza = root.findViewById(R.id.btnActualizar);
        horas = root.findViewById(R.id.editTextHoras);
        Bundle bundle = getActivity().getIntent().getExtras();
        String email =  bundle.getString("email");
        this.email = email;
        actualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getData().get("rol").toString().equalsIgnoreCase("profesor")) {
                            horasString = horas.getText().toString();
                            note = documentSnapshot.getData();
                        /*TODO GUARDA HORAS*/
                            actualizaHoras(horasString);
                        }
                    }
                });
            }
        });




        return root;
    }

    private void actualizaHoras(String horas) {

        note.put("limiteHoras", horas);
        db.collection("users").document(email).update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showAlert("Horas de uso actualizadas");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showAlert("Error al actualizar horas");
            }
        });

    }

    private void showAlert(String text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setMessage(text);
        alert.setPositiveButton("Aceptar", null);
        AlertDialog pop = alert.create();
        alert.show();
    }
}