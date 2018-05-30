package com.antonio.localizador;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Susana on 29/12/2015.
 */

//Activity asociada el fragmento que gestiona las preferencias de la app.
//El fragmento se añade de forma normal pero se debe adjuntar al contenido principal de la aplicación refiriéndonos al identificador general android.R.id.content.
//Lo que sigue es crear un nuevo fragmento que extienda de la clase PreferenceFragment. Lo llamaremos SettingsFragment y simplemente añadiremos el método addPreferencesFromResource() en onCreate()
//para la visualización. Debido a esto, no es necesario sobrescribir nada en el método onCreateView():
public class SettingsActivity extends AppCompatActivity {
    //Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.add(android.R.id.content, new SettingsFragment());
//        ft.commit();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();



    }

}
