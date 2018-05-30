package com.antonio.localizador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener{


    public static final String KEY_USERNAME = "Username";
    public static final String KEY_PASSWORD = "Password";
    public static final String KEY_EMAIL = "Email";
    Button btnLogin;
    TextView txtNombre,txtPassword;
   /* RequestQueue request;
    JsonObjectRequest mJsonObjectRequest;*/
   static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*setTheme(R.style.AppTheme_Theme_Dialog);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        init();
      }

    private void init() {
         btnLogin=findViewById(R.id.btnLogin);
         txtNombre=findViewById(R.id.txtNombre);
         txtPassword=findViewById(R.id.txtPassword);
         btnLogin.setOnClickListener(this);
         //request= Volley.newRequestQueue(getApplicationContext());
         }


    private boolean validarEntrada() {

        final String username = txtNombre.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();


            if(username.isEmpty()||password.isEmpty()){

                //Toast.makeText(getApplicationContext(),"Para logarte en la aplicación debes rellenar los campos nombre y contraseña",Toast.LENGTH_LONG).show();

                Snackbar snack = Snackbar.make(btnLogin, R.string.avisologarseusuario, Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snack.getView();
                //group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    group.setBackground(ContextCompat.getDrawable(Login.this,R.drawable.colorear_button));
                }else{

                    group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }


                snack.show();

                return false;
            }
        return true;

    }





    @Override
    public void onClick(View v) {
        if(validarEntrada())userLogin(btnLogin);



    }


    private void userLogin(final Button btnLogin) {


        //http://petty.hol.es/validar_usuario.php

        //EL USUARIO SE LOGA PARA ENTRAR EN LA APLICACIÓN

        //Parámetros que se envían el Ws
        final String KEY_USERNAME_VALIDAR = "username";
        final String KEY_PASSWORD_VALIDAR = "password";

        String tag_json_obj_actual = "json_obj_req_actual";
        final String username = txtNombre.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Iniciando sesión... espera por favor");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Conexiones.LOGIN_URL_VOLLEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //pDialog.hide();
                        //int id;
                        String nombre="";
                        String email="";
                        String androidID="";
                        String telefono="";



                        try {
                            //DEVUELVE EL SIGUIENTE JSON: {"estado":1,"usuario":{"Id":"10","Username":"Pepe","Password":"dshdsjkhencryptada","Email":"email"}}

                            //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                            JSONObject respuestaJSON = null;   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                            respuestaJSON = new JSONObject(response.toString());
                            int resultJSON = Integer.parseInt(respuestaJSON.getString("estado"));   // estado es el nombre del campo en el JSON..Devuelve un entero


                            if (resultJSON==1){

                                //session.setLogin(true);
                                JSONObject usuarioJSON = respuestaJSON.getJSONObject("usuario");
                                id = usuarioJSON.getInt("Id");
                                nombre=usuarioJSON.getString("Username");
                                email=usuarioJSON.getString("Email");
                                androidID=usuarioJSON.getString("ID_Android");
                                telefono=usuarioJSON.getString("Telefono");

                                Intent intentServicio=new Intent(Login.this,ServicioLocalizaciones.class);
                                intentServicio.putExtra("ID", id);
                             /*   intentInicio.putExtra("USUARIO", nombre);
                                intentInicio.putExtra("EMAIL", email);
                                intentInicio.putExtra("ANDROID_ID", androidID);
                                intentInicio.putExtra("TELEFONO", telefono);*/

                                pDialog.dismiss();
                                startService(intentServicio);//abrimosServicio();

                                Intent intentPararServicio=new Intent(Login.this,PararServicio.class);
                                startActivity(intentPararServicio);

                                finish();

                            } else  if (resultJSON==2) {

                                pDialog.dismiss();
                                //El usuario no existe... Le informamos
                                //Toast.makeText(Login.this,R.string.usuarionoexist, Toast.LENGTH_LONG).show();

                                Snackbar snack = Snackbar.make(btnLogin, R.string.usuarionoexist, Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                //group.setBackground(RippleDrawable);
                                snack.show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        pDialog.dismiss();
                        Snackbar snack = Snackbar.make(btnLogin, error.toString(), Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) snack.getView();
                        group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snack.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME_VALIDAR,username);
                map.put(KEY_PASSWORD_VALIDAR,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




        // Añadir petición a la cola
        //AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj_actual);

        //limpiarDatos();
    }

    private void abrimosServicio() {

        Intent intentServicio=new Intent(Login.this,ServicioLocalizaciones.class);
        startActivity(intentServicio);

    }







}
