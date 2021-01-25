package com.ksld.appemergencia;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.net.URLEncoder;

public class MySMSservice extends AccessibilityService {

    private void handleActionWHATSAPP(String message, String count, String[] mobile_number){
        try{
            PackageManager packageManager= getApplicationContext().getPackageManager();
            if(mobile_number.length!=0){
                for(int j=0;j<mobile_number.length; j++){
                    for(int i =0;i<Integer.parseInt(count.toString());i++){
                        String number =mobile_number[j];
                        String url="https://api.whatsapp.com?phone="+number+"&text="+ URLEncoder.encode(message,"UTF-8");
                        Intent whatsappIntent= new Intent(Intent.ACTION_VIEW);
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.setData(Uri.parse(url));
                        whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(whatsappIntent.resolveActivity(packageManager)!=null){
                            getApplicationContext().startActivity(whatsappIntent);
                            Thread.sleep(5000);
                            Toast.makeText(this, "Correcto en whats", Toast.LENGTH_SHORT).show();
                            //sendBroadcastMessage("Result: "+number);
                        }else{
                            Toast.makeText(this, "No valeee!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }catch(Exception e){
            Toast.makeText(this, "Algo fue mal", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
}
