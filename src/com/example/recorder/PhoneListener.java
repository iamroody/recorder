package com.example.recorder;

import android.media.MediaRecorder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class PhoneListener extends PhoneStateListener {

    private static final String TAG = "RecordPhoneListener";
    private boolean isCall;
    private MediaRecorder mediaRecorder = new MediaRecorder();;
    private File audioFile;

    public PhoneListener() {
        isCall = false;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        Log.e(TAG, "incoming number is " + incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_OFFHOOK:
                isCall = true;
                try {
                    record();
                } catch (IOException e) {
                    e.printStackTrace();
                    mediaRecorder.stop();
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (isCall) {
                    Log.e(TAG, "should stop media recorder when hang off the phone");
                    mediaRecorder.stop();

                    isCall = false;
                }
                break;
        }
    }

    private void record() throws IOException {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        audioFile = File.createTempFile("record_", ".amr");
        Log.e(TAG, "the recording file path is " + audioFile.getAbsolutePath());
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        mediaRecorder.prepare();
        mediaRecorder.start();
    }
}
