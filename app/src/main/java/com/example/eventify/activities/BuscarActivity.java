package com.example.eventify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.eventify.Adaptador.EventosAdapter;
import com.example.eventify.Fragments.DatePickerFragment2;
import com.example.eventify.Objets.Categoria;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.categoriaService;
import com.example.eventify.services.eventoService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public Spinner spinnerOpciones, spinnerAPI;
    public EditText txtFecha;
    public ListView listBusquedaGeneral;
    public List<Categoria> categorias;
    public List<Evento> eventos;
    public categoriaService categoriaService;
    public eventoService eventoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        spinnerOpciones = findViewById(R.id.spinnerBusqueda);
        spinnerAPI = findViewById(R.id.spinnerAPI);
        txtFecha = findViewById(R.id.fechasBusqueda);
        listBusquedaGeneral = findViewById(R.id.listViewBusquedaGeneral);

        categorias = new ArrayList<>();
        eventos = new ArrayList<>();

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        categoriaService = retrofit.create(categoriaService.class);
        eventoService = retrofit.create(eventoService.class);
        loadEventos();

        spinnerOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                if ("Fecha".equals(selectedOption)) {
                    spinnerAPI.setEnabled(false);
                    txtFecha.setVisibility(View.VISIBLE);
                    listBusquedaGeneral.setAdapter(null);
                } else if ("Categoria".equals(selectedOption)) {
                    spinnerAPI.setEnabled(true);
                    txtFecha.setVisibility(View.INVISIBLE);
                    loadCategoriasIntoSpinner();
                }else if("Ubicacion".equals(selectedOption)){
                    spinnerAPI.setEnabled(true);
                    txtFecha.setVisibility(View.INVISIBLE);
                    loadUbicacionesIntoSpinner();
                }else {
                    txtFecha.setVisibility(View.INVISIBLE);
                    spinnerAPI.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAPI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String selectedOption = (String) spinnerOpciones.getSelectedItem();
                if ("Categoria".equals(selectedOption)) {
                    int categoriaId = getCategoriaIdByName(selectedItem); // Método para obtener el ID de la categoría por nombre
                    loadEventosByCategoria(categoriaId);  // Cargar eventos en el ListView
                } else if ("Ubicacion".equals(selectedOption)) {
                    loadEventosByUbicacion(selectedItem);  // Cargar eventos en el ListView
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadEventosByUbicacion(String selectedItem) {
        List<Evento> eventosFiltrados = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.getUbicacion().equals(selectedItem)) {
                eventosFiltrados.add(evento);
            }
        }
        EventosAdapter adapter = new EventosAdapter(BuscarActivity.this, eventosFiltrados);
        listBusquedaGeneral.setAdapter(adapter);
    }

    private void loadUbicacionesIntoSpinner() {
        Set<String> ubicacionesSet = new HashSet<>();
        for (Evento evento : eventos) {
            String nombreValidar = evento.getUbicacion().toString().trim();
            if(!ubicacionesSet.contains(evento.getUbicacion())){
                ubicacionesSet.add(evento.getUbicacion());
            }
            ubicacionesSet.add(evento.getUbicacion());
        }
        List<String> ubicaciones = new ArrayList<>(ubicacionesSet);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(BuscarActivity.this, android.R.layout.simple_spinner_item, ubicaciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAPI.setAdapter(adapter);
    }
    private void loadEventos() {
        Call<List<Evento>> call = eventoService.getEventosAll();
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful()) {
                    eventos = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                // Manejar error
            }
        });
    }
    // Método para cargar eventos por fecha
    private void loadEventosByFecha(String fechaSeleccionada) {
        List<Evento> eventosFiltrados = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.getFecha().equals(fechaSeleccionada)) { // Suponiendo que getFecha() devuelve la fecha del evento
                eventosFiltrados.add(evento);
            }
        }
        EventosAdapter adapter = new EventosAdapter(BuscarActivity.this, eventosFiltrados);
        listBusquedaGeneral.setAdapter(adapter);
    }

    private void loadEventosByCategoria(int categoriaId) {
        List<Evento> eventosFiltrados = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.getIdCategoria() == categoriaId) {
                eventosFiltrados.add(evento);
            }
        }
        EventosAdapter adapter = new EventosAdapter(BuscarActivity.this, eventosFiltrados);
        listBusquedaGeneral.setAdapter(adapter);
    }
    private int getCategoriaIdByName(String selectedCategoria) {
        for (Categoria categoria : categorias) { // Asumiendo que tienes una lista de categorías cargadas previamente
            if (categoria.getCategoria().equals(selectedCategoria)) {
                return categoria.getIdCategoria();
            }
        }
        return -1; // ID inválido si no se encuentra la categoría
    }

    private void loadCategoriasIntoSpinner() {
        Call<List<Categoria>> call = categoriaService.getCategoriasAll();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful()){
                    categorias = response.body();
                    List<String> categoriasNames = new ArrayList<>();
                    for (Categoria categoria : categorias) {
                        categoriasNames.add(categoria.getCategoria());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BuscarActivity.this, android.R.layout.simple_spinner_item, categoriasNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAPI.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Log.d("Error -> ", t.getMessage());
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
        loadEventosByFecha(selectedDate);
    }
}