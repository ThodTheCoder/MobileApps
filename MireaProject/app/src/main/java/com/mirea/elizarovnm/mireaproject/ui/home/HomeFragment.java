package ru.mirea.asalinao.mireaproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.asalinao.mireaproject.RetrofitClient;
import ru.mirea.asalinao.mireaproject.WeatherApi;
import ru.mirea.asalinao.mireaproject.databinding.FragmentHomeBinding;
import ru.mirea.asalinao.mireaproject.WeatherResponse;

import ru.mirea.asalinao.mireaproject.databinding.FragmentHomeBinding;

public class    HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String API_KEY = "7db8fe0d4b387078000adec7394f7d46";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        getWeatherData("Moscow");

        return root;
    }


    private void getWeatherData(String city) {
        WeatherApi weatherApi = RetrofitClient.getRetrofitInstance().create(WeatherApi.class);
        Call<WeatherResponse> call = weatherApi.getWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        String weatherInfo = "City: " + weatherResponse.getName() + "\n" +
                                "Temperature: " + weatherResponse.getMain().getTemp() + "°C";
                        binding.textHome.setText(weatherInfo);
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                binding.textHome.setText("Failed to get weather data");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}