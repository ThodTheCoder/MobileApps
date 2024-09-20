package com.mirea.elizarovnm.serviceapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    public static final String BROADCAST_ACTION = "com.example.mediaplayer.seekbarupdate";
    private MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private final Handler handler = new Handler();
    private Intent seekbarIntent;

    public PlayerService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        seekbarIntent = new Intent(BROADCAST_ACTION);

        mediaPlayer= MediaPlayer.create(this, R.raw.track);
        mediaPlayer.setLooping(false);
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Playing....")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("The Animals - House Of The Rising Sun"))
                .setContentTitle("Music Player");
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Student Елизаров Н. М. Notification", importance);

        channel.setDescription("MIREA Channel");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        startForeground(1, builder.build());
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stopForeground(true);
            }
        });
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            Log.d("run", "run: in runnable");
            if(mediaPlayer.isPlaying()) {
                seekbarIntent.putExtra("mediaposition", mediaPlayer.getCurrentPosition());
                seekbarIntent.putExtra("mediamaxposition", mediaPlayer.getDuration());
                sendBroadcast(seekbarIntent);
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onDestroy() {
        stopForeground(true);
        mediaPlayer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}