package com.felhr.serialportexample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.serialportexample.service.GcmCustomClass;

import java.security.PublicKey;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
{
    public static Context _context;

    public static String TextReceived = "";
    public static TextView MainActivityText;
    CustomClass customClass;
    GcmCustomClass gcmCustomClass;
    public static String lng,lat;

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
//        TextReceived = TextReceived ;
        if(TextReceived !=null)
        {
            MainActivityText.setText(TextReceived+ "\r");
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
        String Seven = "7";

        try {
            if (TextReceived.toString() == "7")
            {
                Log.i("Msg", "onResume: Han Bhai Agaya "+ TextReceived);
               GetGpsCordinates();
            }
            else {
                if (TextReceived !=null) {
                    SendDataToArduino();
                }
            }

        }
        catch (Exception ex)
        {
            Log.i("Msg", "onResume: Han Bhai Agayi exception "+ ex);
        }

    }

    @Override
    public void onPause()
    {
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

    public void GetGpsCordinates()
    {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());
        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
        Toast.makeText(MainActivity.this, "longitude: " + lng + "latitude:" + lat, Toast.LENGTH_SHORT).show();
//        latTextView.setText(String.valueOf(location.getLatitude()));
//        longTextView.setText(String.valueOf(location.getLongitude()));
    }

}


