package com.mirea.elizarovnm.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void stringLenButtonClick(View view) {
        EditText editText = findViewById(R.id.editText1);
        String text = String.valueOf(editText.getText());
        Toast toast = Toast.makeText(getApplicationContext(),
                "СТУДЕНТ №12 ГРУППА БСБО-04-22 Количество символов - " + Integer.toString(text.length()),
                Toast.LENGTH_SHORT
        );
        toast.show();
    }
}