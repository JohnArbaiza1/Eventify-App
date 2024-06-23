package com.example.eventify.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eventify.Objets.Notificacion;
import com.example.eventify.R;

import org.w3c.dom.Text;

import java.util.List;

public class notificacionAdapter extends BaseAdapter {
    public Context context;
    public List<Notificacion> listaNotificacion;

    public notificacionAdapter(Context context, List<Notificacion> listaNotificacion) {
        this.context = context;
        this.listaNotificacion = listaNotificacion;
    }

    @Override
    public int getCount() {
        return listaNotificacion.size();
    }

    @Override
    public Object getItem(int position) {
        return listaNotificacion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.notificacion_item, null);
        TextView fecha = convertView.findViewById(R.id.txtFecha);
        TextView titulo = convertView.findViewById(R.id.txtTitulo);
        TextView mensaje = convertView.findViewById(R.id.txtMensaje);
        fecha.setText(listaNotificacion.get(position).getFechaHora());
        titulo.setText(listaNotificacion.get(position).getTitulo());
        mensaje.setText(listaNotificacion.get(position).getMensaje());
        return convertView;
    }
}
