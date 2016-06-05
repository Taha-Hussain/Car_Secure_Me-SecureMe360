package com.felhr.serialportexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.serialportexample.service.GcmCustomClass;

import java.security.PublicKey;


public class MainActivity extends AppCompatActivity
{
    public static Context _context;

    public static String TextReceived = "";
    public static TextView MainActivityText;
    CustomClass customClass;
    GcmCustomClass gcmCustomClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(MainActivity.this, "onCreate", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customClass = new CustomClass(MainActivity.this);
        gcmCustomClass = new GcmCustomClass(MainActivity.this);
        CustomClass.mHandler = new MyHandler(this);
        _context = this;
        MainActivityText = (TextView) findViewById(R.id.MainActivityText);

        gcmCustomClass.Register();

        Intent TextIntent = getIntent();     /* * Notifications from UsbService will be received here.  */
        TextReceived = (String) TextIntent.getSerializableExtra("Value");
        TextReceived = TextReceived + "\r";
        if(TextReceived !=null)
        {
            MainActivityText.setText(TextReceived);
            Toast.makeText(MainActivity.this, TextReceived, Toast.LENGTH_SHORT).show();
        }
    }


    public void ButtonClick(View v) {
        SendDataToArduino();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartUsbService();
        SendDataToArduino();
    }

    @Override
    public void onPause() {
        super.onPause();
        StopUsbService();
    }

    public void StartUsbService()
    {
        customClass.setFilters();  // Start listening notifications from UsbService
        customClass.startService(UsbService.class, customClass.usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    public void StopUsbService()
    {
        unregisterReceiver(customClass.mUsbReceiver);
        unbindService(customClass.usbConnection);
    }

    public void SendDataToArduino()
    {
        if (CustomClass.usbService != null) { // if UsbService was correctly binded, Send data
//          display.append(data);
            CustomClass.usbService.write(TextReceived.getBytes());

            Toast.makeText(MainActivity.this, "Data Send To Arduino", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Data Not Send To Arduino", Toast.LENGTH_SHORT).show();
        }
    }

}


