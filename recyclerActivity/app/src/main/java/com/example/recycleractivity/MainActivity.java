package com.example.recycleractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import static android.content.ContentValues.TAG;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Evento> eventos;
    Evento eventoActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        obtenerEventosBase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    private void showToast(String mensaje){
        Toast.makeText(this,"Evento clicado: "+mensaje,Toast.LENGTH_LONG).show();
    }

    private void setEventoActual(String campo, String valor){
        switch (campo){
            case "id":{
                eventoActual.setId(valor);
                break;
            }
            case "deporte":{
                eventoActual.setDeporte(valor);
                break;
            }
            case "organizador":{
                eventoActual.setOrganizador(valor);
                break;
            }
        }
    }

    private void deporteEvento(Evento e, String deporte){
        e.setDeporte(deporte);
    }
    private void obtenerNombreDeporte(String idDeporte){
        DocumentReference RefDeporte = db.collection("deportes").document(idDeporte);
        RefDeporte.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentDeporte = task.getResult();
                    if (documentDeporte.exists()) {
                        setEventoActual("deporte",documentDeporte.getString("nombre"));
                        showToast(documentDeporte.getString("nombre"));
                    } else {
                        showToast("no");
                    }
                } else {
                    showToast("ni");
                }
            }
        });
    }

    private void obtenerNombreOrganizador(String idUsuario){
        String nombre = "Usuario";
        DocumentReference RefDeporte = db.collection("Usuario").document(idUsuario);
        RefDeporte.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                setEventoActual("organizador", documentSnapshot.getString("nombre"));
            }

        });
    }

    //Ahora metodo para obtener los Eventos en base a un correo de usuario
    private void obtenerEventosBase() {

        eventos = new ArrayList<>();
        eventoActual = new Evento();
        String correo = "vesga.ed@gmail.com";
        ArrayList<String> eventosStrings = new ArrayList<>();
        ArrayList<Usuario> usuarios = null;
        DocumentReference docRef = db.collection("Usuario").document(correo);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                ArrayList<String> idEventos = usuario.getEventos();
                for (String idEvento : idEventos) {
                    DocumentReference RefEvento = db.collection("evento").document(idEvento);
                    RefEvento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "1 DocumentSnapshot data: " + document.getData());

                                    setEventoActual("id",document.getId());
                                    obtenerNombreOrganizador(document.getString("organizador"));
                                    DocumentReference RefDeporte = db.collection("deportes").document(document.getString("deporte"));
                                    RefDeporte.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentDeporte = task.getResult();
                                                if (documentDeporte.exists()) {
                                                    setEventoActual("deporte",documentDeporte.getString("nombre"));
                                                    showToast(documentDeporte.getString("nombre"));
                                                } else {
                                                    showToast("no");
                                                }
                                            } else {
                                                showToast("ni");
                                            }
                                        }
                                    });
                                    eventos.add(eventoActual);
                                    EventoAdapter eventoAdapter = new EventoAdapter(eventos, getApplicationContext(), new EventoAdapter.ItemClickListener() {
                                        @Override
                                        public void onItemClick(Evento evento) {
                                            showToast(evento.getDeporte());
                                        }
                                    });
                                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    recyclerView.setAdapter(eventoAdapter);


                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }

            }
        });
    }
}