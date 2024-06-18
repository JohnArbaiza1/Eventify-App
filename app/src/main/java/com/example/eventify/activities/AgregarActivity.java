package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.categoriaService;
import com.example.eventify.services.eventoService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgregarActivity extends AppCompatActivity {

    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_GALLERY = 2;
    public Uri pickedImgUri = null;
    public Intent temporal;
    static int REQUESCODE = 2;
    public ImageView img_eventos;
    public EditText txt_nombre_eventos, txt_descripcion_eventos, txt_ubicacion_eventos, txt_cupos_eventos;
    public Spinner spinner_categoria_eventos;
    public Button btn_guardar_eventos;
    FirebaseAuth mAuth;
    FirebaseUser currenUser;
    public List<String> opcionesSpinner;
    public List<Categoria> categoriaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        img_eventos = findViewById(R.id.img_eventos);
        txt_nombre_eventos = findViewById(R.id.nombre_eventos);
        txt_descripcion_eventos = findViewById(R.id.descripcion_eventos);
        txt_ubicacion_eventos = findViewById(R.id.ubicacion_eventos);
        txt_cupos_eventos = findViewById(R.id.cupos_eventos);
        spinner_categoria_eventos = findViewById(R.id.spinner_eventos);
        btn_guardar_eventos = findViewById(R.id.btn_guardar_eventos);
        mAuth = FirebaseAuth.getInstance();
        currenUser = mAuth.getCurrentUser();
        categoriaList = new ArrayList<>();

        image_click();
        btn_guardar_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        txt_descripcion_eventos.getText().toString(), txt_ubicacion_eventos.getText().toString(), "15 de Septiembre de 2024", currenUser.getUid(),
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
                                Toast.makeText(AgregarActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
                    }
                });
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
        String[] opciones = {"Concierto", "Congreso", "Cumpleaños", "15 años", "Boda"};
        spinner_categoria_eventos.setAdapter(adapter);
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
}