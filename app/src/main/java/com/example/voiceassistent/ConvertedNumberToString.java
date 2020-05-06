package com.example.voiceassistent;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertedNumberToString {

    public static void getConvertNumber(String number, final Consumer<String> callback) {
        ConvertedNumberAPI api = ConvertedNumberService.getApi();
        Call<ConvertedNumberModel> call = api.getConvertNumber(number);
        call.enqueue(new Callback<ConvertedNumberModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ConvertedNumberModel> call, Response<ConvertedNumberModel> response) {
                ConvertedNumberModel result = response.body();
                if (result!=null){
                    String answer = result.convertedStr;
                    callback.accept(answer);
                }
                else{
                    callback.accept("Не получается перевести число в строку:(");
                }

            }
            @Override
            public void onFailure(Call<ConvertedNumberModel> call, Throwable t) {
                Log.w("CONVERTNUMBER", t.getMessage());
            }
        });
    }
}