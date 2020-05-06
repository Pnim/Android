package com.example.voiceassistent;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastService {
    public static ForecastApi getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.weatherstack.com") // базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) // конвертер для преобразования JSON в объекты
                .build();
        return retrofit.create(ForecastApi.class);
    }
}