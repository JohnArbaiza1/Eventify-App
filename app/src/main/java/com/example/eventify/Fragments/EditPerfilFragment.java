package com.example.eventify.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.services.eventoService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextInputEditText txtUserName;
    public TextInputEditText txtEmailUser;
    public Button btnEditar;
    public ProgressBar progressBar;

    public EditPerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPerfilFragment newInstance(String param1, String param2) {
        EditPerfilFragment fragment = new EditPerfilFragment();
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
        View root = inflater.inflate(R.layout.fragment_edit_perfil, container, false);
        txtUserName = root.findViewById(R.id.txtUserName);
        txtEmailUser = root.findViewById(R.id.txtEmailUser);
        btnEditar = root.findViewById(R.id.btnEditar);
        progressBar = root.findViewById(R.id.progressBar);
        Bundle bundle = getArguments();
        String nombre = bundle.getString("nombre");
        String correo = bundle.getString("correo");
        txtUserName.setText(nombre);
        txtEmailUser.setText(correo);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(!txtUserName.equals(nombre)){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txtUserName.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            actualizarEventosUsuario(user.getUid(), txtUserName.getText().toString());
                                            Toast.makeText(getContext(), "Nombre de Perfil Actualizado", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new PerfilFragment()).commit();
                                        }
                                    }
                                });

                    }
                }
            }
        });
        return  root;
    }
    
    private void actualizarEventosUsuario(String userId, String nuevoNombre){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventoService eventoservice = retrofit.create(eventoService.class);

        Call<List<Evento>> call = eventoservice.getEventoByUserId(userId);
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful()) {
                    List<Evento> eventos = response.body();
                    for (Evento evento : eventos) {
                        evento.setIdUsuario(userId);
                        evento.setUsername(nuevoNombre);
                        actualizarEvento(evento);
                    }
                } else {
                    Log.e("TAG", "Error retrieving events: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Log.e("TAG", "Error retrieving events: ", t);
            }
        });
    }

    private void actualizarEvento(Evento evento) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventify-api-rest-production.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventoService eventoservice = retrofit.create(eventoService.class);

        Call<Evento> call = eventoservice.updateEvent(evento.getIdEvento().toString(), evento);
        call.enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "Evento actualizado: " + new Gson().toJson(response.body()));
                } else {
                    Log.e("TAG", "Error updating event: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                Log.e("TAG", "Error updating event: ", t);
            }
        });
    }

    private boolean validarCampos(){
        boolean estado = false;
        String nombreValidar = txtUserName.getText().toString().trim();
        String emailValidar = txtEmailUser.getText().toString().trim();
        String emailPattern = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
        if(nombreValidar.isEmpty()){
            estado = false;
            txtUserName.setError("Debe ingresar un Usuario");
        } else if (emailValidar.isEmpty()) {
            txtEmailUser.setError("Debe ingresar un correo electronico");
            estado = false;
        }
        else if(!emailValidar.matches(emailPattern)){
            txtEmailUser.setError("Debe ingresar un correo electronico valido que termine en @gmail.com");
            estado = false;
        }
        else{
            txtEmailUser.setError(null);
            txtUserName.setError(null);
            estado = true;
        }
        return estado;
    }
}