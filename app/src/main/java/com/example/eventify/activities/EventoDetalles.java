package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventoDetalles extends AppCompatActivity {

    public ImageView imgEvento;
    public Button btnInscribirseEvento;
    public TextView txtNombreEvento, txtFechaDeEvento, txtFechaDeCreacion, txtUbicacionEvento,
            txtAsistenteEvento, txtCategoriaEvento, txtNombrePersona, txtDescripcionEvento;
    public DatabaseReference mdataBase;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalles);

        //llamada la referencia de Firebase
        mdataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        imgEvento = findViewById(R.id.post_detail_image);

        btnInscribirseEvento = findViewById(R.id.btnInscribirseEvento);

        txtNombreEvento = findViewById(R.id.nombreEventoDetalle);
        txtFechaDeEvento = findViewById(R.id.fechaDelEvento);
        txtFechaDeCreacion = findViewById(R.id.fechaPublicacionEvento);
        txtUbicacionEvento = findViewById(R.id.ubicacionEventosDetalle);
        txtAsistenteEvento = findViewById(R.id.asistenteEventosDetalle);
        txtCategoriaEvento = findViewById(R.id.categoriaEventoDetalle);
        txtNombrePersona = findViewById(R.id.nombrePersonaDetalle);
        txtDescripcionEvento = findViewById(R.id.descripcionEventosDetalle);
        validarBoton(getIntent().getExtras().getString("id"));

        String img = getIntent().getExtras().getString("imagenEvento");
        Glide.with(this).load(img).into(imgEvento);

        String nombre = getIntent().getExtras().getString("nombreEvento");
        txtNombreEvento.setText(nombre);

        String fechaCreacion = getIntent().getExtras().getString("fechaCreacion");
        txtFechaDeCreacion.setText(fechaCreacion);

        String fechaEvento = getIntent().getExtras().getString("fechaDelEvento");
        txtFechaDeEvento.setText(fechaEvento);

        String ubicacion = getIntent().getExtras().getString("ubicacionEvento");
        txtUbicacionEvento.setText("Ubicacion: " + ubicacion);
        //Verificación de Cupos Disponibles
        txtAsistenteEvento.setText("Cupos: ");
        String id = getIntent().getExtras().getString("id");
        String asistencia = getIntent().getExtras().getString("asistenteEvento");
        verificarCupos(id, asistencia);

        String categoria = getIntent().getExtras().getString("categoriaEvento");
        txtCategoriaEvento.setText("Categoria: " + categoria);

        String descripcion = getIntent().getExtras().getString("descripcionEvento");
        txtDescripcionEvento.setText("Descripcion: " + descripcion);

        String nombrePersona = getIntent().getExtras().getString("nombrePersona");
        txtNombrePersona.setText("Publicado por: " + nombrePersona);

        //sesion del usuario actual:
        FirebaseUser currenUser = mAuth.getCurrentUser();
        btnInscribirseEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference eventoRef = mdataBase.child("Eventos").child(id);
                eventoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            boolean isAlreadyRegistered = false;
                            String registeredKey = null;
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String correo = child.getValue(String.class);
                                if (correo != null && correo.equals(currenUser.getEmail())) {
                                    isAlreadyRegistered = true;
                                    registeredKey = child.getKey();
                                    break;
                                }
                            }
                            if(isAlreadyRegistered){
                                eventoRef.child(registeredKey).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    btnInscribirseEvento.setText("INSCRIBIRSE");
                                                    int colorPersonalizado = Color.rgb(255, 130, 4);
                                                    btnInscribirseEvento.setBackgroundColor(colorPersonalizado);
                                                    Toast.makeText(EventoDetalles.this, "Desinscrito del Evento", Toast.LENGTH_SHORT).show();
                                                    verificarCupos(id, asistencia); // Actualizar cupos después de desinscribirse
                                                } else {
                                                    Toast.makeText(EventoDetalles.this, "Error al desinscribirse", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else {
                                Map<String, Object> correos = new HashMap<>();
                                correos.put(currenUser.getDisplayName().toString(), currenUser.getEmail());
                                eventoRef.updateChildren(correos).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    btnInscribirseEvento.setText("Anular Inscripcion");
                                                    int colorRojo = Color.rgb(255, 0, 0);
                                                    btnInscribirseEvento.setBackgroundColor(colorRojo);
                                                    Toast.makeText(EventoDetalles.this, "Registrado al Evento", Toast.LENGTH_SHORT).show();
                                                    verificarCupos(id, asistencia);
                                                } else {
                                                    Toast.makeText(EventoDetalles.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                        else{

                            Map<String, Object> datosEvento = new HashMap<>();
                            datosEvento.put(currenUser.getDisplayName().toString(),currenUser.getEmail());
                            eventoRef.setValue(datosEvento).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            btnInscribirseEvento.setText("Anular Inscripcion");
                                            int colorRojo = Color.rgb(255, 0, 0);
                                            btnInscribirseEvento.setBackgroundColor(colorRojo);
                                            Toast.makeText(EventoDetalles.this, "Agregado", Toast.LENGTH_SHORT).show();
                                            verificarCupos(id, asistencia);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EventoDetalles.this, "Fallo", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EventoDetalles.this, "Error de Referencia", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void verificarCupos(String id, String asistenteEvento) {
        DatabaseReference eventoRef = mdataBase.child("Eventos").child(id);

        eventoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> correos = new ArrayList<>();
                    for (DataSnapshot item: snapshot.getChildren()) {
                        String correo = item.getValue(String.class);
                        correos.add(correo);
                    }
                    String cupos = String.valueOf(Integer.parseInt(asistenteEvento)-correos.size());
                    txtAsistenteEvento.setText("Cupos: "+cupos);
                }
                else{
                    txtAsistenteEvento.setText("Cupos: "+asistenteEvento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventoDetalles.this, "Error al llamar la Base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validarBoton(String id){
        FirebaseUser init = mAuth.getCurrentUser();
        DatabaseReference eventoRef = mdataBase.child("Eventos").child(id);
        eventoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    boolean isAlreadyRegistered = false;
                    String registeredKey = null;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String correo = child.getValue(String.class);
                        if (correo != null && correo.equals(init.getEmail())) {
                            isAlreadyRegistered = true;
                            registeredKey = child.getKey();
                            break;
                        }
                    }
                    if(isAlreadyRegistered){
                        btnInscribirseEvento.setText("Anular Inscripcion");
                        int color = Color.rgb(255, 0, 0);
                        btnInscribirseEvento.setBackgroundColor(color);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}