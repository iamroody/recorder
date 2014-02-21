package com.example.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.recorder.service.CallRecordService;

public class MainActivity extends Activity {
    private static final String TAG = "RecordMyActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startRecording(View view) {
        Intent intent = new Intent(getApplicationContext(), CallRecordService.class);
        getApplicationContext().startService(intent);
    }
}
