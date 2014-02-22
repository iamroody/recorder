package com.example.recorder;

import android.app.Activity;
import android.os.Bundle;

/**
 * User: gongming
 * Date: 2/22/14
 * Time: 10:47 上午
 * Email:gongmingqm10@foxmail.com
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
