package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventify.Fragments.DatePickerFragment;
import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.categoriaService;
import com.example.eventify.services.eventoService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgregarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_GALLERY = 2;
    public Uri pickedImgUri = null;
    public Intent temporal;
    static int REQUESCODE = 2;
    public ImageView img_eventos;
    public EditText txt_nombre_eventos, txt_descripcion_eventos, txt_ubicacion_eventos, txt_cupos_eventos, txt_fecha_eventos;
    public Spinner spinner_categoria_eventos;
    public TextView titulo;
    public Button btn_guardar_eventos;
    FirebaseAuth mAuth;
    FirebaseUser currenUser;
    public List<String> opcionesSpinner;
    public List<Categoria> categoriaList;
    public String img;
    public String nombre;
    public String fechaCreacion;
    public String fechaEvento;
    public String ubicacion;
    public String id;
    public String asistencia;
    public String categoria;
    public String descripcion;
    public String nombrePersona;
    public DatabaseReference mdataBase;
    public Integer cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        mdataBase = FirebaseDatabase.getInstance().getReference();
        cantidad = 0;

        img_eventos = findViewById(R.id.img_eventos);
        txt_nombre_eventos = findViewById(R.id.nombre_eventos);
        txt_descripcion_eventos = findViewById(R.id.descripcion_eventos);
        txt_ubicacion_eventos = findViewById(R.id.ubicacion_eventos);
        txt_cupos_eventos = findViewById(R.id.cupos_eventos);
        spinner_categoria_eventos = findViewById(R.id.spinner_eventos);
        btn_guardar_eventos = findViewById(R.id.btn_guardar_eventos);
        titulo = findViewById(R.id.titulo);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            img = extras.getString("imagenEvento");
            nombre = extras.getString("nombreEvento");
            fechaCreacion = extras.getString("fechaCreacion");
            fechaEvento = extras.getString("fechaDelEvento");
            ubicacion = extras.getString("ubicacionEvento");
            id = extras.getString("id");
            asistencia = extras.getString("asistenteEvento");
            categoria = extras.getString("categoriaEvento");
            descripcion = extras.getString("descripcionEvento");
            nombrePersona = extras.getString("nombrePersona");
            verificarCupos(id);
        }

        txt_fecha_eventos = findViewById(R.id.input_fecha); // Asociar EditText de fecha

        mAuth = FirebaseAuth.getInstance();
        currenUser = mAuth.getCurrentUser();
        categoriaList = new ArrayList<>();

        image_click();

        // Configurar clic para el EditText de fecha
        txt_fecha_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn_guardar_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_guardar_eventos.getText().equals("Actualizar")){
                    if(actualizar() && validarCampos()){
                        if (pickedImgUri != null) {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("events_images");
                            StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //****************** ACTUALIZAR EVENTO CON IMAGEN DIFERENTE **********************************//
                                            String image_download_link = uri.toString();
                                            Categoria categoriaSeleccionada = buscarCategoriaPorNombre(categoriaList, spinner_categoria_eventos.getSelectedItem().toString());
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
                                            eventoService eventoservice = retrofit.create(eventoService.class);
                                            Evento evento = new Evento(Integer.parseInt(id), txt_nombre_eventos.getText().toString(), Integer.parseInt(txt_cupos_eventos.getText().toString()),
                                                    txt_descripcion_eventos.getText().toString(), txt_ubicacion_eventos.getText().toString(), txt_fecha_eventos.getText().toString(), currenUser.getUid(),
                                                    currenUser.getDisplayName(), categoriaSeleccionada.getIdCategoria(), categoriaSeleccionada.getCategoria(), image_download_link, fechaCreacion());

                                            Log.d("Actualizar", "Evento a guardar: " + new Gson().toJson(evento));

                                            Call<Evento> actualizarEvento = eventoservice.updateEvent(id, evento);
                                            actualizarEvento.enqueue(new Callback<Evento>() {
                                                @Override
                                                public void onResponse(Call<Evento> call, Response<Evento> response) {
                                                    try {
                                                        if (response.isSuccessful()) {
                                                            Log.d("Actualizar Evento", "Evento guardado: " + new Gson().toJson(response.body()));
                                                            Toast.makeText(AgregarActivity.this, "Evento Actualizado", Toast.LENGTH_SHORT).show();
                                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                            DatabaseReference cuposRef = database.getReference("Cupos");
                                                            DatabaseReference notifiacionesRef = database.getReference("Notificaciones");
                                                            DatabaseReference nodoEvento = cuposRef.child(id);
                                                            nodoEvento.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()) {
                                                                        Log.d("Prueba", "Prueba");
                                                                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                                            String userId = userSnapshot.getKey();
                                                                            String dateTime = getFormattedDateTime();
                                                                            // Crear una nueva notificación
                                                                            String notificationId = notifiacionesRef.child(userId).push().getKey();
                                                                            if (notificationId != null) {
                                                                                Map<String, Object> notificationData = new HashMap<>();
                                                                                notificationData.put("fechaHora", dateTime); // Método para obtener la fecha y hora actual
                                                                                notificationData.put("mensaje", "El evento: " + nombre + " Al que estas Suscrito Ha sido Modificado por el Anfitrion");
                                                                                notificationData.put("titulo", "Evento Modificado");
                                                                                // Guardar la notificación en Firebase
                                                                                notifiacionesRef.child(userId).child(notificationId).setValue(notificationData);
                                                                                Log.d("Evento", "Nodo creado");
                                                                            }
                                                                        }
                                                                        enviarHome();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                    Toast.makeText(getApplicationContext(), "Error al obtener los usuarios inscritos", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        } else {
                                                            Log.e("AgregarEvento", "Error en la respuesta: " + response.errorBody().string());
                                                            Toast.makeText(AgregarActivity.this, "Fallo: " + response.message(), Toast.LENGTH_LONG).show();
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Evento> call, Throwable t) {
                                                    Log.e("AgregarEvento", "Error al agregar evento", t);
                                                    Toast.makeText(AgregarActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AgregarActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AgregarActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Categoria categoriaSeleccionada = buscarCategoriaPorNombre(categoriaList, spinner_categoria_eventos.getSelectedItem().toString());
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            eventoService eventoservice = retrofit.create(eventoService.class);
                            Evento evento = new Evento(Integer.parseInt(id), txt_nombre_eventos.getText().toString(), Integer.parseInt(txt_cupos_eventos.getText().toString()),
                                    txt_descripcion_eventos.getText().toString(), txt_ubicacion_eventos.getText().toString(), txt_fecha_eventos.getText().toString(), currenUser.getUid(),
                                    currenUser.getDisplayName(), categoriaSeleccionada.getIdCategoria(), categoriaSeleccionada.getCategoria(), img, fechaCreacion());

                            Log.d("Actualizar", "Evento a guardar: " + new Gson().toJson(evento));

                            Call<Evento> actualizarEvento = eventoservice.updateEvent(id, evento);
                            actualizarEvento.enqueue(new Callback<Evento>() {
                                @Override
                                public void onResponse(Call<Evento> call, Response<Evento> response) {
                                    try {
                                        if (response.isSuccessful()) {
                                            Log.d("Actualizar Evento", "Evento guardado: " + new Gson().toJson(response.body()));
                                            Toast.makeText(AgregarActivity.this, "Evento Actualizado", Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference cuposRef = database.getReference("Cupos");
                                            DatabaseReference notifiacionesRef = database.getReference("Notificaciones");
                                            DatabaseReference nodoEvento = cuposRef.child(id);
                                            nodoEvento.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        Log.d("Prueba", "Prueba");
                                                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                            String userId = userSnapshot.getKey();
                                                            String dateTime = getFormattedDateTime();
                                                            // Crear una nueva notificación
                                                            String notificationId = notifiacionesRef.child(userId).push().getKey();
                                                            if (notificationId != null) {
                                                                Map<String, Object> notificationData = new HashMap<>();
                                                                notificationData.put("fechaHora", dateTime); // Método para obtener la fecha y hora actual
                                                                notificationData.put("mensaje", "El evento " + nombre + " Al que estas Suscrito Ha sido Modificado por el Anfitrion");
                                                                notificationData.put("titulo", "Evento Modificado");
                                                                // Guardar la notificación en Firebase
                                                                notifiacionesRef.child(userId).child(notificationId).setValue(notificationData);
                                                                Log.d("Evento", "Nodo creado");
                                                            }
                                                        }
                                                        enviarHome();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getApplicationContext(), "Error al obtener los usuarios inscritos", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Log.e("AgregarEvento", "Error en la respuesta: " + response.errorBody().string());
                                            Toast.makeText(AgregarActivity.this, "Fallo: " + response.message(), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Evento> call, Throwable t) {
                                    Log.e("AgregarEvento", "Error al agregar evento", t);
                                    Toast.makeText(AgregarActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
                else{
                    if(validarCampos()) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("events_images");
                        StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //****************** LOGICA PARA AGREGAR EVENTO **********************************//

                                        String image_download_link = uri.toString();
                                        Categoria categoriaSeleccionada = buscarCategoriaPorNombre(categoriaList, spinner_categoria_eventos.getSelectedItem().toString());

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        eventoService eventoservice = retrofit.create(eventoService.class);
                                        Evento evento = new Evento(0, txt_nombre_eventos.getText().toString(), Integer.parseInt(txt_cupos_eventos.getText().toString()),
                                                txt_descripcion_eventos.getText().toString(), txt_ubicacion_eventos.getText().toString(), txt_fecha_eventos.getText().toString(), currenUser.getUid(),
                                                currenUser.getDisplayName(), categoriaSeleccionada.getIdCategoria(), categoriaSeleccionada.getCategoria(), image_download_link, fechaCreacion());

                                        Log.d("AgregarEvento", "Evento a guardar: " + new Gson().toJson(evento));

                                        Call<Evento> guardarEvento = eventoservice.saveEvento(evento);
                                        guardarEvento.enqueue(new Callback<Evento>() {
                                            @Override
                                            public void onResponse(Call<Evento> call, Response<Evento> response) {
                                                try {
                                                    if (response.isSuccessful()) {
                                                        Log.d("AgregarEvento", "Evento guardado: " + new Gson().toJson(response.body()));
                                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                        Toast.makeText(AgregarActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                    } else {
                                                        Log.e("AgregarEvento", "Error en la respuesta: " + response.errorBody().string());
                                                        Toast.makeText(AgregarActivity.this, "Fallo: " + response.message(), Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Evento> call, Throwable t) {
                                                Log.e("AgregarEvento", "Error al agregar evento", t);
                                                Toast.makeText(AgregarActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AgregarActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriaService categoriaservice = retrofit.create(categoriaService.class);
        Call<List<Categoria>> obtener = categoriaservice.getCategoriasAll();
        opcionesSpinner = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        obtener.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful() && response.body() !=null) {
                    for (Categoria item : response.body()) {
                        opcionesSpinner.add(item.getCategoria());
                        categoriaList.add(item);
                        //Toast.makeText(AgregarActivity.this, item.getIdCategoria(), Toast.LENGTH_SHORT).show();
                    }
                    if(categoria != null){
                        int opcion = adapter.getPosition(categoria);
                        spinner_categoria_eventos.setSelection(opcion);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(AgregarActivity.this, "Fallo en obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
        // Agregar opciones al Spinner
        spinner_categoria_eventos.setAdapter(adapter);
        if(img != null && nombre != null && descripcion != null && ubicacion != null && categoria != null && asistencia !=null && fechaEvento != null){
            Picasso.get().load(img).into(img_eventos);
            txt_nombre_eventos.setText(nombre);
            txt_descripcion_eventos.setText(descripcion);
            txt_ubicacion_eventos.setText(ubicacion);
            txt_cupos_eventos.setText(asistencia);

            txt_fecha_eventos.setText(fechaEvento);
            btn_guardar_eventos.setText("Actualizar");
            titulo.setText("Actualizando");
        }
    }
    private void verificarCupos(String id) {
        DatabaseReference eventoRef = mdataBase.child("Cupos").child(id);
        eventoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> correos = new ArrayList<>();
                    for (DataSnapshot item: snapshot.getChildren()) {
                        String correo = item.getValue(String.class);
                        correos.add(correo);
                    }
                    cantidad = correos.size();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AgregarActivity.this, "Error al llamar la Base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean actualizar(){
        boolean estado = false;
        Integer cuposNuevos = Integer.parseInt(txt_cupos_eventos.getText().toString());
        verificarCupos(id);
        if(cuposNuevos <= 0){
            estado = false;
            Toast.makeText(this, "No se puede ingresar cantidades de cupos menores a 0", Toast.LENGTH_SHORT).show();
        } else if (cantidad > cuposNuevos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error al modificar cupos");
            builder.setMessage("Ya estan usuarios registrados mayor a la capacidad que se quiere establecer");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show().create();
            estado = false;
        } else{
            Log.d("Estado", String.valueOf(cantidad));
            estado = true;
        }
        return estado;
    }
    private boolean validarCampos(){
        boolean camposValidar = false;
        String nombreValidar = txt_nombre_eventos.getText().toString().trim();
        String descripcionValidar = txt_descripcion_eventos.getText().toString().trim();
        String ubicacionValidar = txt_ubicacion_eventos.getText().toString().trim();
        String cuposValidar = txt_cupos_eventos.getText().toString().trim();
        String fechaValidar = txt_fecha_eventos.getText().toString().trim();
        if(nombreValidar.isEmpty()){
            txt_nombre_eventos.setError("Campo vacio");
            camposValidar = false;
        } else if (descripcionValidar.isEmpty()) {
            txt_descripcion_eventos.setError("Campo Vacio");
            camposValidar = false;
        } else if (ubicacionValidar.isEmpty()) {
            txt_ubicacion_eventos.setError("Campo Vacio");
            camposValidar = false;
        } else if (cuposValidar.isEmpty()) {
            txt_cupos_eventos.setError("Campo Vacio");
            camposValidar = false;
        } else if (fechaValidar.isEmpty()) {
            txt_fecha_eventos.setError("Campo Vacio");
            camposValidar = false;
        }
        else{
            camposValidar = true;
        }
        return camposValidar;
    }

    // Método para mostrar el DatePickerDialog
    private void showDatePickerDialog() {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Implementación del método onDateSet del DatePickerDialog.OnDateSetListener
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Crear una cadena con la fecha seleccionada
        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        // Mostrar la fecha seleccionada en el EditText de fecha
        txt_fecha_eventos.setText(selectedDate);
    }

    private String fechaCreacion(){
        // Obtener la fecha actual
        Date fechaActual = Calendar.getInstance().getTime();

        // Definir el formato
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        // Convertir la fecha a string
        String fechaActualString = formatoFecha.format(fechaActual);
        return fechaActualString;
    }
    private void image_click() {
        img_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions();
            }
        });
    }

    private void showImagePickerOptions() {
        String[] options = {"Cámara", "Galería"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen desde:");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    checkCameraPermission();
                } else {
                    openGallery();
                }
            }
        });
        builder.show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            openCamara();
        }

    }
    private void openCamara(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//          startActivityForResult(cameraIntent, REQUEST_CAMERA);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamara();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // Captura desde la cámara
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                img_eventos.setImageBitmap(imageBitmap);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
                Uri bitmapUri = Uri.parse(path);
                pickedImgUri = bitmapUri;


            } else if (requestCode == REQUEST_GALLERY) {
                // Selección desde la galería
                if (data != null && data.getData() != null) {
                    pickedImgUri = data.getData();
                    img_eventos.setImageURI(pickedImgUri);
                }
            }
        }
    }
    private Categoria buscarCategoriaPorNombre(List<Categoria> lista, String nombre) {
        for (Categoria categoria : lista) {
            if (categoria.getCategoria().equalsIgnoreCase(nombre)) {
                return categoria;
            }
        }
        return null; // Si no se encuentra la categoría
    }
    private void enviarHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("abrirInscripciones", "Eventos");
        startActivity(intent);
        finish();
    }

    public String getFormattedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}