package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nombre, email, pass, pass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre= findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        pass2 = findViewById(R.id.pass2);
    }

    public void siguiente(){
        Intent activity_inicio= new Intent(this,menu.class);
        startActivity(activity_inicio);
    }

    public void aviso(String msj){
        Toast.makeText(this,msj, Toast.LENGTH_SHORT).show();
    }

    public void salir(View vista){
        finish();
    }
}