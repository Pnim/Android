package com.example.voiceassistent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConvertedNumberAPI {
    @GET("/json/convert/num2str")
    Call<ConvertedNumberModel> getConvertNumber(@Query("num") String number);
}
