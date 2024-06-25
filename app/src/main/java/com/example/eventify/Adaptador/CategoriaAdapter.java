package com.example.eventify.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.activities.AgregarCategoria;
import com.example.eventify.activities.EditarCategoria;
import com.example.eventify.services.categoriaService;
import com.example.eventify.services.eventoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditarCategoria.class);
                intent.putExtra("id", String.valueOf(dataCategoria.get(position).getIdCategoria()));
                intent.putExtra("nombre", dataCategoria.get(position).getCategoria());
                intent.putExtra("descripcion", dataCategoria.get(position).getDescripciN());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                eventoService eventoservice = retrofit.create(eventoService.class);
                Call<List<Evento>> eventoCall = eventoservice.getEventosAll();
                eventoCall.enqueue(new Callback<List<Evento>>() {
                    @Override
                    public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                        if (response.isSuccessful()) {
                            boolean Eliminar = false;
                            List<Evento> listEventos = response.body();
                            for (Evento item: listEventos) {
                                if(item.getIdCategoria() == dataCategoria.get(position).getIdCategoria()){
                                    Eliminar = true;
                                    break;
                                }
                            }
                            Log.d("Valor", String.valueOf(Eliminar));
                            if(!Eliminar){
                                Log.d("Entra", "SI ENTRA");
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                categoriaService categoriaservice = retrofit.create(categoriaService.class);
                                Call<Categoria> deleteCall = categoriaservice.deleteCategoria(String.valueOf(dataCategoria.get(position).getIdCategoria()));
                                deleteCall.enqueue(new Callback<Categoria>() {
                                    @Override
                                    public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(context, "Categoria Eliminada", Toast.LENGTH_SHORT).show();
                                            dataCategoria.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Categoria> call, Throwable t) {
                                        Toast.makeText(context, "Ocurrio un error al Conectar con API", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Log.d("Entra", "no entra");
                                Toast.makeText(context, "No se puede Eliminar la categoria porque hay Eventos Registrados en esta Categoria", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Error al obtener los eventos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Evento>> call, Throwable t) {
                        
                    }
                });
                
                
                
                
            }
        });
        return convertView;
    }
}
