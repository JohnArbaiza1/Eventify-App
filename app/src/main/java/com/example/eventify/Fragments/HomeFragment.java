package com.example.eventify.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventify.Adaptador.EventosAdapter;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.eventoService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventosAdapter eventosAdapter;
    public ListView listEventosListView;
    public List<Evento> listEventos;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        listEventosListView = root.findViewById(R.id.listEventos);
        listEventos = new ArrayList<>(); // Lista para almacenar los eventos

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        eventoService eventoService = retrofit.create(eventoService.class);

        // Realizar la llamada asíncrona
        Call<List<Evento>> call = eventoService.getEventosAll();
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful()) {
                    listEventos = response.body();

                    for (Evento item: response.body()) {
                        System.out.println("Nombre -> " + item.getNombreEvento());
                        System.out.println("Asistentes -> " + item.getAsistentes());
                        System.out.println("Ubicacion -> " + item.getUbicacion());
                    }

//                    // Configurar el adaptador y conectarlo al ListView
//                    eventosAdapter = new EventosAdapter(getActivity().getApplicationContext(), listEventos);
//                    listEventosListView.setAdapter(eventosAdapter);
                    eventosAdapter = new EventosAdapter(getContext().getApplicationContext(), listEventos);
                    listEventosListView.setAdapter(eventosAdapter);
                } else {
                    Toast.makeText(getActivity(), "Error al obtener los eventos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}