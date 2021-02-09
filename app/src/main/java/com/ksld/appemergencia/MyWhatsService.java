package com.ksld.appemergencia;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.wafflecopter.multicontactpicker.ContactResult;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyWhatsService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SMS = "com.ksld.appemergencia.action.FOO";
    private static final String ACTION_WHATSAPP = "com.ksld.appemergencia.action.BAZ";

    // TODO: Rename parameters
    private static final String MESSAGE = "com.ksld.appemergencia.extra.PARAM1";
    private static final String COUNT = "com.ksld.appemergencia.extra.PARAM2";
    private static final String MOBILE_NUMBER="com.ksld.appemergencia.extra.PARAM3";

    public MyWhatsService() {
        super("MyWhatsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSMS(Context context, String message, String count, List<ContactResult> mobile_numbers) {
        List<String> numbers= new ArrayList<String>();
        for (int i=0;i<mobile_numbers.size();i++){
            numbers.add(mobile_numbers.get(i).getPhoneNumbers().get(0).getNumber());
        }
        String[] numbersArray=numbers.toArray(new String[0]);

        Intent intent = new Intent(context, MyWhatsService.class);
        intent.setAction(ACTION_SMS);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER,numbersArray);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionWHATSAPP(Context context, String message, String count, List<ContactResult> mobile_numbers) {
        List<String> numbers= new ArrayList<String>();
        for (int i=0;i<mobile_numbers.size();i++){
            numbers.add(mobile_numbers.get(i).getPhoneNumbers().get(0).getNumber());
        }
        String[] numbersArray=numbers.toArray(new String[0]);

        Intent intent = new Intent(context, MyWhatsService.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER,numbersArray);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SMS.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String count= intent.getStringExtra(COUNT);
                final String[] mobile_number = intent.getStringArrayExtra(MOBILE_NUMBER);
                handleActionSMS(message, count,mobile_number);
            } else if (ACTION_WHATSAPP.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String count = intent.getStringExtra(COUNT);
                final String[] mobile_number = intent.getStringArrayExtra(MOBILE_NUMBER);
                handleActionWHATSAPP(message, count,mobile_number);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSMS(String message, String count, String[] mobile_number) {
        // TODO: Handle action Foo
        try {
            if(mobile_number.length!=0){
                for (int j=0;j<mobile_number.length;j++){
                    for(int i=0;i<Integer.parseInt(count.toString());i++){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(mobile_number[j],null,message,null,null);
                        sendBroadcastMessage("Result: "+i+" "+mobile_number[j]);
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Algo falló", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWHATSAPP(String message, String count, String[] mobile_number) {
        // TODO: Handle action WHATSAPP
        try{
            PackageManager packageManager= getApplicationContext().getPackageManager();
            if(mobile_number.length!=0){
                for(int j=0;j<mobile_number.length; j++){
                    for(int i =0;i<Integer.parseInt(count.toString());i++){
                        String number =mobile_number[j];
                        //number=number.replace("+","");
                        //System.out.println("NUMEROOO"+number);
                        String url="https://api.whatsapp.com/send?phone="+number+"&text="+ URLEncoder.encode(message,"UTF-8");
                        System.out.println(url);
                        Intent whatsappIntent= new Intent(Intent.ACTION_VIEW);
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.setData(Uri.parse(url));
                        System.out.println("hasta aqui vale");
                        whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(whatsappIntent.resolveActivity(packageManager)!=null){
                            System.out.println("ashhh");
                            getApplicationContext().startActivity(whatsappIntent);
                            System.out.println("ashhhx2ss");
                            Thread.sleep(5000);
                            sendBroadcastMessage("Result: "+number);
                        }else{
                            System.out.println("noa valeeeeee");
                            sendBroadcastMessage("Result: falló");
                        }
                    }
                }
            }
        }catch(Exception e){
            sendBroadcastMessage("Algo fue mal");
            System.out.println("grrr!!");
        }
    }

    private void sendBroadcastMessage(String message){
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result ",message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}