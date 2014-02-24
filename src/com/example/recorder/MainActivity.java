package com.example.recorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.recorder.adapter.ListMessageAdapter;
import com.example.recorder.model.TeleMessage;
import com.example.recorder.service.CallRecordService;
import com.example.recorder.utils.Constants;
import com.example.recorder.utils.SpeechJsonParser;
import com.iflytek.speech.*;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final int UPDATE_TEXT_MSG = 1001;
    private static final int RESTART_SPEECH_RECOGNIZER = 1003;
    private static final int DELAY_MILLIONS = 4000;
    private boolean speechRunning = false;

    private SpeechRecognizer speechRecognizer;

    private ListMessageAdapter messageAdapter;

    private EditText msgEdit;
    private ListView speechList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initLayout();
        initSpeech();
    }

    private void initLayout() {
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        speechList = (ListView) findViewById(R.id.speech_list);
        messageAdapter = new ListMessageAdapter(this, null);
        speechList.setAdapter(messageAdapter);
        findViewById(R.id.voice_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognise();
            }
        });
        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgEdit.getText().toString().trim();
                updateMessage(msg, Constants.MESSAGE_SENDER_ME);
                msgEdit.setText("");
            }
        });

    }

    private void updateMessage(String message, String sender) {
        if(message!=null && !message.isEmpty()) {
            messageAdapter.insertMessage(new TeleMessage(message, sender));
            speechList.setSelection(messageAdapter.getCount() - 1);
        }
    }

    private void initSpeech() {
        SpeechUtility.getUtility(MainActivity.this).setAppid("53060b5b");
        speechRecognizer = new SpeechRecognizer(this, initListener);
        setRecognizerParams();

    }

    public void startRecording() {
        Intent intent = new Intent(getApplicationContext(), CallRecordService.class);
        getApplicationContext().startService(intent);
    }

    public void startRecognise(){
        speechRecognizer.startListening(recognizerListener);
        speechRunning = true;
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
            speechRunning = false;
            handler.sendEmptyMessageDelayed(RESTART_SPEECH_RECOGNIZER, DELAY_MILLIONS);
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) throws RemoteException {
            Log.i(TAG, "--> result of speech");
            String jsonResult = recognizerResult.getResultString();
            if (jsonResult!=null) {
                Message message = handler.obtainMessage(UPDATE_TEXT_MSG);
                message.obj = SpeechJsonParser.parseIatResult(recognizerResult.getResultString());
                message.sendToTarget();
            }
            startRecognise();
        }

        @Override
        public void onError(int i) throws RemoteException {
            Toast.makeText(MainActivity.this, "errorCode:"+i, Toast.LENGTH_SHORT).show();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_TEXT_MSG) {
                String result = (String) msg.obj;
                updateMessage(result, "somebody");
            }else if (msg.what == RESTART_SPEECH_RECOGNIZER && !speechRunning) {
                startRecognise();
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
