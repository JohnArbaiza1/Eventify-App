package com.example.eventify.Adaptador;

import android.content.Context;
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

        titulo.setText(dataEvento.get(position).getNombreEvento());
        asistentes.setText(String.valueOf(dataEvento.get(position).getAsistentes()));
        ubicacion.setText(dataEvento.get(position).getUbicacion());

        Picasso.get().load(dataEvento.get(position).getImg()).into(imagenEventos);

        return convertView;
    }
}
