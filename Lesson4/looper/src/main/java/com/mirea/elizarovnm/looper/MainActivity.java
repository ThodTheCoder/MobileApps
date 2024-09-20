package com.mirea.elizarovnm.looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.mirea.elizarovnm.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyLooper myLooper;

    public void onClick(View view){
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt("INT_KEY", Integer.parseInt(binding.editTextAge.getText().toString()));
        bundle.putString("JOB_KEY", binding.editTextJob.getText().toString());
        msg.setData(bundle);
        myLooper.mHandler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result"));

            }
        };
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.editTextMirea.setText("Мой номер по списку No12");

    }
}