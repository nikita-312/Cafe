package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CafeInfoActivity extends AppCompatActivity {
    TextviewRegular tvrCafeName, tvrtableNumber, tvrContinue;
    String tableNo="", cafename="", image;
    CircleImageView cafeLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_info);
        init();
        clicks();
    }

    private void clicks() {
        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"yes").apply();

        tvrContinue.setOnClickListener(v -> {
            startActivity(new Intent(CafeInfoActivity.this, MenuActivity.class));
            finish();
        });

    }

    private void init() {
        tvrCafeName = findViewById(R.id.tvrCafeName);
        tvrtableNumber = findViewById(R.id.tvrtableNumber);
        tvrContinue = findViewById(R.id.tvrContinue);
        cafeLogo = findViewById(R.id.cafeLogo);
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            tableNo = intent.getStringExtra("table_no");
        }
        cafeInfo();
    }

    public void cafeInfo() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.cafeInfo("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (object.optInt("success") == 1) {
                                JSONObject object1 = object.getJSONObject("data");
                                cafename = object1.getString("name");
                                image = object1.getString("image");
                            }
                            if (cafename != null)
                            tvrCafeName.setText(cafename);

                            if (tableNo != null)
                            tvrtableNumber.setText("Table number "+tableNo);
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.no_image)
                                    .error(R.drawable.no_image)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(Priority.HIGH);

                            Glide.with(CafeInfoActivity.this).load(image).apply(options).into(cafeLogo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast(R.string.Checkyournetwork);
            }
        });
    }
}
