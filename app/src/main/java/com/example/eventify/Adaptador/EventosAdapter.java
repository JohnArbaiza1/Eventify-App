package com.example.eventify.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventify.Objets.Evento;
import com.example.eventify.R;

import org.w3c.dom.Text;

import java.util.List;

import com.example.eventify.activities.EventoDetalles;
import com.squareup.picasso.Picasso;

public class EventosAdapter extends BaseAdapter {

    public Context context;
    public List<Evento> dataEvento;

    public EventosAdapter(Context context, List<Evento> dataEvento) {
        this.context = context;
        this.dataEvento = dataEvento;
    }

    @Override
    public int getCount() {
        return dataEvento.size();
    }

    @Override
    public Object getItem(int position) {
        return dataEvento.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.evento_items,null);

        TextView titulo = convertView.findViewById(R.id.tituloEventos);
        TextView asistentes = convertView.findViewById(R.id.asistenteEventos);
        TextView ubicacion = convertView.findViewById(R.id.ubicacionEventos);

        ImageView imagenEventos = convertView.findViewById(R.id.row_eventos);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventosDetalles = new Intent(context, EventoDetalles.class);
                eventosDetalles.putExtra("id", dataEvento.get(position).getIdEvento().toString());
                eventosDetalles.putExtra("nombreEvento", dataEvento.get(position).getNombreEvento());
                eventosDetalles.putExtra("imagenEvento", dataEvento.get(position).getImg());
                eventosDetalles.putExtra("descripcionEvento", dataEvento.get(position).getDescripciN());
                eventosDetalles.putExtra("asistenteEvento", String.valueOf(dataEvento.get(position).getAsistentes()));
                eventosDetalles.putExtra("ubicacionEvento", dataEvento.get(position).getUbicacion());
                eventosDetalles.putExtra("fechaCreacion", dataEvento.get(position).getFechaCreacion());
                eventosDetalles.putExtra("fechaDelEvento", dataEvento.get(position).getFecha());
                eventosDetalles.putExtra("categoriaEvento", dataEvento.get(position).getCategoria());
                eventosDetalles.putExtra("nombrePersona", dataEvento.get(position).getUsername());

                eventosDetalles.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(eventosDetalles);
            }
        });

        titulo.setText(dataEvento.get(position).getNombreEvento());
        asistentes.setText(String.valueOf(dataEvento.get(position).getAsistentes()));
        ubicacion.setText(dataEvento.get(position).getUbicacion());

        Picasso.get().load(dataEvento.get(position).getImg()).into(imagenEventos);

        return convertView;
    }
}
