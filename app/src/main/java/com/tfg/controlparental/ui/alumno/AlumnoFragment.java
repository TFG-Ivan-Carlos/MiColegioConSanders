package com.tfg.controlparental.ui.alumno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.controlparental.R;

import java.util.HashMap;
import java.util.Map;

public class AlumnoFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    View vista;
    Button consultar, save, delete;
    EditText col, clase, tutor, codigoAlumno;
    TextView texto, textClase, textColegio, textTut;
    private Map<String, Object> note;


    private AlumnoViewModel alumnoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alumnoViewModel =
                ViewModelProviders.of(this).get(AlumnoViewModel.class);
        vista = inflater.inflate(R.layout.fragment_alumno, container, false);
        final TextView textView = vista.findViewById(R.id.text_alumno);
        alumnoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Operaciones
        Bundle bundle = getActivity().getIntent().getExtras();
        textColegio = vista.findViewById(R.id.textViewColegio);
        col = vista.findViewById(R.id.Colegio);
        codigoAlumno = vista.findViewById(R.id.editTextTextPersonName);
        textClase = vista.findViewById(R.id.textViewClase);
        clase = vista.findViewById(R.id.Clase);
        textTut = vista.findViewById(R.id.textViewTutor);
        tutor = vista.findViewById(R.id.textTutor);
        save = vista.findViewById(R.id.change);
        delete = vista.findViewById(R.id.buttonRemove);
        consultar = vista.findViewById(R.id.btnConsultar);

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto = vista.findViewById(R.id.text_alumno);

                DocumentReference docRef = db.collection("users").document(codigoAlumno.getText().toString());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            String rol;
                            note = documentSnapshot.getData();
                            rol = note.get("rol").toString();
                            if (rol.equalsIgnoreCase("alumno")) {
                                col.setText(note.get("Colegio").toString());
                                clase.setText(note.get("Clase").toString());
                                tutor.setText(note.get("Tutor").toString());
                                if (col.getVisibility() != View.VISIBLE) {
                                    textColegio.setVisibility(View.VISIBLE);
                                    textClase.setVisibility(View.VISIBLE);
                                    textTut.setVisibility(View.VISIBLE);
                                    col.setVisibility(View.VISIBLE);
                                    clase.setVisibility(View.VISIBLE);
                                    tutor.setVisibility(View.VISIBLE);
                                    save.setVisibility(View.VISIBLE);
                                    delete.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            texto.setText("Alumno no encontrado");
                            textTut.setVisibility(View.GONE);
                            textColegio.setVisibility(View.GONE);
                            textClase.setVisibility(View.GONE);
                            col.setVisibility(View.GONE);
                            clase.setVisibility(View.GONE);
                            tutor.setVisibility(View.GONE);
                            save.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //agregar-editar alumno
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> docData = new HashMap<>();

                docData.put("Colegio", col.getText().toString());
                docData.put("Clase", clase.getText().toString());
                docData.put("Tutor", tutor.getText().toString());
                docData.put("rol", "alumno");
                db.collection("users").document(codigoAlumno.getText().toString()).set(docData);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(codigoAlumno.getText().toString()).delete();
            }
        });
        return vista;
    }
}