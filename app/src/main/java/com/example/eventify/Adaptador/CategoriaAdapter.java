package com.example.eventify.Adaptador;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;

import java.util.List;

public class CategoriaAdapter extends BaseAdapter {

    public Context context;
    public List<Categoria> dataCategoria;
    private Typeface aclonicaFont;

    public CategoriaAdapter(Context context, List<Categoria> dataCategoria) {
        this.context = context;
        this.dataCategoria = dataCategoria;

        // Cargar la fuente Aclonica desde assets
        aclonicaFont = Typeface.createFromAsset(context.getAssets(), "Aclonica.ttf");
    }

    @Override
    public int getCount() {
        return dataCategoria.size();
    }

    @Override
    public Object getItem(int position) {
        return dataCategoria.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.categoria_items,null);

        TextView txtNombreCategoria = convertView.findViewById(R.id.txtNombreCategoria);
        TextView txtDescripcionCategoria = convertView.findViewById(R.id.txtDescripcionCategoria);
        TextView txtNombre = convertView.findViewById(R.id.textView4);
        TextView txtDescripcion = convertView.findViewById(R.id.textView9);
        Button btnActualizar = convertView.findViewById(R.id.btnActualizarCategoria);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminarCategoria);

        // Aplicar la fuente Aclonica
        txtNombreCategoria.setTypeface(aclonicaFont);
        txtDescripcionCategoria.setTypeface(aclonicaFont);
        txtNombre.setTypeface(aclonicaFont);
        txtDescripcion.setTypeface(aclonicaFont);
        btnActualizar.setTypeface(aclonicaFont);
        btnEliminar.setTypeface(aclonicaFont);

        txtNombreCategoria.setText(dataCategoria.get(position).getCategoria());
        txtDescripcionCategoria.setText(dataCategoria.get(position).getDescripciN());

        return convertView;
    }
}
