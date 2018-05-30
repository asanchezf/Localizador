package com.antonio.localizador;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Susana on 29/12/2015.
 */

//Referencias en HerProg y en VainiyaSoft
//Fragemento utilizado para gestionar las sharepreferences. Se debe utilizar un fragment en vez de una actividad.
//La creación de un fragmento de preferencias requiere que tengamos una actividad en la cual se añadirá su contenido.
// Se debe crear una actividad de preferencias que extienda de ActionBarActivity o la clase Activity.
public class SettingsFragment extends PreferenceFragment {
    //private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);//settings es el archivo que contiene las preferencias....

//Añadimos la toolbar
        //toolbar = (Toolbar) view.findViewById(R.id.toolbar);


    }
}
