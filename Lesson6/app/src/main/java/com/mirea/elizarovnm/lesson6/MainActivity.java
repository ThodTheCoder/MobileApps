package com.mirea.elizarovnm.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mirea.elizarovnm.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static private final String KEY_GROUP_NUMBER = "group_number";
    static private final String KEY_STUDENT_NUMBER = "student_number";
    static private final String KEY_FILM_TITLE = "film_title";
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences settings = getSharedPreferences("task_2_1.settings", Context.MODE_PRIVATE);
        binding.groupNumberInput.setText(String.valueOf(settings.getInt(KEY_GROUP_NUMBER, 0)));
        binding.studentNumberInput.setText(String.valueOf(settings.getInt(KEY_STUDENT_NUMBER, 0)));
        binding.filmTitleInput.setText(settings.getString(KEY_FILM_TITLE, ""));
    }
    public void OnSaveDataButtonClicked(View v) {
        final SharedPreferences settings = getSharedPreferences("task_2_1.settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        try {
            final int group_number = Integer.parseInt(binding.groupNumberInput.getText().toString());
            final int student_number = Integer.parseInt(binding.studentNumberInput.getText().toString());
            final String film_title = binding.filmTitleInput.getText().toString();
            editor.putInt(KEY_GROUP_NUMBER, group_number);
            editor.putInt(KEY_STUDENT_NUMBER, student_number);
            editor.putString(KEY_FILM_TITLE, film_title);
            editor.apply();
            Toast.makeText(this, "Success saving", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, String.format("Error: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
}