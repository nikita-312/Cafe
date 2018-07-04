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

public class LiveOrderAdapter extends RecyclerView.Adapter<LiveOrderAdapter.MenuViewHolder> {

    Context context;

    public LiveOrderAdapter(){

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_live_order,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
//        holder.itemll.setOnClickListener(v -> {
//            context.startActivity(new Intent(context, DescriptionActivity.class));
//        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemll;
        public MenuViewHolder(View itemView) {
            super(itemView);
            itemll = itemView.findViewById(R.id.itemll);
        }
    }
}
