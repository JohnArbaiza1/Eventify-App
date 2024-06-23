package com.example.eventify.Adaptador;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.eventify.Fragments.InvitacionesFragment;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.activities.AgregarActivity;
import com.example.eventify.activities.EventoDetalles;
import com.example.eventify.services.eventoService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        //----------------------------------------------------------------------------
        //Button para invitar amigos a eventos
        Button btnInvitacion  = convertView.findViewById(R.id.btnInvitar);
        //----------------------------------------------------------------------------
        //Parte donde se trabajan los eventos
        //----------------------------------------------------------------------------
        //Evento del Button de invitacion
        btnInvitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos a la evento
                traslada(position);
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actualizar = new Intent(context, AgregarActivity.class);
                actualizar.putExtra("id", temporal.getIdEvento().toString());
                actualizar.putExtra("nombreEvento", temporal.getNombreEvento());
                actualizar.putExtra("imagenEvento", temporal.getImg());
                actualizar.putExtra("descripcionEvento", temporal.getDescripciN());
                actualizar.putExtra("asistenteEvento", String.valueOf(temporal.getAsistentes()));
                actualizar.putExtra("ubicacionEvento", temporal.getUbicacion());
                actualizar.putExtra("fechaCreacion", temporal.getFechaCreacion());
                actualizar.putExtra("fechaDelEvento", temporal.getFecha());
                actualizar.putExtra("categoriaEvento", temporal.getCategoria());
                actualizar.putExtra("nombrePersona", temporal.getUsername());
                context.startActivity(actualizar);
                Toast.makeText(context, "Editando", Toast.LENGTH_SHORT).show();
            }
        });
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventosDetalles = new Intent(context, EventoDetalles.class);
                eventosDetalles.putExtra("id", temporal.getIdEvento().toString());
                eventosDetalles.putExtra("nombreEvento", temporal.getNombreEvento());
                eventosDetalles.putExtra("imagenEvento", temporal.getImg());
                eventosDetalles.putExtra("descripcionEvento", temporal.getDescripciN());
                eventosDetalles.putExtra("asistenteEvento", String.valueOf(temporal.getAsistentes()));
                eventosDetalles.putExtra("ubicacionEvento", temporal.getUbicacion());
                eventosDetalles.putExtra("fechaCreacion", temporal.getFechaCreacion());
                eventosDetalles.putExtra("fechaDelEvento", temporal.getFecha());
                eventosDetalles.putExtra("categoriaEvento", temporal.getCategoria());
                eventosDetalles.putExtra("nombrePersona", temporal.getUsername());
                context.startActivity(eventosDetalles);
                Toast.makeText(context, "Visualizando", Toast.LENGTH_SHORT).show();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Evento");
                builder.setMessage("La accion siguiente Eliminara el evento Publicado, esta seguro de continuar ?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        eventoService eventoservice = retrofit.create(eventoService.class);
                        Call<Evento> eliminarEvento = eventoservice.deleteEvento(String.valueOf(temporal.getIdEvento()));
                        eliminarEvento.enqueue(new Callback<Evento>() {
                            @Override
                            public void onResponse(Call<Evento> call, Response<Evento> response) {
                                if(response.isSuccessful()){
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference cuposRef = database.getReference("Cupos");
                                    DatabaseReference nodoEvento = cuposRef.child(String.valueOf(temporal.getIdEvento()));
                                    nodoEvento.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                                                listEventos.remove(position);
                                                notifyDataSetChanged();
                                            }
                                            else{
                                                Toast.makeText(context, "Error al eliminar de Firebase", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.d("Error -> ", response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Evento> call, Throwable t) {
                                Log.d("Error -> ", t.getMessage());
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Evento no Eliminado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show().create();
            }
        });

        return convertView;
    }

    //----------------------------------------------------------------------------
    //Metodos
    //----------------------------------------------------------------------------

    //Metodo para mandar al usuario al fragment de invitacion + captura de la info
    public void traslada(int position){
        //Validamos que no tengamos datos nulos
        if (listEventos != null && position >= 0 && position < listEventos.size()) {
            //Obtenemos la posicion del evento para capturar sus datos
            String dataCategoria = listEventos.get(position).getCategoria();
            String dataName = listEventos.get(position).getNombreEvento();
            String dataDate = listEventos.get(position).getFecha();
            String dataIMG = listEventos.get(position).getImg();
            //System.out.println("Datos mandados: "+ dataName +" | " + dataCategoria +" | " + dataDate +" | " + dataIMG);
            InvitacionesFragment invitaciones = InvitacionesFragment.newInstance(dataCategoria, dataName, dataDate, dataIMG);
            //Realizamos la transaccion hacia fragmento con el formulario de invitacion
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, invitaciones).addToBackStack(null).commit();//Agregamos una  pila de retroceso por fuera necesarioacaso
        }else{
            Toast.makeText(context, "Error: Parece que algo salio mal", Toast.LENGTH_SHORT).show();
        }
    }
}
