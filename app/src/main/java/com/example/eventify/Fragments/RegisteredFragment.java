package com.example.eventify.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventify.Adaptador.eventsRegistered;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.eventoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisteredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisteredFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<Integer> listaId;
    public ListView lista;
    public List<Evento> listaEventos;
    public eventsRegistered adapter;
    private FirebaseAuth mAuth;

    public RegisteredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisteredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisteredFragment newInstance(String param1, String param2) {
        RegisteredFragment fragment = new RegisteredFragment();
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
        View root = inflater.inflate(R.layout.fragment_registered, container, false);
        lista = root.findViewById(R.id.listaRegistrados);

        listaId = new ArrayList<>();
        listaEventos = new ArrayList<>();
        adapter = new eventsRegistered(getContext(), listaEventos);
        lista.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        actualizar();
        return  root;
    }
    public void actualizar(){
        FirebaseUser currenuser = mAuth.getCurrentUser();
        String emailCurrenUser = currenuser.getEmail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cupos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    for(DataSnapshot item2: item.getChildren()){
                        String email = item2.getValue(String.class);
                        if(emailCurrenUser.equals(email)){
                            Integer id = Integer.parseInt(item.getKey());
                            listaId.add(id);
                            break;
                        }
                    }
                }
                if(listaId.size()!=0) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    eventoService service = retrofit.create(eventoService.class);
                    Call<List<Evento>> call = service.getEventosAll();
                    call.enqueue(new Callback<List<Evento>>() {
                        @Override
                        public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                            List<Evento> todosEventos = response.body();
                            for (Evento item: todosEventos) {
                                if(listaId.contains(item.getIdEvento())){
                                    listaEventos.add(item);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<List<Evento>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Toast.makeText(getContext(), "No cuenta con Inscripciones a Eventos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}