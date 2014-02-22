package com.example.recorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.recorder.service.CallRecordService;
import com.example.recorder.utils.SpeechJsonParser;
import com.iflytek.speech.*;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final int UPDATE_TEXT_MSG = 1001;

    private SpeechRecognizer speechRecognizer;

    private EditText speechText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initLayout();
        initSpeech();
    }

    private void initLayout() {
        findViewById(R.id.recognise_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognise();
            }
        });
        findViewById(R.id.record_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        speechText = (EditText) findViewById(R.id.speech_text);
    }

    private void initSpeech() {
        speechRecognizer = new SpeechRecognizer(this, initListener);
        setRecognizerParams();

    }

    public void startRecording() {
        Intent intent = new Intent(getApplicationContext(), CallRecordService.class);
        getApplicationContext().startService(intent);
    }

    public void startRecognise(){
        speechRecognizer.startListening(recognizerListener);
    }

    private void setRecognizerParams(){
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS,"2000");
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        speechRecognizer.setParameter(SpeechConstant.PARAMS, "1, asr_audio_path=/sdcard/iflytek/wavaudio.pcm");
    }

    private RecognizerListener recognizerListener = new RecognizerListener.Stub() {

        @Override
        public void onVolumeChanged(int i) throws RemoteException {
            Log.i(TAG, "--> volume:"+i);
        }

        @Override
        public void onBeginOfSpeech() throws RemoteException {
            Log.i(TAG, "--> begin speech");
        }

        @Override
        public void onEndOfSpeech() throws RemoteException {
            Log.i(TAG, "--> end of speech");
            speechRecognizer.startListening(this);
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) throws RemoteException {
            String jsonResult = recognizerResult.getResultString();
            if (jsonResult!=null) {
                Message message = handler.obtainMessage(UPDATE_TEXT_MSG);
                message.obj = SpeechJsonParser.parseIatResult(recognizerResult.getResultString());
                message.sendToTarget();
            }else{
                Log.e(TAG, "--> result is null");
            }
        }

        @Override
        public void onError(int i) throws RemoteException {
            Toast.makeText(MainActivity.this, "errorCode:"+i, Toast.LENGTH_SHORT).show();
            speechRecognizer.startListening(this);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_TEXT_MSG) {
                String result = (String) msg.obj;
                speechText.append(result);
            }
        }
    };

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(ISpeechModule iSpeechModule, int i) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.cancel(recognizerListener);
        speechRecognizer.destory();
    }
}
