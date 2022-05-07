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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventos = new ArrayList<>();
        obtenerEventosBase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){

        eventos.add(new Evento("vesgae", Timestamp.valueOf("2018-11-12 01:02:03.123456789"),(float)1.5,"running",new ArrayList<>(),"vuelta a la jave"));
        eventos.add(new Evento("velorio", Timestamp.valueOf("2018-11-12 01:02:03.123456789"),(float)1.5,"futbol",new ArrayList<>(),"cotejo en la jave"));
        eventos.add(new Evento("vesgae", Timestamp.valueOf("2018-11-12 01:02:03.123456789"),(float)1.5,"running",new ArrayList<>(),"vuelta a la jave"));

        EventoAdapter eventoAdapter = new EventoAdapter(eventos, this, new EventoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Evento evento) {
                showToast(evento.getDeporte());
            }
        });
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventoAdapter);

    }

    private void showToast(String mensaje){
        Toast.makeText(this,"Evento clicado: "+mensaje,Toast.LENGTH_LONG).show();
    }

    //Ahora metodo para obtener los Eventos en base a un correo de usuario
    private void obtenerEventosBase(){

        String correo = "vesga.ed@gmail.com";
        ArrayList<String> eventosStrings = new ArrayList<>();
        ArrayList<Usuario> usuarios = null;
        DocumentReference docRef = db.collection("Usuario").document(correo);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                ArrayList<String> idEventos =usuario.getEventos();
                for(String idEvento: idEventos){
                    DocumentReference RefEvento = db.collection("evento").document(idEvento);
                    RefEvento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    String deporte = document.getString("deporte");
                                    Evento nuevoEvento = new Evento();
                                    nuevoEvento.setDeporte(deporte);
                                    eventos.add(nuevoEvento);
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