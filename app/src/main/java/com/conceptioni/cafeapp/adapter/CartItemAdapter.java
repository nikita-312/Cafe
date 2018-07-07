package com.conceptioni.cafeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.ApiCall;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.model.CartModel;
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewBold;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MenuViewHolder> {

    private Context context;
    private List<CartModel> cartModelsarray;
    private List<Images> imagesList;

    public CartItemAdapter(List<CartModel> cartModelsarray) {
        this.cartModelsarray = cartModelsarray;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_cart, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.tvrCartName.setText(cartModelsarray.get(position).getItem_name());
        holder.tvrCartQty.setText(cartModelsarray.get(position).getQty());
        holder.tvbCartPrice.setText(cartModelsarray.get(position).getPrice());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        imagesList = cartModelsarray.get(position).getImages();
        if (imagesList.size() > 0)
            Glide.with(context).load(imagesList.get(0).getImages()).apply(options).into(holder.imageView1);


        holder.plusiv.setOnClickListener(v -> {
            int count = Integer.parseInt(cartModelsarray.get(position).getQty());
            int Quantity = count + 1;
            String finalQuantity = String.valueOf(Quantity);
            Log.d("+++++quant", "++++" + finalQuantity);
            CallQuantity(holder, finalQuantity, position, cartModelsarray.get(position).getItem_id());
        });

        holder.minusiv.setOnClickListener(v -> {
            if (!cartModelsarray.get(position).getQty().equalsIgnoreCase("0")) {
                int count = Integer.parseInt(cartModelsarray.get(position).getQty());
                int Quantity = count - 1;
                String finalQuantity = String.valueOf(Quantity);
                Log.d("+++++quant", "++++" + finalQuantity);
                CallQuantity(holder, finalQuantity, position, cartModelsarray.get(position).getItem_id());
            } else {
                new MakeToast("Quantity can not be less than 0");
            }

        });
    }

    @Override
    public int getItemCount() {
        return cartModelsarray.size();
    }

    private void CallQuantity(MenuViewHolder holder, String Quantity, int Position, String ItemId) {

        JsonObject object = new JsonObject();
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        object.addProperty("itemid", ItemId);
        object.addProperty("qty", Quantity);
        object.addProperty("note", "");

        Log.d("+++++quant123", "++++" + object.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.AddToCart("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject object1 = new JSONObject(String.valueOf(response.body()));
                        if (object1.optInt("success") == 1) {
                            cartModelsarray.get(Position).setQty(object1.optString("qty"));
                            holder.tvrCartQty.setText(object1.optString("qty"));
                        } else {
                            if (Quantity.equalsIgnoreCase("0")) {
                                new MakeToast(object1.optString("msg"));
                            } else {
                                int Quan = Integer.parseInt(Quantity);
                                holder.tvrCartQty.setText(Quan - 1);
                                new MakeToast(object1.optString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast("Please Try After Some Time");
            }
        });

    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemll;
        RoundedImageView imageView1;
        TextviewRegular tvrCartName, tvrCartQty;
        TextviewBold tvbCartPrice;
        ImageView plusiv, minusiv;

        MenuViewHolder(View itemView) {
            super(itemView);
            itemll = itemView.findViewById(R.id.itemll);
            imageView1 = itemView.findViewById(R.id.imageView1);
            tvrCartName = itemView.findViewById(R.id.tvrCartName);
            tvbCartPrice = itemView.findViewById(R.id.tvbCartPrice);
            tvrCartQty = itemView.findViewById(R.id.tvrCartQty);
            plusiv = itemView.findViewById(R.id.plusiv);
            minusiv = itemView.findViewById(R.id.minusiv);
        }
    }

}
