package com.example.eventify.Adaptador;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.eventify.Fragments.InvitacionesFragment;
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
                Toast.makeText(context, "Editando", Toast.LENGTH_SHORT).show();
            }
        });
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Visualizando", Toast.LENGTH_SHORT).show();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Eliminando", Toast.LENGTH_SHORT).show();
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
