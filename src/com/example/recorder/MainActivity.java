package com.example.recorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.recorder.adapter.ListMessageAdapter;
import com.example.recorder.model.TeleMessage;
import com.example.recorder.service.CallRecordService;
import com.example.recorder.utils.Constants;
import com.example.recorder.utils.SpeechJsonParser;
import com.iflytek.speech.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final int UPDATE_TEXT_MSG = 1001;
    private static final int RESTART_SPEECH_RECOGNIZER = 1003;
    private static final int DELAY_MILLIONS = 4000;
    private boolean speechRunning = false;

    private SpeechRecognizer speechRecognizer;

    private ListMessageAdapter messageAdapter;

    private TextView speechText;
    private ListView speechList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        messageAdapter = new ListMessageAdapter(this, getDemoMessages());
        initLayout();
        initSpeech();
    }

    private void initLayout() {
        findViewById(R.id.recognise_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startRecognise();
                insertDemoMessage();
                speechList.setSelection(messageAdapter.getCount() - 1);
            }
        });
        findViewById(R.id.record_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        speechText = (TextView) findViewById(R.id.speech_text);
        speechList = (ListView) findViewById(R.id.speech_list);
        speechList.setAdapter(messageAdapter);
    }

    private List<TeleMessage> getDemoMessages() {
        List<TeleMessage> demoMessages = new ArrayList<TeleMessage>();
        demoMessages.add(new TeleMessage("你好你好，你再哪里呢。我要去接你哈哈哈哈。你一定要等我", Constants.MESSAGE_SENDER_ME));
        demoMessages.add(new TeleMessage("你好你好，你再哪里呢。我要去接你哈哈哈哈。你一定要等我", "小明"));
        demoMessages.add(new TeleMessage("你好你好，你再哪里呢。我要去接你哈哈哈哈。你一定要等我", "小黑"));
        demoMessages.add(new TeleMessage("你好你好，你再哪里呢。我要去接你哈哈哈哈。你一定要等我", Constants.MESSAGE_SENDER_ME));
        demoMessages.add(new TeleMessage("你好你好，你再哪里呢。我要去接你哈哈哈哈。你一定要等我", "小华"));
        return demoMessages;
    }

    private void insertDemoMessage() {
        messageAdapter.insertMessage(new TeleMessage("你来了一条新的消息，这是被按钮触发的，加油！心声！", Constants.MESSAGE_SENDER_ME));
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
                speechText.append(result);
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
