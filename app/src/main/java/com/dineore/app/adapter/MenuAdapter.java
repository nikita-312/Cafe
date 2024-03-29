package com.dineore.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.dineore.app.R;
import com.dineore.app.model.Category;
import com.dineore.app.utils.TextviewRegular;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<Category> categories;

    public MenuAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.tvrCatName.setText(categories.get(position).getCname());
        Glide.with(context).load(categories.get(position).getCimage()).into(holder.civ);

        if (categories.get(position).isselect){
            holder.llMain.setBackgroundResource(R.drawable.select_category_drawable);
            holder.tvrCatName.setVisibility(View.VISIBLE);
            holder.tvrCatName.setTextColor(context.getResources().getColor(R.color.colorwhite));
        }else {
            holder.llMain.setBackgroundResource(R.drawable.category_drawable);
            holder.tvrCatName.setVisibility(View.VISIBLE);
            holder.tvrCatName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        TextviewRegular tvrCatName;
        ImageView civ;
        RelativeLayout llMain;

        MenuViewHolder(View itemView) {
            super(itemView);
            tvrCatName = itemView.findViewById(R.id.tvrCatName);
            civ = itemView.findViewById(R.id.civ);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
