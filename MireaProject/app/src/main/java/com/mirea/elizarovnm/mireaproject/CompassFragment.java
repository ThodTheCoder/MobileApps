package com.mirea.elizarovnm.mireaproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CompassFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magnetometer;
    private ImageView arrowImageView;
    private TextView directionTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        arrowImageView = view.findViewById(R.id.arrowImageView);
        directionTextView = view.findViewById(R.id.directionTextView);

        // Инициализация менеджера сенсоров и магнитометра
        sensorManager = (SensorManager) requireActivity().getSystemService(requireActivity().SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Регистрация слушателя сенсора магнитного поля
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Отмена регистрации слушателя сенсора магнитного поля
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Обработка изменений сенсора магнитного поля
        if (event.sensor == magnetometer) {
            float[] magneticValues = event.values;
            float azimuth = (float) Math.toDegrees(Math.atan2(magneticValues[1], magneticValues[0]));

            // Коррекция отрицательных углов
            if (azimuth < 0) {
                azimuth += 360;
            }

            arrowImageView.setRotation(-azimuth);

            // Определение направления на основе угла азимута
            String direction;
            if (azimuth >= 337.5 || azimuth < 22.5) {
                direction = "Север";
            } else if (azimuth >= 22.5 && azimuth < 67.5) {
                direction = "Северо-Восток";
            } else if (azimuth >= 67.5 && azimuth < 112.5) {
                direction = "Восток";
            } else if (azimuth >= 112.5 && azimuth < 157.5) {
                direction = "Юго-Восток";
            } else if (azimuth >= 157.5 && azimuth < 202.5) {
                direction = "Юг";
            } else if (azimuth >= 202.5 && azimuth < 247.5) {
                direction = "Юго-Запад";
            } else if (azimuth >= 247.5 && azimuth < 292.5) {
                direction = "Запад";
            } else {
                direction = "Северо-Запад";
            }

            // Обновление текста на экране с указанием направления
            directionTextView.setText("Направление: " + direction);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Обработка изменений точности сенсора (здесь не используется)
    }
}