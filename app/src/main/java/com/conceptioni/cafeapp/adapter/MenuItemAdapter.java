package com.conceptioni.cafeapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.conceptioni.cafeapp.activity.DescriptionActivity;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.TextviewBold;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.makeramen.roundedimageview.RoundedImageView;

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

        holder.itemll.setOnClickListener((View v) ->
                context.startActivity(new Intent(context, DescriptionActivity.class).putExtra("ItemId", itemsArrayList.get(position).getItem_id()))
        );

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(context).load(itemsArrayList.get(position).getImage()).apply(options).into(holder.imageView1);
        holder.itemnametvr.setText(itemsArrayList.get(position).getItem_name());
        holder.itempricetvb.setText(itemsArrayList.get(position).getPrice() + " Rs");
        holder.quantytvr.setText(itemsArrayList.get(position).getQty());
        Log.d("++++++", "++++++" + position + "++++" + itemsArrayList.get(position).getQty());
//        holder.plusiv.setOnClickListener(v -> {
////            Flag = "A";
//            if (!itemsArrayList.get(position).getQty().equalsIgnoreCase("")){
//                int count = Integer.parseInt(itemsArrayList.get(position).getQty());
//                int Quantity = count + 1;
//                String finalQuantity = String.valueOf(Quantity);
//                holder.quantytvr.setText(finalQuantity);
//                CallQuantity(holder,finalQuantity,position,itemsArrayList.get(position).getItem_id());
//            }
//        });
//
//        holder.minusiv.setOnClickListener(v -> {
////            Flag = "A";
//            if (!itemsArrayList.get(position).getQty().equalsIgnoreCase("0") && !itemsArrayList.get(position).getQty().equalsIgnoreCase("1")){
//                int count = Integer.parseInt(itemsArrayList.get(position).getQty());
//                int Quantity = count - 1;
//                String finalQuantity = String.valueOf(Quantity);
//                holder.quantytvr.setText(finalQuantity);
//                CallQuantity(holder,finalQuantity,position,itemsArrayList.get(position).getItem_id());
//            }else {
//                new MakeToast("Quantity can not be less than 0");
//            }
//
//        });
//
//        holder.addtocartiv.setOnClickListener(v -> {
////            Flag = "C";
////            CallQuantity(holder,itemsArrayList.get(position).getQty(),position,itemsArrayList.get(position).getItem_id());
//        });
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
