package com.felhr.serialportexample;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Taha on 30/05/2016.
 */

  /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
public class MyHandler extends Handler {

    private final WeakReference<MainActivity> mActivity;

    public MyHandler(MainActivity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UsbService.MESSAGE_FROM_SERIAL_PORT:
                String data = (String) msg.obj;
//                    mActivity.get().display.append(data);
               MainActivity.MainActivityText.setText( data +"Message Receive From Serial Port \n");
                break;
        }
    }
}
