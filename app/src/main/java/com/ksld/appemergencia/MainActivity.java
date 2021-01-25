package com.ksld.appemergencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST=202;

    EditText mensaje,num,cant;
    //TextView num;
    List<ContactResult> results = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mensaje = findViewById(R.id.InputMsj);
        num = findViewById(R.id.txtNum);
        cant = findViewById(R.id.cant);
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    }
    }).check();
    }

    public void contactos(View vista){
        new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }
*/
    public void btnsms(View vista){
        try {
            if(!results.isEmpty()){
                for (int j=0;j<results.size();j++){
                    for(int i=0;i<Integer.parseInt(cant.getText().toString());i++){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(results.get(i).getPhoneNumbers().get(0).getNumber(),null,mensaje.getText().toString(),null,null);
                        Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Algo falló", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                results=MultiContactPicker.obtainResult(data);
                StringBuilder names= new StringBuilder(results.get(0).getDisplayName());
                for (int j=0;j<results.size();j++){
                    if (j!=0){
                        names.append(", ").append(results.get(j).getDisplayName());
                    }
                }
                num.setText(names);
            }else if(resultCode==RESULT_CANCELED){
                System.out.println("se cerró");
            }
        }
    }

    /////////////////////

    public void WhatsAppMessage(View vista){

        if (num.getText().toString().isEmpty()){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,mensaje.getText().toString());
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }else{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_VIEW);
            String uri ="whatsapp://send?phone="+num.getText().toString()+"&text="+mensaje.getText().toString();//almacenará la ubicación o recurso q queremos compartir con whatsapp
            sendIntent.setData(Uri.parse(uri));
            startActivity(sendIntent);
        }
    }
    public void mensajeDirecto(View vista){
        PackageManager packageManager= getApplicationContext().getPackageManager();
        Intent i= new Intent(Intent.ACTION_VIEW);
        try{
            String url="https://api.whatsapp.com/send?phone="+num.getText().toString()+"&text="+ URLEncoder.encode(mensaje.getText().toString(),"UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if(i.resolveActivity(packageManager)!=null){
                getApplicationContext().startActivity(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleActionWHATSAPP(String message,String count,String[] mobile_number){
        try{
            PackageManager packageManager= getApplicationContext().getPackageManager();
            if(mobile_number.length!=0){
                for(int j=0;j<mobile_number.length; j++){
                    for(int i =0;i<Integer.parseInt(count.toString());i++){
                       String number =mobile_number[j];
                       String url="https://api.whatsapp.com?phone="+number+"&text="+URLEncoder.encode(message,"UTF-8");
                       Intent whatsappIntent= new Intent(Intent.ACTION_VIEW);
                       whatsappIntent.setPackage("com.whastapp");
                       whatsappIntent.setData(Uri.parse(url));
                       whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       if(whatsappIntent.resolveActivity(packageManager)!=null){
                           getApplicationContext().startActivity(whatsappIntent);
                           Thread.sleep(5000);
                           //sendBroadcastMessage("Result: "+number);
                       }
                    }
                }
            }
        }catch(Exception e){

        }
    }


}