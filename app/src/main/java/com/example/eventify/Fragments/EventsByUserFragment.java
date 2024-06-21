package com.example.eventify.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventify.Adaptador.eventosbyUserAdapter;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.eventoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsByUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsByUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListView lista;
    private FirebaseAuth mAuth;
    public eventosbyUserAdapter adapter;

    public EventsByUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsByUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsByUserFragment newInstance(String param1, String param2) {
        EventsByUserFragment fragment = new EventsByUserFragment();
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
        View root = inflater.inflate(R.layout.fragment_events_by_user, container, false);
        lista = root.findViewById(R.id.listaEventosByUser);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventoService service = retrofit.create(eventoService.class);
        Log.d("User id", currentUser.getUid());
        Call<List<Evento>> call = service.getEventoByUserId(currentUser.getUid());
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                List<Evento>  eventos = response.body();
                if(eventos.size() != 0){
                    Log.d("Esta entrando", "Si entra");
                    adapter = new eventosbyUserAdapter(getContext(), eventos);
                    lista.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getContext(), "La lista esta vacia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Log.d("Error -> ", t.getMessage());
            }
        });
        return root;
    }
}