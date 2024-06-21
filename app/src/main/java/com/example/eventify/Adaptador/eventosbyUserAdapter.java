package com.example.eventify.Adaptador;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class eventosbyUserAdapter extends BaseAdapter {
    public Context context;
    public List<Evento> listEventos;

    public eventosbyUserAdapter(Context context, List<Evento> listEventos) {
        this.context = context;
        this.listEventos = listEventos;
    }

    @Override
    public int getCount() {
        return listEventos.size();
    }

    @Override
    public Object getItem(int position) {
        return listEventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.eventos_by_user_item, null);
        Evento temporal = listEventos.get(position);
        ImageView imgEvento = convertView.findViewById(R.id.imageView11);
        TextView txtNombreEvento = convertView.findViewById(R.id.txtNombre);
        TextView txtFechaEvento = convertView.findViewById(R.id.txtFecha);
        TextView txtCuposEvento = convertView.findViewById(R.id.txtCupos);
        ImageView btnEditar = convertView.findViewById(R.id.imgEdit);
        ImageView btnVer = convertView.findViewById(R.id.imgSee);
        ImageView btnEliminar = convertView.findViewById(R.id.imgDelete);
        Log.d("objeto", String.valueOf(temporal.getImg()));
        Picasso.get().load(String.valueOf(temporal.getImg())).into(imgEvento);
        txtNombreEvento.setText(temporal.getNombreEvento());
        txtFechaEvento.setText(temporal.getFecha());
        txtCuposEvento.setText(String.valueOf(temporal.getAsistentes()));
        return convertView;
    }
}
