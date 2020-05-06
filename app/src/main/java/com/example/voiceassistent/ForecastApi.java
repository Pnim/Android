package com.example.voiceassistent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastApi {
    @GET("/current?access_key=0419285119ec13ae814b684b5a365ab6")
    Call<Forecast> getCurrentWeather(@Query("query") String city);
}
