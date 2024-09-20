package com.mirea.elizarovnm.mireaproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.Random;
import java.io.File;
import java.util.concurrent.TimeUnit;
import androidx.work.Data;

import javax.xml.transform.Result;


public class WeatherTips extends Worker {
    private static final String[] PROGRAMMER_TIPS = {
            "Всегда проверяйте прогноз погоды перед выходом из дома",
            "Имейте под рукой несколько слоев одежды для различных погодных условий",
            "Избегайте длительного пребывания под прямыми солнечными лучами во время жары",
            "Носите солнцезащитные очки и надежный крем от солнца в жаркую погоду",
            "По возможности используйте зонты или дождевики во время дождя",
            "Держите при себе бутылку воды в жаркую погоду, чтобы избежать обезвоживания",
            "Постарайтесь сохранить ноги в тепле и сухости во время холодной и дождливой погоды",
            "Будьте особенно осторожны на дорогах во время сильного ветра или гололеда",
            "При планировании активного отдыха учитывайте прогноз погоды, чтобы избежать неприятных сюрпризов",
            "Обращайте внимание на предупреждения и уведомления о погодных условиях от местных властей"
    };

    public WeatherTips(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }


    @NonNull
    @Override
    public Result doWork() {
//        int tipIndex = new Random().nextInt(PROGRAMMER_TIPS.length);
//        String dailyTip = "Совет дня: " + PROGRAMMER_TIPS[tipIndex];
//
//        getApplicationContext().getSharedPreferences("WeatherTips", Context.MODE_PRIVATE)
//                .edit()
//                .putString("DailyTip", dailyTip)
//                .apply();

        Data outputData = null;

        try {
            TimeUnit.SECONDS.sleep(5);

            int tipIndex = new Random().nextInt(PROGRAMMER_TIPS.length);
            String dailyTip = "Совет дня: " + PROGRAMMER_TIPS[tipIndex];

            outputData = new Data.Builder()
                    .putString("Tip", dailyTip)
                    .build();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.success(outputData);
    }
}