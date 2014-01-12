package com.example.recorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallRecordService extends Service {
    private static final String TAG = "RecordCallRecordService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "recording service is created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "recording service is destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(getApplicationContext(), "recording service is started", Toast.LENGTH_LONG).show();
        TelephonyManager telephoneService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephoneService.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }
}
