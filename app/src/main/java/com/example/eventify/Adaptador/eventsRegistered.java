package com.example.eventify.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.activities.EventoDetalles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class eventsRegistered extends BaseAdapter {
    public Context context;
    public List<Evento> listaRegistrados;

    public eventsRegistered(Context context, List<Evento> listaRegistrados) {
        this.context = context;
        this.listaRegistrados = listaRegistrados;
    }

    @Override
    public int getCount() {
        return listaRegistrados.size();
    }

    @Override
    public Object getItem(int position) {
        return listaRegistrados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.events_registered_item, null);
        ImageView imgEvento = convertView.findViewById(R.id.imageView12);
        TextView txtNombre = convertView.findViewById(R.id.txtNombre);
        TextView txtFechaEvento = convertView.findViewById(R.id.txtFecha);
        Evento tem = listaRegistrados.get(position);
        Picasso.get().load(String.valueOf(tem.getImg())).into(imgEvento);
        txtNombre.setText(tem.getNombreEvento());
        txtFechaEvento.setText(tem.getFecha());
        return convertView;
    }
}
