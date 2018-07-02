package com.conceptioni.cafeapp.activity.retrofitinterface;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Service {

    @POST("userapi/userLogin")
    Call<JsonObject> sendotp(@Header("Content-Type") String content,@Body JsonObject object);

//    @POST("validate")
//    Call<JsonObject> verifyotp(@Header("Authorization") String authorization, @Header("Content-Type") String content, @Header("Accept-type") String Accept_type, @Body JsonObject object);
}
