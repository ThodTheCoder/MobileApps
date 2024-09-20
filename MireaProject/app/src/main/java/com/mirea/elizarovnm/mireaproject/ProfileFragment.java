package com.mirea.elizarovnm.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText nameEditText, ageEditText;
    private Button saveButton;
    private TextView nameTextView, ageTextView;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "profile_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        saveButton = view.findViewById(R.id.saveButton);
        nameTextView = view.findViewById(R.id.nameTextView);
        ageTextView = view.findViewById(R.id.ageTextView);

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        loadProfileData(); // Загрузка сохраненных данных

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        return view;
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString(KEY_NAME, "");
        int age = sharedPreferences.getInt(KEY_AGE, 0);

        nameTextView.setText("Сохраненное имя: " + name);
        ageTextView.setText("Сохраненный возраст: " + age);
    }

    private void saveProfileData() {
        String name = nameEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putInt(KEY_AGE, age);
        editor.apply();

        // Обновление текста в TextView после сохранения данных
        nameTextView.setText("Сохраненное имя: " + name);
        ageTextView.setText("Сохраненный возраст: " + age);
    }
}