package com.example.eventify.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventify.Objets.Evento;
import com.example.eventify.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import com.example.eventify.activities.EventoDetalles;
import com.squareup.picasso.Picasso;

public class EventosAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public List<Evento> dataEvento;
    private List<Evento> dataEventoOriginal;
    private EventoFilter eventoFilter;

    public EventosAdapter(Context context, List<Evento> dataEvento) {
        this.context = context;
        this.dataEvento = dataEvento;
        this.dataEventoOriginal = new ArrayList<>(dataEvento);
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
        asistentes.setText("Fecha del Evento: "+dataEvento.get(position).getFecha());
        ubicacion.setText("Lugar: " + dataEvento.get(position).getUbicacion());

        Picasso.get().load(dataEvento.get(position).getImg()).into(imagenEventos);

        return convertView;
    }

    private class EventoFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Evento> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataEventoOriginal);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Evento evento : dataEventoOriginal) {
                    if (evento.getNombreEvento().toLowerCase().contains(filterPattern)) {
                        filteredList.add(evento);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataEvento.clear();
            dataEvento.addAll((List<Evento>) results.values);
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if (eventoFilter == null) {
            eventoFilter = new EventoFilter();
        }
        return eventoFilter;
    }
}
