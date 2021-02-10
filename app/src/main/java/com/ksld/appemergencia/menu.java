package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void ac_funcionalidad(View vista){
        Intent activity_inicio= new Intent(this,funcionalidad.class);
        startActivity(activity_inicio);
    }
    public void ac_configuracion(View vista){
        Intent activity_inicio= new Intent(this,configuracion.class);
        startActivity(activity_inicio);
    }

    public void salir(View vista){
        moveTaskToBack(true);
    }


}