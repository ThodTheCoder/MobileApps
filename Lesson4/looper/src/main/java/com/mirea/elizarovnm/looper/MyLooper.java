package com.mirea.elizarovnm.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread{
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper(Handler mainThreadHandler) {
        mainHandler =mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String data = msg.getData().getString("JOB_KEY");
                long delay = msg.getData().getInt("INT_KEY");
                Log.d("Delay", Long.toString(delay));
                try {
                    Thread.sleep(delay*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d("MyLooper get message: ", data);
                int count = data.length();
                Message message = new Message();
                Bundle bundle = new Bundle();
                String mes = "Работа - " + data + ". Кол-во лет - " + Long.toString(delay);
                bundle.putString("result", mes);
                message.setData(bundle);
                // Send the message back to main thread message queue use main thread message Handler.
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}
