package com.felhr.serialportexample.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

public class GCMreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:0377778888"));
        try {

//            Intent mintent = new Intent(context, MainActivity.class);
//            intent.putExtra("message_received", msg);
//            context.startActivity(mintent);
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);

            Bundle bundle = intent.getExtras();
            String title = bundle.getString("title");
            String m = bundle.getString("m");

            Intent i = new Intent();
            i.setClassName("com.felhr.serialportexample", "com.felhr.serialportexample.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Value",m);
            context.startActivity(i);

//            Intent myIntent = new Intent(context, NewActivity.class);
//            context.startActivity(myIntent);
//            context.startActivity(new Intent(context,NewActivity.class));

        }
        catch (Exception ex)
        {
            Log.e(ex.toString(), "Error : ");
        }
//        intent.setClass(context, GCMservice.class);
//        context.startService(intent);
    }
}
