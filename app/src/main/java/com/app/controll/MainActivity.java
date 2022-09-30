package com.app.controll;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.app.crash_monitor.MonitorUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_method).setOnClickListener(v -> {
            MonitorUtil.init(this);
        });
    }
}