package com.example.eventify.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventify.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class InvitacionesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //----------------------------------------------------------
    // Atributos de la clase
    //----------------------------------------------------------

    //Relacionadas con los datos pedidos en el formulario
    EditText correo, name , asunto;
    Button btnEnviar;

    //variables que almacenan los datos que iran en el correo
    private String categoria;
    private String nombreEvento ;
    private String fecha ;
    private String img ;

    //Varables empleadas para el funcionamiento del envio del correo
    private static String emailFrom = "eventifyassistant@gmail.com"; //almacena el correo desde donde se envia el correo
    private static String passwordEmail = "tvrrqcsrzbvicuoj";
    private  String emailTo; //Almacena el correo al que se envia la inviatcion
    private  String subject;
    private  String nameUser;

    //-------------------------------------------------------------------


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvitacionesFragment newInstance(String param1, String param2) {
        InvitacionesFragment fragment = new InvitacionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InvitacionesFragment() {
        // Required empty public constructor
    }

    //Definimos  un metodo static para crear una nueva instancia de InvitacionesFragment y recibir los datos desde el adapater correspondiente
    public static InvitacionesFragment newInstance(String dataCategoria, String dataName, String dataDate, String dataIMG) {
        InvitacionesFragment invitacionesFragment = new InvitacionesFragment();
        //Definimos un objeto Bundle para almacenar argumentos
        Bundle args = new Bundle();
        //Agregamos los datos  utilizando una clave  con un valor asociado
        args.putString("categoria", dataCategoria);
        args.putString("nombreEvento", dataName);
        args.putString("fecha", dataDate);
        args.putString("img", dataIMG);
        invitacionesFragment.setArguments(args);
        return invitacionesFragment;
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
        View root = inflater.inflate(R.layout.fragment_invitaciones, container, false);
        //------------------------------------------------------------------------------------------
        //Capturamos los id
        //------------------------------------------------------------------------------------------
        correo = root.findViewById(R.id.txtEmail);
        name = root.findViewById(R.id.txtNameInvitado);
        asunto = root.findViewById(R.id.txtAsunto);
        btnEnviar = root.findViewById(R.id.btnEnviarInvitacion);
        //------------------------------------------------------------------------------------------

        //evento encaragdo de mandar el correo
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correo.getText().toString().isEmpty() && name.getText().toString().isEmpty() && asunto.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Debe Ingresar los datos requeridos", Toast.LENGTH_SHORT).show();
                }else{
                    //Recuperamos los datos
                    Bundle args = getArguments();
                    //Verificamos que no existan valores nulos
                    if (args != null) {
                        categoria = args.getString("categoria");
                        nombreEvento = args.getString("nombreEvento");
                        fecha = args.getString("fecha");
                        img = args.getString("img");
                        System.out.println("Datos Recibidos: "+ categoria +" | " + nombreEvento +" | " + fecha +" | " + img);
                    }
                    else{
                        System.out.println("No hay datos disponibles");
                    }
                    emailTo = correo.getText().toString();
                    nameUser = name.getText().toString();
                    subject = asunto.getText().toString();
                    //Validamos que ninguna de las avraibles de contenido del correo esten vacias
                    if(emailTo.isEmpty() || nameUser.isEmpty() || subject.isEmpty()){
                        Toast.makeText(getContext(), "Debe Ingresar los datos requeridos", Toast.LENGTH_SHORT).show();
                    }
                    else if (!validarEmail(emailTo)) {
                        // Validamos el formato del correo
                        Toast.makeText(getContext(), "Formato de correo no válido", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //llamamos a las funciones empleada
                        sendEmail();
                        Toast.makeText(getContext(), "Invitacion enviada", Toast.LENGTH_SHORT).show();
                        //Limpiamos los EditText
                        correo.setText("");
                        name.setText("");
                        asunto.setText("");
                    }
                }
            }
        });

        return root;
    }

    //----------------------------------------------------------------------------------------------
    //Metodos
    //----------------------------------------------------------------------------------------------
    //Metodo encargado de validar el correo
    private boolean validarEmail(String email) {
        // Expresión regular para validar el formato del correo
        String regex = "^[\\w\\.-]+@([\\w-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && email.contains("gmail.com") || email.contains("ues.edu.sv");
    }

    //Metodo encargado de leer el archivo html
    private String readHtml(String file){
        try {
            InputStream inputStream = getActivity().getAssets().open(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Reemplaza los marcadores de posición con los valores reales
                line = line.replace("{{CATEGORIA}}", categoria);
                line = line.replace("{{NOMBRE_EVENTO}}", nombreEvento);
                line = line.replace("{{FECHA}}", fecha);
                line = line.replace("{{IMG}}", img);

                htmlContent.append(line);
            }
            reader.close();
            return htmlContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Manejo de error: devuelve una cadena vacía en caso de excepción
        }
    }

    //Metodo encaegado de las configuraciones necesarias para mandar el correo
    private void sendEmail(){

        //Creamos un hilo (Thread) para realizar la operación de envío de correo electrónico.
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //manejamos los posibles errores
                try {
                    //Creamos un objeto properties para almacenar las configuraciones
                    Properties properties = new Properties();
                    //Configuramos las propiedades necesarias para la conexión SMTP con el servidor de Gmail
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.port", "587");
                    properties.put("mail.smtp.user", emailFrom);
                    properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
                    properties.put("mail.smtp.auth", "true");

                    //Creamos la session utilizando las propiedades antes configuradas
                    Session session = Session.getDefaultInstance(properties);
                    //Creamos un objeto MimeMessage para representar el correo electrónico.
                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.setFrom(new InternetAddress(emailFrom));
                    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
                    mimeMessage.setSubject(subject);
                    // Lee el contenido del archivo HTML
                    String htmlContent = readHtml("invitations.html");
                    // Reemplazamos los marcadores de posición con los contenidos reales
                    htmlContent = htmlContent.replace("{{CATEGORIA}}", categoria);
                    htmlContent = htmlContent.replace("{{NOMBRE_EVENTO}}", nombreEvento);
                    htmlContent = htmlContent.replace("{{FECHA}}", fecha);
                    htmlContent = htmlContent.replace("{{IMG}}", img);
                    htmlContent = htmlContent.replace("{{NAME}}",nameUser);

                    // Establece el contenido del correo electrónico como HTML
                    mimeMessage.setContent(htmlContent, "text/html; charset=utf-8");

                    //creamos un objeto Transport para manejar la conexión con el servidor SMTP
                    Transport transport = session.getTransport("smtp");
                    //Establece la conexión con el servidor SMTP utilizando la dirección de correo electrónico del remitente  y la contraseña asociada.
                    transport.connect(emailFrom, passwordEmail);
                    //Envíamos el mensaje (mimeMessage) al destinatario especificado en el campo “TO” del correo electrónico.
                    transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
                    //Cerramos la conexión con el servidor SMTP.
                    transport.close();

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
}