package com.tfg.controlparental.ui.cursos;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tfg.controlparental.R;

import java.util.List;
import java.util.Map;

public class CursosFragment extends Fragment {

    private CursosViewModel cursosViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String codigoAlumnoString;
    View vista;
    Button consultar, save, delete;
    EditText col, clase, tutor, codigoClase;
    TextView texto;
    private Map<String, Object> note, noteWeb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        String user;
        cursosViewModel =
                ViewModelProviders.of(this).get(CursosViewModel.class);
        vista = inflater.inflate(R.layout.fragment_cursos, container, false);
        final TextView textView = vista.findViewById(R.id.text_Opciones);
        cursosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        consultar = vista.findViewById(R.id.btnActualizar);
        codigoClase = vista.findViewById(R.id.editTextHoras);


        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto = vista.findViewById(R.id.text_alumno);
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                    int i = 1;
                                    for (DocumentSnapshot docu : myListOfDocuments) {
                                        if (docu.get("Clase").toString().equals(codigoClase.getText().toString())) {
                                            if (docu.get("rol").toString().equals("alumno")) {
                                                String user = "user";
                                                StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
                                                int idUser = getResources().getIdentifier(user + i, "id", getContext().getPackageName());
                                                TextView userTextView = (TextView) vista.findViewById(idUser);
                                                SpannableString negrita = new SpannableString("ID USUARIO: " + docu.getId().toString());
                                                negrita.setSpan(negrita, 0, negrita.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                String stringToClean = docu.get("registro_uso").toString();
                                                stringToClean = stringToClean.replace(",", "\n");
                                                stringToClean = stringToClean.replace("[", "");
                                                stringToClean = stringToClean.replace("]", "");
                                                userTextView.setText(negrita + "\n" + stringToClean);

                                                String[] array = stringToClean.split("Horas: ");
                                                array[0] = array[7];
                                                userTextView.setBackgroundColor(Color.parseColor("#7DFF33")); //verde
                                                for (int j = 0; j < array.length; j++) {
                                                    if (array[j].startsWith("0") || array[j].startsWith("1") || array[j].startsWith("2") || array[j].startsWith("3") || array[j].startsWith("4")) {

                                                    } else {
                                                        userTextView.setBackgroundColor(Color.parseColor("#C70039")); //rojo
                                                        break;
                                                    }
                                                }
                                                userTextView.setVisibility(View.VISIBLE);
                                                i++;
                                            }
                                        }
                                    }
                                }
                            }
                        });
            }
        });


        return vista;
    }
}