package com.example.recycleractivity;

//import com.google.firebase.Timestamp;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Evento {
    private String id;
    private String organizador;
    private Timestamp fechahora;
    private float duracion;
    private String deporte;
    private ArrayList<String> Asistentes;
    private String descrpicion;

    public Evento() {
        this.id="nuevoEvento";
        this.organizador="No organizador";
        this.fechahora = new Timestamp(System.currentTimeMillis());
        this.duracion = 1;
        this.deporte = "neporte";
        Asistentes = new ArrayList<>();
        this.descrpicion = "no descripción";

    }

    public Evento(String organizador, Timestamp fechahora, float duracion, String deporte, ArrayList<String> asistentes, String descrpicion) {
        this.id = "evento-"+organizador+fechahora.toString();
        this.organizador = organizador;
        this.fechahora = fechahora;
        this.duracion = duracion;
        this.deporte = deporte;
        Asistentes = asistentes;
        this.descrpicion = descrpicion;
    }


    public String getOrganizador() {
        return organizador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public ArrayList<String> getAsistentes() {
        return Asistentes;
    }

    public void setAsistentes(ArrayList<String> asistentes) {
        Asistentes = asistentes;
    }

    public String getDescrpicion() {
        return descrpicion;
    }

    public void setDescrpicion(String descrpicion) {
        this.descrpicion = descrpicion;
    }

    public Timestamp getFechahora() {
        return fechahora;
    }

    public void setFechahora(Timestamp fechahora) {
        this.fechahora = fechahora;
    }
}
