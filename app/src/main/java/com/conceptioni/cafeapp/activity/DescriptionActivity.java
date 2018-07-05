package com.conceptioni.cafeapp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionActivity extends AppCompatActivity {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    int slider[] = {R.drawable.slider, R.drawable.slider, R.drawable.slider};
    String ItemData,ItemId,ImageData,Qty;
    List<Items> itemsArrayList = new ArrayList<>();
    List<Images> imagesArrayList = new ArrayList<>();
    TextviewRegular ItemPricetvr,Itemnametvr,Itemdesctvr,qtytvr;
    EditText noteset;
    ImageView plusiv,minusiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        init();
        allclick();
    }

    private void init() {
        viewPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        ItemPricetvr = findViewById(R.id.ItemPricetvr);
        Itemnametvr = findViewById(R.id.Itemnametvr);
        Itemdesctvr = findViewById(R.id.Itemdesctvr);
        noteset = findViewById(R.id.noteset);
        qtytvr = findViewById(R.id.qtytvr);
        plusiv = findViewById(R.id.plusiv);
        minusiv = findViewById(R.id.minusiv);


        if (getIntent().getExtras() != null){
            ItemId = getIntent().getStringExtra("ItemId");

            itemsArrayList.clear();
            imagesArrayList.clear();
            itemsArrayList = getArrayList();
            imagesArrayList = getimageArrayList();
            for (int i = 0; i <itemsArrayList.size() ; i++) {
                if (ItemId.equalsIgnoreCase(itemsArrayList.get(i).getItem_id())){
                    Itemnametvr.setText(itemsArrayList.get(i).getItem_name());
                    ItemPricetvr.setText(itemsArrayList.get(i).getPrice() + " Rs");
                    Itemdesctvr.setText(itemsArrayList.get(i).getDesc());
                    qtytvr.setText(itemsArrayList.get(i).getQty());
                    Qty = itemsArrayList.get(i).getQty();
//                    imagesArrayList = itemsArrayList.get(i).getImage();
                }
            }

        }
        SlidePageAdapter slidePageAdapter = new SlidePageAdapter();
        if (viewPager != null) {
            viewPager.setAdapter(slidePageAdapter);
        }

        CirclePageIndicator indicator =
                findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(4 * density);
        indicator.setStrokeWidth(13);
        indicator.setRadius(10);
        indicator.setStrokeColor(Color.WHITE);

        NUM_PAGES = slider.length;

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    private void allclick() {
        plusiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CallQuantity(Qty,);
            }
        });
    }

    public ArrayList<Items> getArrayList(){
        Gson gson = new Gson();
        ItemData = SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.ItemData, Constant.notAvailable);
        Type type = new TypeToken<ArrayList<Items>>() {}.getType();
        return gson.fromJson(ItemData, type);
    }

    public ArrayList<Images> getimageArrayList(){
        Gson gson = new Gson();
        ImageData = SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Imageata, Constant.notAvailable);
        Type type = new TypeToken<ArrayList<Images>>() {}.getType();
        return gson.fromJson(ImageData, type);
    }

    class SlidePageAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public int getCount() {
            return imagesArrayList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.row_item_slider, container, false);
            ImageView banneriv = view.findViewById(R.id.banneriv);
            Log.d("++++++size","++++"+imagesArrayList.size());
            Glide.with(DescriptionActivity.this).load(imagesArrayList.get(position).getImages()).into(banneriv);
//            banneriv.setImageResource(imagesArrayList.get(position).getImages());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void CallQuantity(String Quantity, int Position, String ItemId){

        JsonObject object = new JsonObject();
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        object.addProperty("itemid", ItemId);
        object.addProperty("qty", Quantity);

        Log.d("+++++quant123","++++"+object.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.AddToCart("application/json", object);


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null){
                    try {
                        JSONObject object1 = new JSONObject(String.valueOf(response.body()));
                        if (object1.optInt("success") == 1){
                            itemsArrayList.get(Position).setQty(object1.optString("qty"));
                            qtytvr.setText(object1.optString("qty"));
                            Log.d("+++++quant12","++++"+object1.optString("qty"));
                        }else {
                            if (Quantity.equalsIgnoreCase("0")){
                                new MakeToast(object1.optString("msg"));
                            }else {
                                int Quan = Integer.parseInt(Quantity);
                                qtytvr.setText(Quan - 1);
                                new MakeToast(object1.optString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                new MakeToast("Please Try After Some Time");
            }
        });

    }

}
