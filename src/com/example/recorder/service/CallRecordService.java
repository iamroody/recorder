package com.example.recorder.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.example.recorder.listener.PhoneListener;

public class CallRecordService extends Service {

    private static final String TAG = "CallRecordService";

    public class CallRecordBinder extends Binder {
        CallRecordService getService () {
            return CallRecordService.this;
        }

    }

    private final IBinder callRecordBinder = new CallRecordBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return callRecordBinder;
    }


    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "recording service is created", Toast.LENGTH_LONG).show();
        TelephonyManager telephoneService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephoneService.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "recording service is destroyed", Toast.LENGTH_LONG).show();
    }


}
