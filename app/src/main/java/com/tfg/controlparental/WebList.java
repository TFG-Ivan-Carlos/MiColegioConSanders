package com.tfg.controlparental;

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
import java.util.Map;

public class WebList extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView mListView;
    private EditText mEditText;
    private Button btnAdd, btnSolicitar;
    private String user, rol;
    private String tutor;
    private ArrayList<String> webs;
    private Map<String, Object> note, noteWeb;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.sitios_web);

        btnAdd = findViewById(R.id.btnAgregar);
        btnSolicitar = findViewById(R.id.btnSolicitar);
        mListView = findViewById(R.id.listView);
        mEditText = findViewById(R.id.editLista);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("email");
        DocumentReference docRef = db.collection("users").document(user);

        //recogemos los datos de firestore
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                note = documentSnapshot.getData();
                rol = note.get("rol").toString();
                //Si es un alumno
                if (rol.equalsIgnoreCase("alumno")) {
                    tutor = note.get("Tutor").toString();
                    btnSolicitar.setVisibility(View.VISIBLE);
                    //recogemos la lista de webs
                    getWebList(tutor);
                } else {//si es profesor
                    btnAdd.setVisibility(View.VISIBLE);
                    getWebList(user);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString().trim();
                String[] webList = text.split("\\s*,\\s*");
                boolean changes = false;
                for (String web : webList) {
                    if (!webs.contains(web)) {
                        if (URLUtil.isValidUrl(web)) {
                            changes = true;
                            webs.add(web);
                        } else {
                            showAlert("Esta web no es valida");
                        }
                    } else {
                        showAlert("Ya existe esta web");
                    }
                }
                //si hay cambios, actualizamos Firestore
                if (changes) {
                    noteWeb.put("searches", webs);
                    docRef.update(noteWeb).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showAlert("Exito al agregar web");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showAlert("Error al agregar web");
                        }
                    });
                }
            }
        }); //end_onClick_btnAdd

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Web solicitada al tutor");
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent web = new Intent(WebList.this, WebBrowser.class);
                String url = (String) (mListView.getItemAtPosition(position));
                web.putExtra("url", url);
                startActivity(web);
            }
        });//end_OnItemClick_mListView


    }

    private void getWebList(String tutor) {
        db.collection("users").document(tutor).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                noteWeb = documentSnapshot.getData();
                webs = (ArrayList<String>) noteWeb.get("searches");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WebList.this, android.R.layout.simple_list_item_1, webs);
                mListView.setAdapter(adapter);
            }
        });
    }


    private void showAlert(String text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(text);
        alert.setPositiveButton("Aceptar", null);
        AlertDialog pop = alert.create();
        alert.show();
    }
}
