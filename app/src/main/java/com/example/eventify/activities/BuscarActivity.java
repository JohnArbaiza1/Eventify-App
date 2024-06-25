package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.eventify.Fragments.DatePickerFragment2;
import com.example.eventify.R;

import java.util.ArrayList;
import java.util.List;

public class BuscarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public Spinner spinnerOpciones, spinnerAPI;
    public EditText txtFecha;
    public ListView listBusquedaGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        spinnerOpciones = findViewById(R.id.spinnerBusqueda);
        spinnerAPI = findViewById(R.id.spinnerAPI);
        txtFecha = findViewById(R.id.fechasBusqueda);
        listBusquedaGeneral = findViewById(R.id.listViewBusquedaGeneral);

        List<String> opciones = new ArrayList<>();
        opciones.add("Categoria");
        opciones.add("Ubicacion");
        opciones.add("Fecha");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        spinnerOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                if ("Fecha".equals(selectedOption)) {
                    txtFecha.setVisibility(View.VISIBLE);
                } else {
                    txtFecha.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerDialog() {
        DialogFragment datePickerFragment = new DatePickerFragment2();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Crear una cadena con la fecha seleccionada
        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        // Mostrar la fecha seleccionada en el EditText de fecha
        txtFecha.setText(selectedDate);
    }
}