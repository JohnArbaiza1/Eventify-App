package com.example.eventify.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventify.Adaptador.notificacionAdapter;
import com.example.eventify.Objets.Notificacion;
import com.example.eventify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<Notificacion> listaFirebase;
    public notificacionAdapter adapter;
    public ListView lista;
    private FirebaseAuth mAuth;
    private DatabaseReference notificationsRef;

    public NotifiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifiFragment newInstance(String param1, String param2) {
        NotifiFragment fragment = new NotifiFragment();
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
        View root = inflater.inflate(R.layout.fragment_notifi, container, false);
        lista = root.findViewById(R.id.listaNotificaciones);
        listaFirebase = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notificaciones").child(user.getUid());

        // Obtener las notificaciones del usuario
        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        Notificacion notificacion = item.getValue(Notificacion.class);
                        if (notificacion != null) {
                            listaFirebase.add(notificacion);
                        }
                    }
                    adapter = new notificacionAdapter(getContext(), listaFirebase);
                    lista.setAdapter(adapter);
                }
                else {
                    Toast.makeText(getContext(), "Lista vacia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
        return  root;
    }
}