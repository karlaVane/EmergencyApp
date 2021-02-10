package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class configuracion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
    }

    public void regresar_menu(View vista){
        finish();
    }
    public void salir(View vista){
        moveTaskToBack(true);
    }

}