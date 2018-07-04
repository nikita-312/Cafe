package com.conceptioni.cafeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.model.Category;
import com.conceptioni.cafeapp.utils.TextviewRegular;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<Category> categories;

    public MenuAdapter(List<Category> categories){
        this.categories = categories;
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
//        categories.get(position).getItems();
        holder.tvrCatName.setText(categories.get(position).getCname());
        Glide.with(context).load(categories.get(position).getCimage()).into(holder.civ);
//        holder.llMain.setOnClickListener(v -> {
//            holder.llMain.setBackgroundResource(R.drawable.orange_menu_drawable);
//            holder.tvrCatName.setVisibility(View.GONE);
//        });
    }
//
    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        TextviewRegular tvrCatName;
        ImageView civ;
        RelativeLayout llMain;
        public MenuViewHolder(View itemView) {
            super(itemView);
            tvrCatName = itemView.findViewById(R.id.tvrCatName);
            civ = itemView.findViewById(R.id.civ);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
