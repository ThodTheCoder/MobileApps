package com.mirea.elizarovnm.favouritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView ageView = findViewById(R.id.textViewBook1);
            String university = extras.getString(MainActivity.KEY);
            ageView.setText(String.format("Моя любимая книга: %s", university));
        }
    }

    public void onClickSend(View view) {
        TextView text = findViewById(R.id.editTextText);
        String s = text.getText().toString();
        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, s);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
