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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
                                //link de imagen ya proveniente de Firebase ********************
                                //con mAutn se accede a la informacion del usuario en linea
                                String image_download_link = uri.toString();
                                Toast.makeText(AgregarActivity.this, image_download_link, Toast.LENGTH_SHORT).show();
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

        // Agregar opciones al Spinner
        String[] opciones = {"Concierto", "Congreso", "Cumpleaños", "15 años", "Boda"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categoria_eventos.setAdapter(adapter);
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

}