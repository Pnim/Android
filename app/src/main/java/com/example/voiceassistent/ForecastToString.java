package com.example.voiceassistent;

import android.util.Log;

import androidx.core.util.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastToString {

    public static void getForecast (String city, final Consumer<String> callback){
        try {
            ForecastApi api = ForecastService.getApi();
            Call<Forecast> call = api.getCurrentWeather(city);
            call.enqueue(new Callback<Forecast>() {
                @Override
                public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                    Forecast result = response.body();
                    if(result!=null){
                        String answer = "сейчас где-то " + result.current.temperature + " " + SuperPuperRoboAI.normalizeEnding(result.current.temperature) + " и " + result.current.weather_descriptions.get(0);
                        callback.accept(answer);
                    }
                    else
                        callback.accept("Не могу узнать погоду");
                }

                @Override
                public void onFailure(Call<Forecast> call, Throwable t) {
                    Log.w("WEATHER", t.getMessage());
                }
            });
        }
        catch (Exception e) {
            callback.accept("Не знаю:(");
        }
    }
}