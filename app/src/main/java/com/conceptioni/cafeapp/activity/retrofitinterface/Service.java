package com.conceptioni.cafeapp.activity.retrofitinterface;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Service {

    @POST("userapi/userLogin")
    Call<JsonObject> sendotp(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/get_cafeMenu")
    Call<JsonObject> getMenuItem(@Header("Content-Type") String content, @Body JsonObject jsonObject);
    @POST("userapi/verifyOTP")
    Call<JsonObject> verifyotp(@Header("Content-Type") String content,@Body JsonObject object);
    @POST("userapi/view_cart")
    Call<JsonObject> viewCart(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/add_to_cart")
    Call<JsonObject> AddToCart(@Header("Content-Type") String content,@Body JsonObject object);
    @POST("userapi/remove_from_cart")
    Call<JsonObject> removeCart(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/give_cafe_review")
    Call<JsonObject> getReview(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/place_order")
    Call<JsonObject> placeOrder(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/live_order_list")
    Call<JsonObject> ViewLiveOrder(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/make_payment")
    Call<JsonObject> makePayment(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/review_current_order")
    Call<JsonObject> reviewCurrentOrder(@Header("Content-Type") String content, @Body JsonObject object);
    @POST("userapi/review_like_unlike")
    Call<JsonObject> reviewLikeUnlike(@Header("Content-Type") String content,@Body JsonObject object);
    @POST("userapi/re_order")
    Call<JsonObject> reorder(@Header("Content-Type") String content,@Body JsonObject object);
}
