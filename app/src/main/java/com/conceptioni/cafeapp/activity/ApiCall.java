package com.conceptioni.cafeapp.activity;

import com.conceptioni.cafeapp.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constant.ApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
