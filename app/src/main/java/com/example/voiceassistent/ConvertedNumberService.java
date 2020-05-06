package com.example.voiceassistent;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConvertedNumberService {
    public static ConvertedNumberAPI getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://htmlweb.ru") // базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) // конвертер для преобразования JSON в объекты
                .build();
        return retrofit.create(ConvertedNumberAPI.class);
    }
}
