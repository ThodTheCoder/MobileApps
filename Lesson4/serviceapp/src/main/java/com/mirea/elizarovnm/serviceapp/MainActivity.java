package com.mirea.elizarovnm.serviceapp;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.mirea.elizarovnm.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private int PermissionCode = 200;
    private BroadcastReceiver broadcastReceiver;
    private SeekBar seekBar;

    public void onPauseClick(View view){
        stopService(new Intent(MainActivity.this, PlayerService.class));
    }

    public void onPlayClick(View view){
        Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        seekBar = binding.seekBar;

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI(intent);
            }
        };


        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения получены");
        } else {
            Log.d(MainActivity.class.getSimpleName().toString(), "Нет разрешений!");

            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE}, PermissionCode);
        }
    }

    private void updateUI(Intent serviceIntent) {
        int maxPosition = serviceIntent.getIntExtra("mediamaxposition", 0);
        int currentPosition = serviceIntent.getIntExtra("mediaposition", 0);
        seekBar.setMax(maxPosition);
        seekBar.setProgress(currentPosition);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(PlayerService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}