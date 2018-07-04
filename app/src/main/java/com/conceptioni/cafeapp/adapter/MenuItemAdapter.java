package com.conceptioni.cafeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.DescriptionActivity;
import com.conceptioni.cafeapp.utils.TextviewBold;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder> {

    Context context;
    List<Items> itemsArrayList;

    public MenuItemAdapter(List<Items> itemsArrayList){
        this.itemsArrayList = itemsArrayList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_menu_category,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.itemll.setOnClickListener(v -> {
            context.startActivity(new Intent(context, DescriptionActivity.class).putExtra("ItemId",itemsArrayList.get(position).getItem_id()));
        });

//        Glide.with(context).load(itemsArrayList.get(position).getImage()).into(holder.imageView1);
        holder.itemnametvr.setText(itemsArrayList.get(position).getItem_name());
        holder.itempricetvb.setText(itemsArrayList.get(position).getPrice() + " Rs");
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemll;
        RoundedImageView imageView1;
        TextviewRegular itemnametvr;
        TextviewBold itempricetvb;
        public MenuViewHolder(View itemView) {
            super(itemView);
            itemll = itemView.findViewById(R.id.itemll);
            imageView1 = itemView.findViewById(R.id.imageView1);
            itemnametvr = itemView.findViewById(R.id.itemnametvr);
            itempricetvb = itemView.findViewById(R.id.itempricetvb);
        }
    }
}
