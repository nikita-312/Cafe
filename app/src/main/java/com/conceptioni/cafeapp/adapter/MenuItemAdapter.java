package com.conceptioni.cafeapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.database.DBOpenHelper;
import com.conceptioni.cafeapp.model.CartData;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewBold;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder> {

    private Context context;
    private List<Items> itemsArrayList;


    public MenuItemAdapter(List<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_menu_category, parent, false);
        return new MenuViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(context).load(itemsArrayList.get(position).getImage()).apply(options).into(holder.imageView1);
        holder.itemnametvr.setText(itemsArrayList.get(position).getItem_name());
        holder.itempricetvb.setText(itemsArrayList.get(position).getPrice() + " Rs");


        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        List<CartData> cartDataArrayList = new ArrayList<>();
        cartDataArrayList.clear();
        cartDataArrayList = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        /*if quantity is update from menu and in detail user not change in quantity so here we check first database quantity and if there then set database quantity*/
        if (!cartDataArrayList.isEmpty()){
            for (int i = 0; i <cartDataArrayList.size() ; i++) {
                Log.d("++++size1234","+++"+cartDataArrayList.size() + "++++" + cartDataArrayList.get(i).getCOLUMN_ITEM_ID() + "++++" + cartDataArrayList.get(i).getCOLUMN_ITEMS_QUANTITY());
                if (cartDataArrayList.get(i).getCOLUMN_ITEM_ID().equalsIgnoreCase(itemsArrayList.get(position).getItem_id())){
//                    Log.d("++++size1234if","+++"+cartDataArrayList.size() + "++++" + cartDataArrayList.get(i).getCOLUMN_ITEM_ID() + "++++" + cartDataArrayList.get(i).getCOLUMN_ITEMS_QUANTITY());
                    itemsArrayList.get(position).setQty(cartDataArrayList.get(i).getCOLUMN_ITEMS_QUANTITY());
//                    Log.d("++++size123456if","++++"+itemsArrayList.get(position).getQty());
                    holder.quantytvr.setText(itemsArrayList.get(position).getQty());
                }
            }
        }else {
            holder.quantytvr.setText(itemsArrayList.get(position).getQty());
        }


    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemll;
        RoundedImageView imageView1;
        TextviewRegular itemnametvr, quantytvr;
        TextviewBold itempricetvb;
        ImageView plusiv, minusiv, addtocartiv;
        ProgressBar progress;

        MenuViewHolder(View itemView) {
            super(itemView);
            itemll = itemView.findViewById(R.id.itemll);
            imageView1 = itemView.findViewById(R.id.imageView1);
            itemnametvr = itemView.findViewById(R.id.itemnametvr);
            itempricetvb = itemView.findViewById(R.id.itempricetvb);
            quantytvr = itemView.findViewById(R.id.quantytvr);
            plusiv = itemView.findViewById(R.id.plusiv);
            minusiv = itemView.findViewById(R.id.minusiv);
            addtocartiv = itemView.findViewById(R.id.addtocartiv);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
