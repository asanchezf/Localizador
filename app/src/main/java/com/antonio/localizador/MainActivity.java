package com.antonio.localizador;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SOLICITUD_ACCESS_FINE_LOCATION = 1;//Para control de permisos en Android M o superior
    private Button btnRegistrarse, btnLogin;
    private static long back_pressed;//Contador para cerrar la app al pulsar dos veces seguidas el btón de cerrar. Se gestiona en el evento onBackPressed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        //manejador es el LocationManager
        LocationManager manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GpsNoHabilitado();//Avisa de que el GPS no está habilitado y da la posibiliddd de habilitarlo
        }
        permisosPorAplicacion();
    }

    private void init() {

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_blue_grey_50));
        setSupportActionBar(toolbar);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnRegistrarse.setOnClickListener(this);

    }


    private void permisosPorAplicacion() {

        //Gestionamos los permisos según la versión. A partir de Android M algnos permisos catalogados como peligrosos se gestionan en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //1-La aplicación tiene permisos....

                //utilizamosGps();
                btnLogin.setEnabled(true);

                //Toast.makeText(this, "1 Permiso Concedido", Toast.LENGTH_SHORT).show();

            } else {//No tiene permisos

                //explicarUsoPermiso();
                //solicitarPermiso();

                solicitarPermisoGPS();
            }

        } else {//No es Android M o superior. Ejecutamos de manera normal porque el permiso ya viene dado en el Manifiest

            //utilizamosGps();
            btnLogin.setEnabled(true);
        }


    }

    private void solicitarPermisoGPS() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            //4-Pequeña explicación de para qué queremos los permisos
            LinearLayout contenedor = findViewById(R.id.contenedor);
            Snackbar.make(contenedor, "Para que la app. funcione correctamente debe "
                    + " aceptarse el permiso para poder utilizar el GPS.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    SOLICITUD_ACCESS_FINE_LOCATION);
                        }
                    })
                    .show();
        } else {
            //5-Se muetra cuadro de diálogo predeterminado del sistema para que concedamos o denegemos el permiso
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    SOLICITUD_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //Si se preguntara por más permisos el resultado se gestionaría desde aquí.
        if (requestCode == SOLICITUD_ACCESS_FINE_LOCATION) {//6-Se ha concedido los permisos... procedemos a ejecutar el proceso
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //utilizamosGps();
                btnLogin.setEnabled(true);

            } else {//7-NO se han concedido los permisos. No se puede ejecutar el proceso. Se le informa de ello al usuario.

                /*Snackbar.make(vista, "Sin el permiso, no puedo realizar la" +
                        "acción", Snackbar.LENGTH_SHORT).show();*/
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                Toast.makeText(this, "No se ha concedido el permiso necesario para que la aplicación utilice el GPS del dispositivo.", Toast.LENGTH_SHORT).show();
                solicitarPermisosManualmente();
            }

        }
    }

    private void solicitarPermisosManualmente() {
        final CharSequence[] opciones = {"Si", "No"};
        final android.support.v7.app.AlertDialog.Builder alertOpciones = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados. La aplicación no funcionará correctamente.", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();

    }

    /*private void utilizamosGps() {
        muestraProveedores();
        *//*CRITERIOS PARA ELEGIR EL PROVEEDOR:SIN COSTE, QUE MUESTRE ALTITUD, Y QUE TENGA PRECISIÓN FINA. CON ESTOS
     * SERÁ ELEGIDO AUTOMÁTICAMENTE EL PROVEEDOR A UTILIZAR POR EL PROPIO TERMINAL*//*
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        Log.v(LOGCAT, "Mejor proveedor: " + proveedor + "\n");
        Log.v(LOGCAT, "Comenzamos con la última localización conocida:");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //NO HACEMOS NADA. A ESTE NIVEL EL PERMISO DE USO DE GPS YA ESTÁ APROVADO POR EL USUARIO.
            return;
        }
        Location localizacion = manejador.getLastKnownLocation(proveedor);
        muestraLocaliz(localizacion);
        muestradireccion(localizacion);
        traerMarcadoresNew();


    }*/

    private void GpsNoHabilitado() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS no está activado. La aplicación no funcionará correctamente. ¿Deseas activarlo ahora?")
                .setCancelable(false)
                .setPositiveButton(R.string.configurargps, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnRegistrarse:
             /*   Intent intentRegistro=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intentRegistro);*/

                break;

            case R.id.btnLogin:
                Intent intentLogin = new Intent(MainActivity.this, Login.class);
                startActivity(intentLogin);

                break;


        }


    }

    @Override
    public void onBackPressed() {
        /**
         * Cierra la app cuando se ha pulsado dos veces seguidas en un intervalo inferior a dos segundos.
         */

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            stopService(new Intent(MainActivity.this,
                    ServicioLocalizaciones.class));
        } else
            Toast.makeText(getBaseContext(), R.string.localizador_salir, Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();


    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intentSettings=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intentSettings);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
