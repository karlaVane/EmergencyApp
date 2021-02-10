package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nombre, email, pass, pass2;
    public AdminSQLite admin;
    public SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conectar();
        Cursor fila = bd.rawQuery("select * from usuario",null);

        if (fila.moveToFirst()){
            siguiente();
        }else {
            setContentView(R.layout.activity_main);
            nombre = findViewById(R.id.nombre);
            email = findViewById(R.id.email);
            pass = findViewById(R.id.pass);
            pass2 = findViewById(R.id.pass2);
        }
    }
    protected void onStart(){
        super.onStart();
        conectar();
        Cursor fila=bd.rawQuery("select * from usuario",null);
        //nos aseguramos de que existe al menos un registro
        if(fila.moveToFirst()){
            //setContentView(R.layout.activity_inicio_sesion);
            siguiente();
        }
    }

    public void conectar(){
        admin=new AdminSQLite(this,"DSA",null,1);
        bd=admin.getWritableDatabase();
    }

    public void validar(View vista){
        conectar();
        String et_nombre= nombre.getText().toString();
        String et_email = email.getText().toString();
        String et_pass = pass.getText().toString();
        String et_pass2 = pass2.getText().toString();

        if(et_nombre.equals("")|| et_email.equals("")|| et_pass.equals("")|| et_pass2.equals("")){
            aviso("Todos los campos son obligatorios");
        }else{
            String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
            if (et_pass.length()<8){
                aviso("La contraseña mínimo debe tener 8 caracteres");
            }else{
                if(et_pass.matches(pattern)){
                    if(et_pass.equals(et_pass2)){
                        ContentValues reg = new ContentValues();
                        reg.put("correo",et_email);
                        reg.put("nombre",et_nombre);
                        reg.put("pass",et_pass);
                        reg.put("pass2",et_pass2);
                        bd.insert("usuario",null,reg);
                        bd.close();
                        aviso("Registro exitoso");
                        siguiente();
                    }else{
                        aviso("Las contraseñas no coinciden");
                    }
                }else{
                    aviso("Contraseña debe contener Mayúscula, minúscula y caracteres especiales");
                }
            }
        }
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