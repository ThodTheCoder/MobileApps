package com.mirea.elizarovnm.mireaproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.mirea.elizarovnm.mireaproject.WeatherResponse;

public interface WeatherApi {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("q") String city, @Query("appid") String apiKey, @Query("units") String units);
}