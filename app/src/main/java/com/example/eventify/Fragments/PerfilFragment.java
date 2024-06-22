package com.example.eventify.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventify.Adaptador.eventosbyUserAdapter;
import com.example.eventify.Objets.Evento;
import com.example.eventify.R;
import com.example.eventify.activities.MainActivity;
import com.example.eventify.activities.loginActivity;
import com.example.eventify.services.eventoService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        TextView userName = root.findViewById(R.id.txtUserName);
        TextView userEmail = root.findViewById(R.id.txtUserEmail);
        LinearLayout eventosPublicados = root.findViewById(R.id.eventosPublicados);
        LinearLayout inscripciones = root.findViewById(R.id.inscripciones);
        LinearLayout closeSession = root.findViewById(R.id.cerrarSesion);
        ImageView imgEditar = root.findViewById(R.id.imgEditar);
        TextView txtEditar = root.findViewById(R.id.txtEditar);
        ImageView imgDesactivar = root.findViewById(R.id.imgDesactivar);
        TextView txtDesactivar = root.findViewById(R.id.txtDesactivar);

        userName.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());


        eventosPublicados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new EventsByUserFragment()).commit();
            }
        });
        inscripciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new RegisteredFragment()).commit();
            }
        });
        closeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cierre de Sesión");
                builder.setMessage("¿Su sesión será cancelada, esta seguro de realizar esta acción?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mAuth.signOut();
                            Intent intent = new Intent(getContext(), loginActivity.class);
                            startActivity(intent);
                            Toast.makeText(getContext(), "Sesión Cerrada", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }catch (Exception e){
                            Log.d("Error",e.getMessage());
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cierre de Sesión Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show().create();
            }
        });
        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Editando", Toast.LENGTH_SHORT).show();
            }
        });
        txtEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Editando", Toast.LENGTH_SHORT).show();
            }
        });
        imgDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesactivarCuenta();
            }
        });
        txtDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesactivarCuenta();
            }
        });
        return  root;
    }
    public void DesactivarCuenta(){
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Eventos Creados");
                    builder.setMessage("La cuenta No puede ser Desactivada porque cuenta con Eventos Creados");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show().create();
                }
                else{
                    List<Integer> listaId = new ArrayList<>();
                    String emailCurrenUser = currentUser.getEmail();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Suscripciones Activas");
                                builder.setMessage("La cuenta No puede ser Desactivada porque cuenta con Inscripciones a Eventos");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show().create();

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Desactivación De Cuenta");
                                builder.setMessage("Su cuenta será Desactivada, ¿Está seguro de realizar este paso?");
                                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showReauthenticateDialog();
                                    }
                                });
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(), "Desactivacón de Cuenta Cancelada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show().create();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Log.d("Error -> ", t.getMessage());
            }
        });
    }

    private void showReauthenticateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reingrese su contraseña para confirmar");

        // Layout para el diálogo de re-autenticación
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reauthenticate, (ViewGroup) getView(), false);
        final EditText inputPassword = viewInflated.findViewById(R.id.inputPassword);
        builder.setView(viewInflated);

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = inputPassword.getText().toString();
                reauthenticateAndDelete(password);
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void reauthenticateAndDelete(String password) {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                deleteAccount();
                            } else {
                                Log.d("Error", "Reautenticación fallida: " + task.getException().getMessage());
                                Toast.makeText(getContext(), "Reautenticación fallida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void deleteAccount() {
        if (currentUser != null) {
            currentUser.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Cuenta Eliminada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", "Error al eliminar la cuenta: " + e.getMessage());
                            Toast.makeText(getContext(), "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}