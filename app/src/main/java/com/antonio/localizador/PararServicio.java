package com.antonio.localizador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PararServicio extends AppCompatActivity implements View.OnClickListener{

    private  Button btnPararServicio;
    private TextView txtTiempo,txtMetros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parar_servicio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        Button btnPararServicio=findViewById(R.id.btnPararServicio);
        TextView txtTiempo=findViewById(R.id.txtTiempo);
        TextView txtMetros=findViewById(R.id.txtMetros);
        btnPararServicio.setOnClickListener(this);
        long CONVERTIR_A_MILISEGUNDOS = 60000;

        //RECOGEMOS LAS PREFERENCIAS...
        SharedPreferences preferencias= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //long TIEMPO_TEXTO =CONVERTIR_A_MILISEGUNDOS*(Long.parseLong(preferencias.getString("tiempo_GPS","300")));
        //long TIEMPO_TEXTO =(Long.parseLong(preferencias.getString("tiempo_GPS","300")));
        //CONVERTIR_A_MILISEGUNDOS * (Long.parseLong(preferencias.getString("tiempo_GPS","300")));
        String texto= String.valueOf(Long.parseLong(preferencias.getString("tiempo_GPS","300")));
        String texto2= String.valueOf(Integer.parseInt(preferencias.getString("distancia_GPS","100")));
        txtTiempo.setText("TIEMPO ACTUALIZACIÓN: "+texto+" minutos");
        txtMetros.setText("METROS HASTA LA ACTUALIZACIÓN: "+texto2+" metros");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnPararServicio:
                stopService(new Intent(PararServicio.this,
                        ServicioLocalizaciones.class));
                finish();
                break;


        }


    }
}
