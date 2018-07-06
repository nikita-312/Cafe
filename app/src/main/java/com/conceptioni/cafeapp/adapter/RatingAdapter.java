package com.conceptioni.cafeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.ApiCall;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.model.CurrentOrderModel;
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.Holder> {
    private Context context;
     List<Images> imagesarray;
    List<CurrentOrderModel> currentOrderModelsArray;

    public RatingAdapter(List<Images> imagesarray,List<CurrentOrderModel> currentOrderModelsArray ){
        this.imagesarray = imagesarray;
        this.currentOrderModelsArray = currentOrderModelsArray;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_rating,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvrItemName.setText(currentOrderModelsArray.get(position).getItem_name());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        for (int i = 0; i <imagesarray.size() ; i++) {
            Glide.with(context).load(imagesarray.get(0).getImages()).apply(options).into(holder.imageView1);
        }
        if (currentOrderModelsArray.get(position).getLike().equalsIgnoreCase("0")){
            holder.ivUp.setBackgroundResource(R.drawable.unfill_like);
        }else{
            holder.ivUp.setBackgroundResource(R.drawable.fill_like);
        }
        holder.ivUp.setOnClickListener(v -> {
            if (currentOrderModelsArray.get(position).getLike().equalsIgnoreCase("0")){
                holder.ivUp.setBackgroundResource(R.drawable.fill_like);
                currentOrderModelsArray.get(position).setLike("1");
            }else if (currentOrderModelsArray.get(position).getLike().equalsIgnoreCase("1")){
                holder.ivUp.setBackgroundResource(R.drawable.unfill_like);
            }
            ReviewLikeUnlike(position);
        });
        if (currentOrderModelsArray.get(position).getUnlike().equalsIgnoreCase("0")){
            holder.ivDown.setBackgroundResource(R.drawable.unfill_like);
        }else{
            holder.ivDown.setBackgroundResource(R.drawable.fill_like);
        }
        holder.ivDown.setOnClickListener(v -> {
            if (currentOrderModelsArray.get(position).getLike().equalsIgnoreCase("0")){
                holder.ivDown.setBackgroundResource(R.drawable.fill_unlike);
                currentOrderModelsArray.get(position).setLike("1");
            }else if (currentOrderModelsArray.get(position).getLike().equalsIgnoreCase("1")){
                holder.ivDown.setBackgroundResource(R.drawable.unfill_unlike);
            }
            ReviewLikeUnlike(position);
        });
    }

    @Override
    public int getItemCount() {
        return currentOrderModelsArray.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        RoundedImageView imageView1;
        TextviewRegular tvrItemName;
        ImageView ivUp,ivDown;
        public Holder(View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            tvrItemName = itemView.findViewById(R.id.tvrItemName);
            ivUp = itemView.findViewById(R.id.ivUp);
            ivDown = itemView.findViewById(R.id.ivDown);
        }
    }
    public void ReviewLikeUnlike(final int pos) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("orderid",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.orderid,Constant.notAvailable));
        jsonObject.addProperty("auth_token",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token,Constant.notAvailable));
        jsonObject.addProperty("itemid",currentOrderModelsArray.get(pos).getItem_id());
        jsonObject.addProperty("like",currentOrderModelsArray.get(pos).getLike());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.reviewCurrentOrder("auth_token",jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null){
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.optInt("success") == 1){
                                currentOrderModelsArray.get(pos).setLike(object.optString("like"));
                                currentOrderModelsArray.get(pos).setUnlike(object.optString("unlike"));
                               new MakeToast(object.optString("msg"));
                            }else
                                new MakeToast(object.optString("msg"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                new MakeToast("Error while getting data");
            }
        });
    }

}
