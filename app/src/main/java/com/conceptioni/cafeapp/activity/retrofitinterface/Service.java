package com.conceptioni.cafeapp.activity.retrofitinterface;

import com.conceptioni.cafeapp.model.Menu;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Service {

    @POST("userapi/userLogin")
    Call<JsonObject> sendotp(@Header("Content-Type") String content,@Body JsonObject object);
    @POST("userapi/get_cafeMenu")
    Call<Menu> getMenuItem(@Header("Content-Type") String content, @Body JsonObject jsonObject);
    @POST("userapi/verifyOTP")
    Call<JsonObject> verifyotp(@Header("Content-Type") String content,@Body JsonObject object);
}
