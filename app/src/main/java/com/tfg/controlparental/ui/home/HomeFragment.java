package com.tfg.controlparental.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.controlparental.R;
import com.tfg.controlparental.WebBrowser;
import com.tfg.controlparental.WebList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View vista;
    Button logout, browser;
    private String email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = vista.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Bundle bundle = getActivity().getIntent().getExtras();
        String email =  bundle.getString("email");
        this.email = email;
        String rol = bundle.getString("rol");
        //Cerrar sesion

        logout = vista.findViewById(R.id.logOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.getData().get("rol").toString().equalsIgnoreCase("alumno")) {
                                registrarHoras(documentSnapshot);
                            }
                            FirebaseAuth.getInstance().signOut();
                            getActivity().onBackPressed();
                        }
                    });
                }
        });
        
        //Ir al navegador
        browser = vista.findViewById(R.id.browser);
        
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(getActivity().getApplication(), WebList.class);
                browser.putExtra("email", email);
                startActivity(browser);
            }
        });

        return vista;
    }

    private void registrarHoras(DocumentSnapshot documentSnapshot){
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,Object> mapAlum = documentSnapshot.getData();
        ArrayList<String> registro = (ArrayList<String>) mapAlum.get("registro_uso");
        mapAlum.put("Hora_out", currentTime);
        Timestamp in = (Timestamp) mapAlum.get("Hora_in");
        String dia =  elegirDia(in.toDate().getDay());
        int horas = Math.abs(currentTime.getHours() - in.toDate().getHours());
        int min = Math.abs(currentTime.getMinutes() - in.toDate().getMinutes());
        int secs = Math.abs(currentTime.getSeconds() - in.toDate().getSeconds());

        registro.set(in.toDate().getDay(), dia + " Horas: " + horas + " Min: " + min + " Secs: " + secs);
        mapAlum.put("registro_uso", registro );
        db.collection("users").document(email).update(mapAlum);
    }


    private String elegirDia(int day) {
        String dia;
        switch(day){
            case 1: dia = "Lunes";
                break;
            case 2 :dia = "Martes";
                break;
            case 3: dia = "Miercoles";
                break;
            case 4: dia = "Jueves";
                break;
            case 5: dia = "Viernes";
                break;
            case 6: dia = "Sabado";
                break;
            case 0: dia = "Domingo";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + day);
        }
        return dia;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}