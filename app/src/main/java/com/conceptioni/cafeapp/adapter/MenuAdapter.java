package com.conceptioni.cafeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.model.Category;
import com.conceptioni.cafeapp.utils.TextviewRegular;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    List<Category> categories;

    public MenuAdapter(List<Category> categories){

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_menu,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        categories.get(position).getItems();
        holder.tvrCatName.setText(categories.get(position).getCname());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        TextviewRegular tvrCatName;
        public MenuViewHolder(View itemView) {
            super(itemView);
            tvrCatName = itemView.findViewById(R.id.tvrCatName);
        }
    }
}
